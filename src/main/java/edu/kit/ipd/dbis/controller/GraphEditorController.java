package edu.kit.ipd.dbis.controller;

import edu.kit.ipd.dbis.database.GraphDatabase;
import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.TotalColoringAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

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
	 * @param graph the PropertyGraph<V,E> to add.
	 * @param oldID the id of the modified graph from the Grapheditor.
	 */
	public void addEditedGraph(PropertyGraph graph, int oldID) {

	}

	/**
	 * checks if the graph is valid
	 *
	 * @param graph the PropertyGraph<V,E> to check.
	 * @return true if the given graph is valid.
	 */
	public boolean isValidGraph(PropertyGraph graph) {
		return true;
	}

	/**
	 * triggers the calculation of the next denser graph for a specific graph
	 *
	 * @param graph the PropertyGraph<V,E> to calculate.
	 */
	public void calculateDenserGraph(PropertyGraph graph) {

	}

/*	*//**
	 * calculates a valid colorization for a specific graph.
	 *
	 * @param graph the PropertyGraph<V,E> to calculate.
	 * @return the graphcolorization.
	 *//*
	public Coloring getVertexColoring(PropertyGraph graph) {
		return null;
	}*/

	/**
	 * calculates a valid colorization for a specic graph
	 *
	 * @param graph the PropertyGraph<V,E> to calculate.
	 * @return the graphcolorization.
	 */
	public TotalColoringAlgorithm.TotalColoring getTotalColoring(PropertyGraph graph) {
		return null;
	}

/*	*//**
	 * calculates a coloring which is not equivalent to current coloring
	 *
	 * @param graph the PropertyGraph<V,E> to calculate.
	 * @return the next valid alternative Coloring.
	 * @throws NoEquivalentColoringException thrown if there is no equivalent colorization for a specific graph
	 *//*
	public Coloring getAlternateVertexColoring(PropertyGraph graph) {
		return null;
	}*/

	/**
	 * calculates a coloring which is not equivalent to current coloring
	 *
	 * @param graph the PropertyGraph<V,E> to calculate.
	 * @return the next valid alternative Coloring.
	 * @throws NoEquivalentColoringException thrown if there is no equivalent colorization for a specific graph
	 */
	public TotalColoringAlgorithm.TotalColoring getAlternateTotalColoring(PropertyGraph graph) {
		return null;
	}

}
