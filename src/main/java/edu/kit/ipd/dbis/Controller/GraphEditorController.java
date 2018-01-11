package edu.kit.ipd.dbis.Controller;

public class GraphEditorController {

	private Database;

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
	public void setDatabase(Database database) {
		this.database = Database;
	}

}
