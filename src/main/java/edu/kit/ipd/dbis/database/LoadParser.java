package edu.kit.ipd.dbis.database;

/**
 *
 */
public class LoadParser extends Parser {

	/**
	 *
	 * @param information
	 */
	public LoadParser(String information) {
		this.information = information;
	}

	@Override
	public void parse() {
		String[] content = this.information.split(";");
		GraphTable graphTable = new GraphTable(content[0], content[1], content[2], content[3]);
		FilterTable filterTable = new FilterTable(content[4], content[5],content[6],content[7]);
		this.database = new GraphDatabase(graphTable, filterTable);
	}

}
