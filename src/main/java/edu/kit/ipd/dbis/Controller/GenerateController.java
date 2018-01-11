package edu.kit.ipd.dbis.Controller;

/**
 * The type Generate controller.
 */
public class GenerateController {

	private Database;

	private static GenerateController generate;

	private GenerateController(){}

	public static GenerateController getInstance() {
		if(generate == null) {
			generate = new GenerateController();
		}
		return generate;
	}

	/**
	 * Replaces the old database with the given database.
	 * @param database the current database
	 */
	public void setDatabase(Database database) {
		this.database = Database;
	}

	/**
	 * Gives the graph generator the command to generate the graphs and saves them in the Database.
	 * @param minVertices lower bound of vertices
	 * @param maxVertices upper bound of vertices
	 * @param minEdges lower bound of edges.
	 * @param maxEdges upper bound of edges.
	 * @param amount the number of graphs
	 */
	public void generateGraphs(int minVertices, int maxVertices, int minEdges, int maxEdges, int amount) {

	}

	/**
	 * Calculates a graph with the BFS Code and saves it to the Database.
	 * @param bfsCode the BFS Code of the graph to save.
	 */
	public void generateBFSGraph(String bfsCode) {

	}

	/**
	 * Deletes the given graph from the GUI table.
	 * @param id the ID of the PropertyGraph<V,E>.
	 */
	public void delGraph(int id) {

	}

	/**
	 * Saves the graphs in the Database in the list of not yet calculated graphs.
	 * @param graphs the set of PropertyGraph<V,E>
	 */
	public void saveGraphs(int graphs) {

	}

	private Boolean isValidBFS(String bfsCode) {
		return true;
	}

	private Boolean isValidGeneratorInput(int minVertices, int maxVertices, int minEdges, int maxEdges, int amount) {
		return true;
	}
}

