/**
  Created by Robin Link
*/

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.grapheditor.GraphEditorUI;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class GrapeUI extends GUI {

	private GraphEditorUI graphEditorUI;
	private MenuUI menuUI;
	private FilterUI filterUI;
	private CorrelationUI correlationUI;
	private TableUI tableUI;
	private StatusbarUI statusbarUI;
	private LogUI logUI;

	public GrapeUI(final ResourceBundle language, Theme theme) {
		super.language = language;
		super.theme = theme;
		show();
	}

	private void show() {
		String programName = "Grape";
		JFrame mainWindow = new JFrame(programName);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setMinimumSize(new Dimension(600, 400));
		mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);

		mainWindow.setJMenuBar(MenuUI.makeMenuBar(language, theme));

		JPanel filterUI = FilterUI.makeFilterUI(language, theme);
		JPanel correlationUI = CorrelationUI.makeCorrelationUI(language, theme);

		JSplitPane filterCorrelationDivider = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				true,
				filterUI,
				correlationUI);
		filterCorrelationDivider.setDividerSize(theme.dividerSize);
		filterCorrelationDivider.setBorder(null);

		JPanel graphEditor = GraphEditorUI.makeGraphEditorUI(language, theme);

		JSplitPane graphEditorDivider = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				true,
				filterCorrelationDivider,
				graphEditor);
		graphEditorDivider.setDividerSize(theme.dividerSize);
		graphEditorDivider.setBorder(null);

		JPanel leftUI = new JPanel();
		leftUI.setLayout(new BoxLayout(leftUI, BoxLayout.Y_AXIS));
		leftUI.setBackground(theme.backgroundColor);
		leftUI.setForeground(theme.foregroundColor);
		leftUI.add(graphEditorDivider);

		graphEditorDivider.setResizeWeight(.55f);
		filterCorrelationDivider.setResizeWeight(.5f);

		JPanel tableUI = TableUI.makeTableUI(language, theme);
		JPanel statusbarUI = StatusbarUI.makeStatusbarUI(language, theme);
		JPanel rightUI = new JPanel();

		rightUI.setLayout(new BoxLayout(rightUI, BoxLayout.Y_AXIS));
		rightUI.add(tableUI, BorderLayout.NORTH);
		rightUI.add(statusbarUI, BorderLayout.SOUTH);

		JSplitPane verticalDivider = new JSplitPane(
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
	 * Updates the GUI element.
	 */
	@Override
	public void update() {

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
