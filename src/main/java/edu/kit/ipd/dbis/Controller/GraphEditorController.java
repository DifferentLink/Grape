package edu.kit.ipd.dbis.Controller;

import edu.kit.ipd.dbis.database.GraphDatabase;

public class GraphEditorController {

	private GraphDatabase database;

	private static GraphEditorController editor;

	private GraphEditorController(){}

	public static GraphEditorController getInstance() {
		if(editor == null) {
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

}
