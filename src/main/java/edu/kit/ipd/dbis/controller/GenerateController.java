package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.GraphDatabase;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.generate.BulkRandomConnectedGraphGenerator;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Generate controller.
 */
public class GenerateController {

	private GraphDatabase database;



	private BulkGraphGenerator generator;

	//TODO: Singleton pattern
	private static GenerateController generate;

	private GenerateController(){
		this.generator = new BulkRandomConnectedGraphGenerator();
	}

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
	public void setDatabase(GraphDatabase database) {
		this.database = database;
	}

	/**
	 * Gives the graph generator the command to generate the graphs and saves them in the Database.
	 * @param minVertices lower bound of vertices
	 * @param maxVertices upper bound of vertices
	 * @param minEdges lower bound of edges.
	 * @param maxEdges upper bound of edges.
	 * @param amount the number of graphs
	 */
	public void generateGraphs(int minVertices, int maxVertices, int minEdges, int maxEdges, int amount) throws Exception {
		Set<PropertyGraph<Integer, Integer>> graphs = new HashSet<PropertyGraph<Integer,Integer>>();
		//generator.generateBulk(graphs, amount, minVertices, maxVertices, minEdges, maxEdges);
		this.saveGraphs(graphs);
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
	public void delGraph(int id) throws Exception {
		database.deleteGraph(id);
	}

	/**
	 * Saves the graphs in the Database in the list of not yet calculated graphs.
	 * @param graphs the set of PropertyGraph<V,E>
	 */
	private void saveGraphs(Set<PropertyGraph<Integer,Integer>> graphs) throws Exception {
		for (PropertyGraph<Integer,Integer> graph: graphs) {
			database.addGraph(graph);
		}
	}

	private Boolean isValidBFS(String bfsCode) {
		return true;
	}

	private Boolean isValidGeneratorInput(int minVertices, int maxVertices, int minEdges, int maxEdges, int amount) {
		return true;
	}
}

