package edu.kit.ipd.dbis.controller;


import edu.kit.ipd.dbis.controller.exceptions.InvalidBfsCodeInputException;
import edu.kit.ipd.dbis.controller.exceptions.InvalidGeneratorInputException;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.gui.GrapeUI;
import edu.kit.ipd.dbis.gui.StatusbarUI;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.log.EventType;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkRandomConnectedGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.NotEnoughGraphsException;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import javax.swing.SwingUtilities;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The type Generate controller.
 */
public class GenerateController {

	private GraphDatabase database;
	private BulkGraphGenerator generator;
	private StatusbarController statusbar;
	private CalculationController calculation;
	private GrapeUI grapeUI;
	private StatusbarUI statusbarUI;

	private static GenerateController generate;

	private GenerateController() {
		this.statusbar = StatusbarController.getInstance();
		this.generator = new BulkRandomConnectedGraphGenerator();
		this.calculation = CalculationController.getInstance();
	}

	/**
	 * Gets instance.
	 *
	 * @return the instance
	 */
	public static GenerateController getInstance() {
		if (generate == null) {
			generate = new GenerateController();
		}
		return generate;
	}

	/**
	 * Sets grape ui.
	 *
	 * @param grapeUI the grape ui
	 */
	public void setGrapeUI(GrapeUI grapeUI) {
		this.grapeUI = grapeUI;
	}

	/**
	 * Sets statusbar ui.
	 *
	 * @param statusbarUI the statusbar ui
	 */
	public void setStatusbarUI(StatusbarUI statusbarUI) {
		this.statusbarUI = statusbarUI;
	}

	/**
	 * Replaces the old database with the given database.
	 *
	 * @param database the current database
	 */
	public void setDatabase(GraphDatabase database) {
		this.database = database;
	}

