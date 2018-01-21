package edu.kit.ipd.dbis.database.file.parsers;

import edu.kit.ipd.dbis.database.connection.tables.FilterTable;
import edu.kit.ipd.dbis.database.connection.GraphDatabase;
import edu.kit.ipd.dbis.database.connection.tables.GraphTable;
import edu.kit.ipd.dbis.database.exceptions.files.FileContentNotAsExpectedException;

/**
 *
 */
public class LoadParser extends Parser {

	/**
	 * and sets the given information as attribute.
	 * @param information contains all the information that should be saved in the text file
	 */
	public LoadParser(String information) {
		this.information = information;
	}

	@Override
	public void parse()
			throws FileContentNotAsExpectedException {
		try {
			String[] content = this.information.split(";");
			GraphTable graphTable = new GraphTable(content[0], content[1], content[2], content[3]);
			FilterTable filterTable = new FilterTable(content[4], content[5],content[6],content[7]);
			this.database = new GraphDatabase(graphTable, filterTable);
		} catch (Exception e) {
			throw new FileContentNotAsExpectedException();
		}

	}

}
