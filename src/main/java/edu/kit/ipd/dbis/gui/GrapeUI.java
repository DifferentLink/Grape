/**
  Created by Robin Link
*/

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.controller.*;
import edu.kit.ipd.dbis.gui.filter.FilterUI;
import edu.kit.ipd.dbis.gui.grapheditor.GraphEditorUI;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ResourceBundle;

public class GrapeUI {

	private GraphEditorUI graphEditorUI;
	private MenuUI menuUI;
	private FilterUI filterUI;
	private CorrelationUI correlationUI;
	private JTable tableUI;
	private StatusbarUI statusbarUI;
	private GUIElement logUI;

	private String programName = "Grape";
	private JFrame mainWindow;

	private float verticalSplitRatio = .1f;

	public GrapeUI(CalculationController calculationController,
	               CorrelationController correlationController,
	               DatabaseController databaseController,
	               FilterController filterController,
	               GenerateController generateController,
	               GraphEditorController graphEditorController,
	               ResourceBundle language,
	               Theme theme) {

		mainWindow = new JFrame(programName);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setMinimumSize(new Dimension(400, 400));
		mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);

		try {
			Image logo = ImageIO.read(getClass().getResource("/icons/GrapeLogo.png"));
			mainWindow.setIconImage(logo);
		} catch (IOException e) {}


		menuUI = new MenuUI(language, theme);
		mainWindow.setJMenuBar(menuUI);

		filterUI = new FilterUI(filterController, language, theme);
		correlationUI = new CorrelationUI(correlationController, language, theme);

		JPanel filterCorrelationDivider = new JPanel();
		filterCorrelationDivider.setLayout(new BoxLayout(filterCorrelationDivider, BoxLayout.Y_AXIS));
		filterCorrelationDivider.setBackground(theme.backgroundColor);
		filterCorrelationDivider.add(filterUI);
		filterCorrelationDivider.add(Box.createVerticalGlue());
		filterCorrelationDivider.add(correlationUI);

		graphEditorUI = new GraphEditorUI(graphEditorController, language, theme);

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

		statusbarUI = new StatusbarUI(language, theme);
		JPanel rightUI = new JPanel();
		rightUI.setLayout(new BoxLayout(rightUI, BoxLayout.Y_AXIS));
		rightUI.setBackground(theme.backgroundColor);
		String[] columns = {"ID", "#Vertices", "#Edges"}; // todo remove with dynamic columns/rows
		Object[][] data = {{"001", "5", "6"},
				{"002", "3", "5"},
				{"003", "2", "4"}};
		tableUI = new JTable(new NonEditableTableModel(columns, data));
		rightUI.add(tableUI, BorderLayout.CENTER);
		rightUI.add(Box.createVerticalGlue());
		rightUI.add(statusbarUI, BorderLayout.SOUTH);

		JSplitPane verticalDivider;
		verticalDivider = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				true,
				leftUI,
				rightUI);
		verticalDivider.setDividerSize(theme.dividerSize);
		verticalDivider.setResizeWeight(verticalSplitRatio);

		mainWindow.add(verticalDivider);

		mainWindow.setVisible(true);
	}
}
