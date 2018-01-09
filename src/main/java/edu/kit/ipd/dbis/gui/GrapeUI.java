/**
  Created by Robin Link
*/

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.grapheditor.GraphEditorUI;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class GrapeUI extends GUIWindow {

	private GUIElement graphEditorUI;
	private MenuUI menuUI;
	private GUIElement filterUI;
	private GUIElement correlationUI;
	private GUIElement tableUI;
	private GUIElement statusbarUI;
	private GUIElement logUI;

	String programName = "Grape";
	JFrame mainWindow;

	public GrapeUI(ResourceBundle language, Theme theme) {
		super.language = language;
		super.theme = theme;
		build();
	}

	private void build() {
		mainWindow = new JFrame(programName);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setMinimumSize(new Dimension(600, 400));
		mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);

		menuUI = new MenuUI(language, theme);
		mainWindow.setJMenuBar(menuUI);

		filterUI = new FilterUI(language, theme);
		correlationUI = new CorrelationUI(language, theme);

		JSplitPane filterCorrelationDivider = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				true,
				filterUI,
				correlationUI);
		filterCorrelationDivider.setDividerSize(theme.dividerSize);
		filterCorrelationDivider.setBorder(null);

		graphEditorUI = new GraphEditorUI(language, theme);

		JSplitPane graphEditorDivider = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				true,
				filterCorrelationDivider,
				graphEditorUI);
		graphEditorDivider.setDividerSize(theme.dividerSize);
		graphEditorDivider.setBorder(null);

		JPanel leftUI = new JPanel();
		leftUI.setLayout(new BoxLayout(leftUI, BoxLayout.Y_AXIS));
		leftUI.setBackground(theme.backgroundColor);
		leftUI.setForeground(theme.foregroundColor);
		leftUI.add(graphEditorDivider);

		graphEditorDivider.setResizeWeight(.55f);
		filterCorrelationDivider.setResizeWeight(.5f);

		tableUI = new TableUI(language, theme);
		statusbarUI = new StatusbarUI(language, theme);
		JPanel rightUI;
		rightUI = new JPanel();

		rightUI.setLayout(new BoxLayout(rightUI, BoxLayout.Y_AXIS));
		rightUI.add(tableUI, BorderLayout.NORTH);
		rightUI.add(statusbarUI, BorderLayout.SOUTH);

		JSplitPane verticalDivider;
		verticalDivider = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				true,
				leftUI,
				rightUI);
		verticalDivider.setDividerSize(theme.dividerSize);
		verticalDivider.setResizeWeight(.2f);

		mainWindow.add(verticalDivider);

		mainWindow.setVisible(true);
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {
		updateGraphEditor();
		updateMenu();
		updateFilter();
		updateCorrelation();
		updateTable();
		updateStatusbar();
		updateLog();
	}

	public void updateGraphEditor() {

	}

	public void updateMenu() {

	}

	public void updateFilter() {

	}

	public void updateCorrelation() {

	}

	public void updateTable() {

	}

	public void updateStatusbar() {

	}

	public void updateLog() {

	}
}
