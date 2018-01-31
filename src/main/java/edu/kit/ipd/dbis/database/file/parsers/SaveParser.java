package edu.kit.ipd.dbis.database.file.parsers;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;

/**
 * Stores all the information that is necessary to load the given GraphDatabaseObject into a String.
 */
public class SaveParser extends Parser {

	/**
	 * Creates a SaveParser-Object and sets the given database as attribute.
	 * @param database contains all the information that should be saved in the text file.
	 */
	public SaveParser(GraphDatabase database) {
		this.database = database;
	}

	@Override
	public void parse() {
		String graphUrl = this.database.getGraphTable().getUrl();
		String filterUrl = this.database.getFilterTable().getUrl();
		String information = graphUrl.substring(0, graphUrl.length() - 32)
				+ ";" + this.database.getGraphTable().getUser()
				+ ";" + this.database.getGraphTable().getPassword()
				+ ";" + this.database.getGraphTable().getName()
				+ ";" + filterUrl.substring(0, filterUrl.length() - 32)
				+ ";" + this.database.getFilterTable().getUser()
				+ ";" + this.database.getFilterTable().getPassword()
				+ ";" + this.database.getFilterTable().getName();
		this.information = information;
	}

}