	/**
	 * Generate graphs.
	 *
	 * @param minVertices the min vertices
	 * @param maxVertices the max vertices
	 * @param minEdges    the min edges
	 * @param maxEdges    the max edges
	 * @param amount      the amount
	 * @throws InvalidGeneratorInputException the invalid generator input exception
	 * @throws InterruptedException           the interrupted exception
	 */
	public void generateGraphs(int minVertices, int maxVertices, int minEdges, int maxEdges, int amount) throws
			InterruptedException {
		if (!isValidGeneratorInput(minVertices, maxVertices, minEdges, maxEdges, amount)) {
			statusbar.addMessage("Invalid Input");
		} else {
			Set<PropertyGraph<Integer, Integer>> graphs = new HashSet<>();
			try {
				generator.generateBulk(graphs, amount, minVertices, maxVertices, minEdges, maxEdges);
			} catch (NotEnoughGraphsException e) {
			}
			//save uncalculated graphs
			this.saveGraphs(graphs);
			List<Thread> jobs = new LinkedList<>();
			for (PropertyGraph<Integer, Integer> graph : graphs) {
				jobs.add(new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							graph.calculateProperties();
							database.replaceGraph(graph.getId(), graph);
						} catch (ConnectionFailedException | InsertionFailedException | UnexpectedObjectException e) {
							statusbar.addMessage(e.getMessage());
						}
					}
				}));
			}
			int runningJobs = 0;
			final int maxJobs = 8 * Runtime.getRuntime().availableProcessors();

			for (Thread job : jobs) {
				job.start();
				if (runningJobs < maxJobs) {
					runningJobs++;
				} else {
					try {
						job.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
			for (Thread job : jobs) {
				job.join();
			}

			//create log entry
			Set<Integer> changedGraphs = new HashSet<>();
			for (PropertyGraph<Integer, Integer> graph : graphs) {
				if (graph.getId() != 0) {
					changedGraphs.add(graph.getId());
				}
			}
			if (changedGraphs.size() > 0) {
				if (changedGraphs.size() < amount) {
					statusbar.addEvent(new Event(EventType.ADD, changedGraphs.size() + " graphs were generated " + amount +
							" different graphs haven't been found", changedGraphs));
				} else {
					statusbar.addEvent(new Event(EventType.ADD, changedGraphs.size() + " graphs were generated", changedGraphs));
				}
			} else {
				statusbar.addMessage("All possible graphs already exists in the database");
			}
			this.statusbar.setNumberOfGraphs();
			this.grapeUI.updateTable();
		}
	}

	/**
	 * Creates a graph with the BFS Code and saves it in the Database.
	 *
	 * @param bfsCode the BFS Code of the graph to save.
	 *                //@throws InvalidBfsCodeInputException the invalid bfs code input exception
	 */
	public void generateBFSGraph(String bfsCode) throws InvalidBfsCodeInputException {
		if (!isValidBFS(bfsCode)) {
			throw new InvalidBfsCodeInputException("Wrong BFS input");
		} else {
			// Parsing String into int[]
			String[] splitCode = bfsCode.split(",");
			int[] code = new int[splitCode.length];
			for (int i = 0; i < splitCode.length; i++) {
				code[i] = Integer.parseInt(splitCode[i]);
			}
			// Creating BfsCode Object
			BfsCodeAlgorithm.BfsCodeImpl bfs = new BfsCodeAlgorithm.BfsCodeImpl(code);
			try {
				PropertyGraph<Integer, Integer> graph = new PropertyGraph<>(bfs);
				boolean graphExists = false;
				graphExists = database.graphExists(graph);
				database.addGraph(graph);
				calculation.run();
				this.grapeUI.updateTable();

				if (graphExists) {
					//TODO: message is shown if the graph was deleted before (don't know if graph is visible)
					//TODO: how can i know if a graph is markes as deleted or not? -> else wrong message (create deleted graph)
					statusbar.addMessage("BFS-Graph: " + bfsCode + " already exists");
				} else {
					statusbar.addEvent(EventType.ADD, graph.getId(), "Graph added with BFS-Code: " + bfsCode);
				}

			} catch (ConnectionFailedException | UnexpectedObjectException | InsertionFailedException e) {
				statusbar.addMessage(e.getMessage());
			} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
				statusbar.addMessage("Illegal bfs code");
			}
		}
	}

	/**
	 * Deletes the given graph from the GUI table.
	 *
	 * @param id the ID of the PropertyGraph<V,E>.
	 */
	public void deleteGraph(int id) {
		try {
			database.deleteGraph(id);
			statusbar.addEvent(EventType.REMOVE, id, "Graph " + id + " deleted");
			grapeUI.updateTable();
		} catch (ConnectionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * Saves the graphs in the Database in the list of not yet calculated graphs.
	 *
	 * @param graphs the set of PropertyGraph<V,E>
	 */
	private void saveGraphs(Set<PropertyGraph<Integer, Integer>> graphs) {
		for (PropertyGraph<Integer, Integer> graph : graphs) {
			try {
				database.addGraph(graph);
				this.statusbarUI.setRemainingCalculations(0);
			} catch (ConnectionFailedException | InsertionFailedException | UnexpectedObjectException e) {
				statusbar.addMessage(e.getMessage());
			}
		}
	}

	/**
	 * Is valid bfs boolean.
	 *
	 * @param bfsCode the bfs code
	 * @return the boolean
	 */
	public static Boolean isValidBFS(String bfsCode) {
		if (!bfsCode.matches("(-?1,\\d+,\\d+)(,-?1,\\d+,\\d+)*")) {
			return false;
		}
		// convert to int Array
		String[] splitCode = bfsCode.split(",");
		int[] code = new int[splitCode.length];
		for (int i = 0; i < splitCode.length; i++) {
			code[i] = Integer.parseInt(splitCode[i]);
		}
		for (int i = 0; i < splitCode.length - 3; i += 3) {
			if (code[i] != 1 && code[i] != -1) {
				return false;
			}
			if (code[i + 1] < 0 || code[i + 2] < 0) {
				return false;
			}
			if (code[i + 1] >= code[i + 2]) {
				return false;
			}
		}
		return true;
	}

	private Boolean isValidGeneratorInput(int minVertices, int maxVertices, int minEdges, int maxEdges, int amount) {
		return minVertices >= 0 && minEdges >= 0 && maxEdges >= 0 && maxVertices >= 0 && amount >= 1;
	}

	public static boolean isValidVerticesInput(String input) {
		return input.matches("\\d+") || input.matches("\\d+-\\d+");
	}

	public static boolean isValidEdgesInput(String input) {
		return input.matches("\\d+") || input.matches("\\d+-\\d+");
	}

	public static boolean isValidNumberOfGraphs(String input) {
		return input.matches("\\d+");
	}
}
