/**
  Created by Robin Link
*/

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.grapheditor.GraphEditorUI;

import javax.swing.*;
import java.util.ResourceBundle;

public class GrapeUI extends JFrame {

	private final String programName = "Grape";
	private ResourceBundle language;

	public GrapeUI(final ResourceBundle language) {
		this.language = language;
		run();
	}

	private void run() {
		JFrame mainWindow = new JFrame(programName);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel filterUI = FilterUI.makeFilterUI(language);
		JPanel correlationUI = CorrelationUI.makeCorrelationUI(language);
		JPanel filterAndCorrelationUI = new JPanel();
		filterAndCorrelationUI.setLayout(new BoxLayout(filterAndCorrelationUI, BoxLayout.Y_AXIS));
		JSplitPane filterCorrelationDivider = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				filterUI,
				correlationUI);
		filterAndCorrelationUI.add(filterCorrelationDivider);

		JPanel graphEditor = GraphEditorUI.makeGraphEditorUI(language);
		JPanel leftUI = new JPanel();
		JSplitPane graphEditorDivider = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				filterCorrelationDivider,
				graphEditor);
		leftUI.add(graphEditorDivider);

		JPanel tableUI = TableUI.makeTableUI(language);
		JPanel statusbarUI = StatusbarUI.makeStatusbarUI(language);
		JPanel rightUI = new JPanel();
		rightUI.setLayout(new BoxLayout(rightUI, BoxLayout.Y_AXIS));
		rightUI.add(tableUI);
		rightUI.add(statusbarUI);

		JSplitPane verticalDivider = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				leftUI,
				rightUI);

		verticalDivider.setDividerLocation(150); // todo make relativ
		mainWindow.getContentPane().add(verticalDivider);

		mainWindow.setJMenuBar(MenuUI.makeMenuBar(language));

		mainWindow.pack();
		mainWindow.setVisible(true);
	}
}
