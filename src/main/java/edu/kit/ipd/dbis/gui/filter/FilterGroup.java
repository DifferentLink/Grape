/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.filter;

import java.util.HashSet;
import java.util.Set;

public class FilterGroup extends Filter {

	private Set<SimpleFilter> simpleFilter = new HashSet<>();

	public FilterGroup(final int id) {
		super(id);
	}

	public void add(SimpleFilter filter) {
		simpleFilter.add(filter);
	}
}
