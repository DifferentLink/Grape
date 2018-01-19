/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterManagement {
	private List<SimpleFilter> simpleFilter = new ArrayList<>();
	private List<FilterGroup> filterGroups = new ArrayList<>();
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

	public List<SimpleFilter> getSimpleFilter() {
		return simpleFilter;
	}

	public List<FilterGroup> getFilterGroups() {
		return filterGroups;
	}

	public void remove(int id) {

		Filter filterToRemove = null;

		for (Filter filter : simpleFilter) {
			if (filter.getID() == id) {
				filterToRemove = filter;
			}
		}
		if (filterToRemove != null) {
			simpleFilter.remove(filterToRemove);
		}

		for (Filter filter : filterGroups) {
			if (filter.getID() == id) {
				filterToRemove = filter;
				return;
			}
		}
		if (filterToRemove != null) {
			filterGroups.remove(filterToRemove);
		}
	}
}
