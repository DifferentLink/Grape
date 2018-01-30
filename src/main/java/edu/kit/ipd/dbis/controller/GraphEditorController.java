package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.exceptions.sql.*;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.color.MinimalTotalColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.color.MinimalVertexColoring;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.density.NextDenserGraphFinder;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

public class GraphEditorController {

	private GraphDatabase database;

	//TODO: Singleton pattern
	private static GraphEditorController editor;

	private GraphEditorController() {
	}

	public static GraphEditorController getInstance() {
		if (editor == null) {
			editor = new GraphEditorController();
		}
		return editor;
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
	 * checks the given graph for duplicates then adds the graph to the not yet calculated
	 * graphlist of CalculationController and deletes the old graph from the
	 * database.
	 *
	 * @param newGraph the PropertyGraph<V,E> to add.
	 * @param oldID    the id of the modified graph from the Grapheditor.
	 */
	public void addEditedGraph(PropertyGraph newGraph, int oldID) throws DatabaseDoesNotExistException, AccessDeniedForUserException, ConnectionFailedException, TablesNotAsExpectedException, UnexpectedObjectException, InsertionFailedException {
		if (database.graphExists(newGraph)) {
			return;
		} else {
			database.addGraph(newGraph);
			database.deleteGraph(oldID);
		}
	}

	public void addNewGraph(PropertyGraph graph) { // todo implement me

	}

	/**
	 * checks if the graph is valid
	 *
	 * @param graph the PropertyGraph<V,E> to check.
	 * @return true if the given graph is valid.
	 */
	public boolean isValidGraph(PropertyGraph graph) {
		return true;
	} // todo throw exception for gui

	/**
	 * triggers the calculation of the next denser graph for a specific graph
	 *
	 * @param graph the PropertyGraph<V,E> to calculate.
	 */
	public void addNextDenserToDatabase(PropertyGraph graph) {
		NextDenserGraphFinder denserGraph = new NextDenserGraphFinder(graph);
		//database.addGraph(denserGraph.getNextDensityGraph());
	}

	/**
	 * calculates a valid colorization for a specific graph.
	 *
	 * @param graph the PropertyGraph<V,E> to calculate.
	 * @return the graphcolorization.
	 */
	public VertexColoringAlgorithm.Coloring getVertexColoring(PropertyGraph graph) {
		MinimalVertexColoring coloring = new MinimalVertexColoring(graph);
		return coloring.getColoring();
	}

	/**
	 * calculates a valid colorization for a specific graph
	 *
	 * @param graph the PropertyGraph<V,E> to calculate.
	 * @return the graphcolorization.
	 */
	public TotalColoringAlgorithm.TotalColoring getTotalColoring(PropertyGraph graph) {
		MinimalTotalColoring coloring = new MinimalTotalColoring(graph, graph.getVertexFactory());
		return coloring.getColoring();
	}

	/**
	 * calculates a coloring which is not equivalent to current coloring
	 *
	 * @param graph the PropertyGraph<V,E> to calculate.
	 * @return the next valid alternative Coloring.
	 * @throws //NoEquivalentColoringException thrown if there is no equivalent colorization for a specific graph
	 */
	public VertexColoringAlgorithm.Coloring getAlternateVertexColoring(int id) {
		return null;
	}

	/**
	 * calculates a coloring which is not equivalent to current coloring
	 *
	 * @param graph the PropertyGraph<V,E> to calculate.
	 * @return the next valid alternative Coloring.
	 * @throws //NoEquivalentColoringException thrown if there is no equivalent colorization for a specific graph
	 */
	public TotalColoringAlgorithm.TotalColoring getAlternateTotalColoring(int id) {
		return null;
	}

}
