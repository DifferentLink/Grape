package edu.kit.ipd.dbis.gui.filter;

import javax.swing.JFileChooser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Maintains two lists that together contain all filters. Manages associations between FilterGroups and their
 * SimpleFilters. Ensures that only unique IDs are used.
 */
public class UIFilterManager {

	private List<SimpleFilter> simpleFilter = new ArrayList<>();
	private List<FilterGroup> filterGroups = new ArrayList<>();
	private int nextUniqueID = 0;

	/**
	 * Adds a new SimpleFilter to the known Filters.
	 */
	public void addNewSimpleFilter() {
		SimpleFilter newFilter = new SimpleFilter(getUniqueID());
		simpleFilter.add(newFilter);
	}

	/**
	 * Associates an existing SimpleFilter to a FilterGroup.
	 * @param filterGroup the associated FilterGroup
	 * @param simpleFilter the associated SimpleFilter
	 */
	private void addSimpleFilterToGroup(FilterGroup filterGroup, SimpleFilter simpleFilter) {
		filterGroup.add(simpleFilter);
	}

	/**
	 * Associates a new SimpleFilter to a FilterGroup.
	 * @param filterGroup the associated FilterGroup
	 */
	public void addNewSimpleFilterToGroup(FilterGroup filterGroup) {
		filterGroup.add(new SimpleFilter(getUniqueID()));
	}

	/**
	 * Adds a new FilterGroup to the known Filters.
	 * @param name the name of the FilterGroup
	 */
	public void addNewFilterGroup(String name) {
		FilterGroup newGroup = new FilterGroup(getUniqueID(), name);
		filterGroups.add(newGroup);
	}

	/**
	 * @return the unique ID
	 */
	public int getUniqueID() {
		return nextUniqueID++;
	}

	/**
	 * @return all known SimpleFilters
	 */
	public List<SimpleFilter> getSimpleFilter() {
		return simpleFilter;
	}

	/**
	 * @return all known FilterGroups
	 */
	public List<FilterGroup> getFilterGroups() {
		return filterGroups;
	}

	/**
	 * Uses an ID to remove a Filter
	 * @param id the ID of the filter to remove
	 */
	public void remove(int id) {
		simpleFilter.removeIf(simpleFilter -> simpleFilter.getID() == id);
		filterGroups.removeIf(filterGroup -> filterGroup.getID() == id);
		filterGroups.forEach(filterGroup -> {
			filterGroup.getSimpleFilter().removeIf(simpleFilter1 -> simpleFilter1.getID() == id);
		});
	}

	/**
	 * Writes all known Filters to a String with the format [FilterGroupName;SimpleFilterText]+.
	 * Where FilterGroupName is an empty string if a simple filter is not associated with any FilterGroup.
	 * @return a string representation of the visible Filters
	 */
	public String visibleFiltersToString() {

		StringBuilder output = new StringBuilder();

		for (FilterGroup filterGroup : filterGroups) {
			for (SimpleFilter simpleFilter : filterGroup.getSimpleFilter()) {
				if (simpleFilter.isActive()) {
					output.append("[" + filterGroup.getText() + ";" + simpleFilter.getText() + "]:");
				}
			}
		}

		for (SimpleFilter simpleFilter : simpleFilter) {
			output.append("[;" + simpleFilter.getText() + "]:");
		}

		return output.toString();
	}

	/**
	 * Opens a file chooser and writes the visible Filters to a text file.
	 */
	public void exportVisibleFilters() {
		try {
			final JFileChooser fileChooser = new JFileChooser();
			final int returnValue = fileChooser.showDialog(null, "Save Filters"); // todo use language resource
			File file = fileChooser.getSelectedFile();
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				Files.write(Paths.get(file.getPath()), visibleFiltersToString().getBytes());
			}
		} catch (IOException e) {
			System.out.println("Failed to write filters to file!");
		}
	}

	/**
	 * Parses a string input to Filters. The excepted format is [FilterGroupName;SimpleFilterText]+.
	 * @param filter the string with the filters
	 */
	public void stringToFilters(String filter) {
		Pattern pattern = Pattern.compile("(\\[(.)*;(.)+\\]:)+");
		Matcher matcher = pattern.matcher(filter);
		if (matcher.find()) {
			String[] groups = matcher.group().split(":");
			for (int i = 0; i < groups.length; i++) {
				String[] filterInfo = groups[i].split(";");
				String group = filterInfo[0].substring(1);
				String content = filterInfo[1].substring(0, filterInfo[1].length() - 1);

				FilterGroup targetFilterGroup = getFilterGroupByName(group);
				if (targetFilterGroup != null) {
					SimpleFilter newSimpleFilter = new SimpleFilter(getUniqueID(), content);
					addSimpleFilterToGroup(targetFilterGroup, newSimpleFilter);
				} else if (!group.equals("")){
					addNewFilterGroup(group);
				}
			}
		}
	}

	/**
	 * Opens a file chooser to open a text file and tries to parse it's content to Filters
	 */
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
		} catch (IOException e) { }  // todo handle possible exception
	}

	private FilterGroup getFilterGroupByName(String name) {
		return filterGroups.stream()
				.filter(filterGroup -> filterGroup.getText().equals(name)).findFirst().orElse(null);
	}

	public void clearFilters() {
		simpleFilter = new ArrayList<>();
		filterGroups = new ArrayList<>();
		nextUniqueID = 0;
	}
}
