package edu.kit.ipd.dbis.database.file.parsers;

import edu.kit.ipd.dbis.database.connection.GraphDatabase;

/**
 *
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
		String information = this.database.getGraphTable().getUrl()
				+ ";" + this.database.getGraphTable().getUser()
				+ ";" + this.database.getGraphTable().getPassword()
				+ ";" + this.database.getGraphTable().getName()
				+ ";" + this.database.getFilterTable().getUrl()
				+ ";" + this.database.getFilterTable().getUser()
				+ ";" + this.database.getFilterTable().getPassword()
				+ ";" + this.database.getFilterTable().getName();
		this.information = information;
	}

}
