/**
  Created by Robin Link
*/

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.grapheditor.GraphEditorUI;
import edu.kit.ipd.dbis.gui.theme.LightTheme;
import edu.kit.ipd.dbis.gui.theme.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class GrapeUI extends JFrame {

	private final String programName = "Grape";
	private ResourceBundle language;
	private Theme theme = new LightTheme();

	public GrapeUI(final ResourceBundle language) {
		this.language = language;
		run();
	}

	private void run() {
		JFrame mainWindow = new JFrame(programName);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel filterUI = FilterUI.makeFilterUI(language, theme);

		JPanel correlationUI = CorrelationUI.makeCorrelationUI(language, theme);

		JPanel filterAndCorrelationUI = new JPanel();
		filterAndCorrelationUI.setLayout(new BoxLayout(filterAndCorrelationUI, BoxLayout.Y_AXIS));
		JSplitPane filterCorrelationDivider = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				true,
				filterUI,
				correlationUI);
		filterCorrelationDivider.setDividerSize(theme.dividerSize);
		filterCorrelationDivider.setContinuousLayout(true);
		filterAndCorrelationUI.add(filterCorrelationDivider);

		JPanel graphEditor = GraphEditorUI.makeGraphEditorUI(language, theme);
		JPanel leftUI = new JPanel();
		leftUI.setBackground(theme.backgroundColor);
		leftUI.setForeground(theme.foregroundColor);
		JSplitPane graphEditorDivider = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				true,
				filterCorrelationDivider,
				graphEditor);
		graphEditorDivider.setDividerSize(theme.dividerSize);
		graphEditorDivider.setContinuousLayout(true);
		leftUI.add(graphEditorDivider);

		JPanel tableUI = TableUI.makeTableUI(language, theme);
		JPanel statusbarUI = StatusbarUI.makeStatusbarUI(language, theme);
		JPanel rightUI = new JPanel();
		rightUI.setLayout(new BoxLayout(rightUI, BoxLayout.Y_AXIS));
		rightUI.add(tableUI);
		rightUI.add(statusbarUI);
		rightUI.setBackground(theme.backgroundColor);
		rightUI.setForeground(theme.foregroundColor);

		JSplitPane verticalDivider = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				true,
				leftUI,
				rightUI);
		verticalDivider.setDividerSize(theme.dividerSize);

		mainWindow.add(verticalDivider);

		mainWindow.setJMenuBar(MenuUI.makeMenuBar(language, theme));

		mainWindow.pack();
		mainWindow.setVisible(true);
	}
}
