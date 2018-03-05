package edu.kit.ipd.dbis.controller;


import edu.kit.ipd.dbis.controller.exceptions.InvalidBfsCodeInputException;
import edu.kit.ipd.dbis.controller.exceptions.InvalidGeneratorInputException;
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
			InvalidGeneratorInputException, InterruptedException {
		if (!isValidGeneratorInput(minVertices, maxVertices, minEdges, maxEdges, amount)) {
			throw new InvalidGeneratorInputException();
		}

		Set<PropertyGraph<Integer, Integer>> graphs = new HashSet<>();
		try {
			generator.generateBulk(graphs, amount, minVertices, maxVertices, minEdges, maxEdges);
		} catch (NotEnoughGraphsException e) {
			statusbar.addMessage(e.getMessage());
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
						statusbar.addEvent(EventType.ADD, graph.getId());
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
		grapeUI.updateTable();
	}

	/**
	 * Creates a graph with the BFS Code and saves it in the Database.
	 *
	 * @param bfsCode the BFS Code of the graph to save.
	 *                //@throws InvalidBfsCodeInputException the invalid bfs code input exception
	 */
	public void generateBFSGraph(String bfsCode) throws InvalidBfsCodeInputException {
		if (!isValidBFS(bfsCode)) {
			throw new InvalidBfsCodeInputException("wrong input");
		} else {
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
	}

	/**
	 * Deletes the given graph from the GUI table.
	 *
	 * @param id the ID of the PropertyGraph<V,E>.
	 */
	public void deleteGraph(int id) {
		try {
			database.deleteGraph(id);
			statusbar.addEvent(EventType.REMOVE, id);
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
	public Boolean isValidBFS(String bfsCode) {
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
