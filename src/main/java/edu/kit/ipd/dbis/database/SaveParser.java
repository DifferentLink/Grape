package edu.kit.ipd.dbis.database;

/**
 *
 */
public class SaveParser extends Parser {

	/**
	 *
	 * @param database
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
