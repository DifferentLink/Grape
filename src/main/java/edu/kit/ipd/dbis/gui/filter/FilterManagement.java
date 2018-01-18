/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.filter;

import java.util.HashSet;
import java.util.Set;

public class FilterManagement {
	private Set<SimpleFilter> simpleFilter = new HashSet<>();
	private Set<FilterGroup> filterGroups = new HashSet<>();
	private int nextUniqueID = 0;

	public void addNewSimpleFilter() {
		simpleFilter.add(new SimpleFilter(getUniqueID()));
	}

	public void addNewSimpleFilterToGroup(FilterGroup filterGroup) {
		filterGroup.add(new SimpleFilter(getUniqueID()));
	}

	public void addNewFilterGroup() {
		filterGroups.add(new FilterGroup(getUniqueID()));
	}

	private int getUniqueID() {
		return nextUniqueID++;
	}

	public Set<SimpleFilter> getSimpleFilter() {
		return simpleFilter;
	}

	public Set<FilterGroup> getFilterGroups() {
		return filterGroups;
	}
}
