package edu.kit.ipd.dbis.controller;


import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.ConnectionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.InsertionFailedException;
import edu.kit.ipd.dbis.database.exceptions.sql.UnexpectedObjectException;
import edu.kit.ipd.dbis.gui.GrapeUI;
import edu.kit.ipd.dbis.gui.StatusbarUI;
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
	private FilterController filter;
	private CalculationController calculation;
	private GrapeUI grapeUI;
	private StatusbarUI statusbarUI;

	/**
	 * @param grapeUI the GUI to manage
	 */
	public void setGrapeUI(GrapeUI grapeUI) {
		this.grapeUI = grapeUI;
	}

	//TODO: Singleton pattern
	private static GenerateController generate;

	private GenerateController() {
		this.statusbar = StatusbarController.getInstance();
		this.generator = new BulkRandomConnectedGraphGenerator();
		this.calculation = CalculationController.getInstance();
		this.filter = FilterController.getInstance();
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
	 * Gives the graph generator the command to generate the graphs and saves them in the Database.
	 *
	 * @param minVertices lower bound of vertices
	 * @param maxVertices upper bound of vertices
	 * @param minEdges    lower bound of edges.
	 * @param maxEdges    upper bound of edges.
	 * @param amount      the number of graphs
	 * @throws InvalidGeneratorInputException the invalid generator input exception
	 */
	public void generateGraphsSequential(int minVertices, int maxVertices, int minEdges, int maxEdges, int amount) throws
			InvalidGeneratorInputException {
		if (!isValidGeneratorInput(minVertices, maxVertices, minEdges, maxEdges, amount)) {
			throw new InvalidGeneratorInputException();
		}
		Set<PropertyGraph<Integer, Integer>> graphs = new HashSet<>();
		try {
			generator.generateBulk(graphs, amount, minVertices, maxVertices, minEdges, maxEdges);
			this.saveGraphs(graphs);
			Thread calculate = new Thread(CalculationController.getInstance());
			SwingUtilities.invokeLater(calculate);
		} catch (IllegalArgumentException e) {
			throw new InvalidGeneratorInputException();
		} catch (NotEnoughGraphsException e) {
			statusbar.addMessage(e.getMessage());
			this.saveGraphs(graphs);
			Thread calculate = new Thread(CalculationController.getInstance());
			SwingUtilities.invokeLater(calculate);
		}
	}

	public void generateGraphs(int minVertices, int maxVertices, int minEdges, int maxEdges, int amount) throws
			InvalidGeneratorInputException {
		if (!isValidGeneratorInput(minVertices, maxVertices, minEdges, maxEdges, amount)) {
			throw new InvalidGeneratorInputException();
		}

		Set<PropertyGraph<Integer, Integer>> graphs = new HashSet<>();
		try {
			try {
				generator.generateBulk(graphs, amount, minVertices, maxVertices, minEdges, maxEdges);
			} catch (NotEnoughGraphsException e){
				statusbar.addMessage(e.getMessage());
			}
			this.saveGraphs(graphs);

			List<Thread> jobs = new LinkedList<>();
			for (PropertyGraph<Integer, Integer> graph : graphs) {
				jobs.add(new Thread(new Runnable() {
					@Override
					public void run() {
						graph.calculateProperties();
						try {
							database.replaceGraph(graph.getId(), graph);
						} catch (ConnectionFailedException e) {
							e.printStackTrace();
						} catch (InsertionFailedException e) {
							e.printStackTrace();
						} catch (UnexpectedObjectException e) {
							e.printStackTrace();
						}
						System.out.println("Finished graph: " + graph.toString());
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
					job.join();
				}
			}

			for (Thread job : jobs) {
				job.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		grapeUI.updateTable();
	}
		/**
	 * Generate empty graph.
	 */
	public void generateEmptyGraph() { // todo please implement me
		try {
			generateGraphs(0, 0, 0, 0, 1);
		} catch (InvalidGeneratorInputException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * Creates a graph with the BFS Code and saves it in the Database.
	 *
	 * @param bfsCode the BFS Code of the graph to save.
	 * @throws InvalidBfsCodeInputException the invalid bfs code input exception
	 */

	public void generateBFSGraph(String bfsCode) throws InvalidBfsCodeInputException { //TODO: check for valid BFS input
		// Parsing String into int[]
		String[] splitCode = bfsCode.split(",");
		int[] code = new int[splitCode.length];
		for (int i = 0; i < splitCode.length; i++) {
			code[i] = Integer.parseInt(splitCode[i]);
		}
		// Creating BfsCode Object
		BfsCodeAlgorithm.BfsCodeImpl bfs = new BfsCodeAlgorithm.BfsCodeImpl(code);
		PropertyGraph<Integer, Integer> graph = new PropertyGraph<>(bfs);
		try {
			database.addGraph(graph);
			calculation.run();
			this.grapeUI.updateTable();
		} catch (ConnectionFailedException | UnexpectedObjectException | InsertionFailedException e) {
			statusbar.addMessage(e.getMessage());
		}
	}

	/**
	 * Deletes the given graph from the GUI table.
	 *
	 * @param id the ID of the PropertyGraph<V,E>.
	 */
	public void delGraph(int id) {
		try {
			database.deleteGraph(id);
			statusbar.addEvent(EventType.REMOVE, id);
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
	public Boolean isValidBFS(String bfsCode) {
		String[] splitCode = bfsCode.split(",");
		for (int i = 0; i < splitCode.length; i++) {
			if (!isNumeric(splitCode[i])) {
				return false;
			}
		}
		return true;
	}

	private Boolean isValidGeneratorInput(int minVertices, int maxVertices, int minEdges, int maxEdges, int amount) {
		if (minVertices >= 0 && minEdges >= 0 && maxEdges >= 0 && maxVertices >= 0 && amount >= 1) {
			return true;
		} else {
			return false;
		}
	}

	private Boolean isNumeric(String text) {
		try {
			int number = Integer.parseInt(text);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
