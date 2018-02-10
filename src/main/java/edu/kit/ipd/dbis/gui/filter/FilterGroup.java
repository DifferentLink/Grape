package edu.kit.ipd.dbis.gui.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of SimpleFilters
 */
public class FilterGroup extends Filter {

	private List<SimpleFilter> simpleFilter = new ArrayList<>();

	/**
	 * Every filter has a unique ID. This is ensured by the UIFilterManagement
	 * @param id the unique ID
	 */
	public FilterGroup(final int id) {
		super(id);
	}

	/**
	 * Every filter has a unique ID. This is ensured by the UIFilterManagement
	 * @param id the unique ID
	 * @param name the filtergroup's name
	 */
	public FilterGroup(int id, String name) {
		super(id);
		super.setText(name);
	}

	/**
	 * Add a SimpleFilter to the associated filters
	 * @param filter the SimpleFilter
	 */
	public void add(SimpleFilter filter) {
		simpleFilter.add(filter);
	}

	/**
	 * @return the with this FilterGroup associated SimpleFilters
	 */
	public List<SimpleFilter> getSimpleFilter() {
		return simpleFilter;
	}
}
