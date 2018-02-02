/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.filter;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UIFilterManager {

	private List<SimpleFilter> simpleFilter = new ArrayList<>();
	private List<FilterGroup> filterGroups = new ArrayList<>();
	private int nextUniqueID = 0;

	public void addNewSimpleFilter() {
		SimpleFilter newFilter = new SimpleFilter(getUniqueID());
		simpleFilter.add(newFilter);
	}

	private void addSimpleFilterToGroup(FilterGroup filterGroup, SimpleFilter simpleFilter) {
		filterGroup.add(simpleFilter);
	}

	public void addNewSimpleFilterToGroup(FilterGroup filterGroup) {
		filterGroup.add(new SimpleFilter(getUniqueID()));
	}

	public void addNewFilterGroup(String name) {
		FilterGroup newGroup = new FilterGroup(getUniqueID(), name);
		filterGroups.add(newGroup);
	}

	public int getUniqueID() {
		return nextUniqueID++;
	}

	public List<SimpleFilter> getSimpleFilter() {
		return simpleFilter;
	}

	public List<FilterGroup> getFilterGroups() {
		return filterGroups;
	}

	public void remove(int id) {
		simpleFilter.removeIf(simpleFilter -> simpleFilter.getID() == id);
		filterGroups.removeIf(filterGroup -> filterGroup.getID() == id);
		filterGroups.forEach(filterGroup -> filterGroup.getSimpleFilter().removeIf(simpleFilter1 -> simpleFilter1.getID() == id));
	}

	public String visibleFiltersToString() {

		StringBuilder output = new StringBuilder();

		for (FilterGroup filterGroup : filterGroups) {
			for (SimpleFilter simpleFilter : filterGroup.getSimpleFilter()) {
				if (simpleFilter.isActive()) {
					output.append("[" + filterGroup.getText() + ";" + simpleFilter.getText() + "]");
				}
			}
		}

		for (SimpleFilter simpleFilter : simpleFilter) {
			output.append("[;" + simpleFilter.getText() + "]");
		}

		return output.toString();
	}

	public void exportVisibleFilters() {
		try {
			final JFileChooser fileChooser = new JFileChooser();
			final int returnValue = fileChooser.showDialog(null, "Save Filters"); // todo use language resource
			File file = fileChooser.getSelectedFile();
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(visibleFiltersToString());
				System.out.println("Wrote: " + visibleFiltersToString());
			}
		} catch (IOException e) {
			System.out.println("Failed to write filters to file!");
		}
	}

	public void stringToFilters(String filter) {
		Pattern pattern = Pattern.compile("(\\[\\w*;\\w+\\])+");
		Matcher matcher = pattern.matcher(filter);
		while (matcher.find()) {
			String[] filterInfo = matcher.group().split(";");
			FilterGroup targetFilterGroup = getFilterGroupByName(filterInfo[0]);
			if (targetFilterGroup != null) {
				SimpleFilter newSimpleFilter = new SimpleFilter(getUniqueID(), filterInfo[1]);
				addSimpleFilterToGroup(targetFilterGroup, newSimpleFilter);
			} else if (!filterInfo[0].equals("")){
				addNewFilterGroup(filterInfo[0]);
			}
		}
	}

	public void importFilters() {
		try {
			final JFileChooser fileChooser = new JFileChooser();
			final int returnValue = fileChooser.showDialog(null, "Import Filters"); // todo use language resource
			File file = fileChooser.getSelectedFile();
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				String line = bufferedReader.readLine();
				StringBuilder stringBuilder = new StringBuilder();
				while (line != null) {
					stringBuilder.append(line).append("\n");
					line = bufferedReader.readLine();
				}
				stringToFilters(stringBuilder.toString());
			}
		} catch (IOException e) {}  // todo handle possible exception
	}

	private FilterGroup getFilterGroupByName(String name) {
		return filterGroups.stream()
				.filter(filterGroup -> filterGroup.getText().equals(name)).findFirst().orElse(null);
	}
}
