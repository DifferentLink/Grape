package edu.kit.ipd.dbis.gui.filter;

/**
 * A SimpleFilter. It's text is directly associated with a filter expression to filter the database.
 */
public class SimpleFilter extends Filter {

	/**
	 * Every filter has a unique ID. This is ensured by the UIFilterManagement
	 * @param id the unique ID
	 */
	public SimpleFilter(int id) {
		super(id);
	}

	/**
	 * Every filter has a unique ID. This is ensured by the UIFilterManagement
	 * @param id the unique ID
	 * @param text the simplefilter's name
	 */
	public SimpleFilter(int id, String text) {
		super(id);
		super.setText(text);
	}
}
