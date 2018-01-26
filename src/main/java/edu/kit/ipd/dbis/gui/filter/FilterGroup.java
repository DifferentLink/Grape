/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterGroup extends Filter {

	private List<SimpleFilter> simpleFilter = new ArrayList<>();

	public FilterGroup(final int id) {
		super(id);
	}

	public FilterGroup(int id, String name) {
		super(id);
		super.setText(name);
	}

	public void add(SimpleFilter filter) {
		simpleFilter.add(filter);
	}

	public List<SimpleFilter> getSimpleFilter() {
		return simpleFilter;
	}
}
