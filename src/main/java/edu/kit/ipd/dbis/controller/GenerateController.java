package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.controller.exceptions.InvalidBfsCodeInputException;
import edu.kit.ipd.dbis.controller.util.CalculationMaster;
import edu.kit.ipd.dbis.controller.util.CalculationWorker;
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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * The type Generate controller.
 */
public class GenerateController {

	private ResourceBundle language;
	private GraphDatabase database;
	private BulkGraphGenerator graphGenerator;
	private StatusbarController statusbarController;
	private GrapeUI grapeUI;
	private StatusbarUI statusbarUI;
	private static GenerateController generateController;

	private GenerateController() {
		this.statusbarController = StatusbarController.getInstance();
		this.graphGenerator = new BulkRandomConnectedGraphGenerator();
	}

	/**
	 * Gets instance.
	 *
	 * @return the instance
	 */
	public static GenerateController getInstance() {
		if (generateController == null) {
			generateController = new GenerateController();
		}
		return generateController;
	}

	public void setLanguage(final ResourceBundle language) {
		this.language = language;
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
	 * Sets statusbarController ui.
	 *
	 * @param statusbarUI the statusbarController ui
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
	 */
	public void generateGraphs(int minVertices, int maxVertices, int minEdges, int maxEdges, int amount) {
		if (!isValidGeneratorInput(minVertices, maxVertices, minEdges, maxEdges, amount)) {
			statusbarController.addMessage("Invalid Input");
		} else {
			Set<PropertyGraph<Integer, Integer>> graphs = new HashSet<>();
			try {
				graphGenerator.generateBulk(graphs, amount, minVertices, maxVertices, minEdges, maxEdges);
			} catch (NotEnoughGraphsException ignored) { }
			this.saveGraphs(graphs);
			List<Thread> jobs = new LinkedList<>();
			for (PropertyGraph<Integer, Integer> graph : graphs) {
				jobs.add(new CalculationWorker(graph, database));
			}

			CalculationMaster.executeCalculation(jobs);

			// Create log entry
			List<Integer> changedGraphs = new LinkedList<>();
			for (PropertyGraph<Integer, Integer> graph : graphs) {
				if (graph.getId() != 0) {
					changedGraphs.add(graph.getId());
				}
			}
			if (changedGraphs.size() > 0) {
				if (changedGraphs.size() < amount) {
					statusbarController.addEvent(new Event(EventType.ADD,  changedGraphs.size()
							+ " " + language.getString("graphsIncompleteGenerated") + " "
							+ amount + language.getString("found"),
							changedGraphs));
				} else {
					statusbarController.addEvent(new Event(EventType.ADD,  changedGraphs.size()
							+ " " + language.getString("graphsGenerated"),
							changedGraphs));
				}
			} else {
				statusbarController.addMessage(language.getString("allPossibleGraphsExist"));
			}
			this.statusbarController.setNumberOfGraphs();
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
			throw new InvalidBfsCodeInputException(language.getString("invalidBFSCode"));
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
				boolean graphExists;
				graphExists = database.graphExists(graph);
				database.addGraph(graph);
				Thread thread = new CalculationWorker(graph, database);
				thread.start();
				thread.join();
				this.grapeUI.updateTable();
				this.statusbarController.setNumberOfGraphs();

				if (graphExists) {
					statusbarController.addMessage(language.getString("graph") + " " +  bfsCode
							+ " " + language.getString("alreadyExists"));
				} else {
					statusbarController.addEvent(EventType.ADD, graph.getId(),
							language.getString("graphAdded") + ": " + bfsCode);
				}

			} catch (ConnectionFailedException | UnexpectedObjectException | InsertionFailedException e) {
				statusbarController.addMessage(e.getMessage());
			} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
				statusbarController.addMessage(language.getString("invalidBFSCode"));
			} catch (InterruptedException ignored) { }
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
			statusbarController.addEvent(EventType.REMOVE, id, language.getString("graphDeleted"));
			grapeUI.updateTable();
			this.statusbarController.setNumberOfGraphs();
		} catch (ConnectionFailedException e) {
			statusbarController.addMessage(e.getMessage());
		}
	}

	public void deleteGraphs(List<Integer> ids) {
		try {
			for (int id : ids) {
				database.deleteGraph(id);
				grapeUI.updateTable();
				this.statusbarController.setNumberOfGraphs();
			}
			statusbarController.addEvent(EventType.REMOVE, ids,  language.getString("graphDeleted"));
		} catch (ConnectionFailedException e) {
				statusbarController.addMessage(e.getMessage());
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
				statusbarController.addMessage(e.getMessage());
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
