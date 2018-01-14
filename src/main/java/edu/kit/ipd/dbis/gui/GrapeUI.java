/**
  Created by Robin Link
*/

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.filter.FilterUI;
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

	private String programName = "Grape";
	private JFrame mainWindow;

	private float verticalSplitRatio = .22f;

	public GrapeUI(Controller controller, ResourceBundle language, Theme theme) {
		super(controller, language, theme);

		mainWindow = new JFrame(programName);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setMinimumSize(new Dimension(400, 400));
		mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);

		menuUI = new MenuUI(language, theme);
		mainWindow.setJMenuBar(menuUI);

		filterUI = new FilterUI(controller, language, theme);
		correlationUI = new CorrelationUI(controller, language, theme);

		JPanel filterCorrelationDivider = new JPanel();
		filterCorrelationDivider.setLayout(new BoxLayout(filterCorrelationDivider, BoxLayout.Y_AXIS));
		filterCorrelationDivider.add(filterUI);
		filterCorrelationDivider.add(Box.createVerticalGlue());
		filterCorrelationDivider.add(correlationUI);

		graphEditorUI = new GraphEditorUI(controller, language, theme);

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

		statusbarUI = new StatusbarUI(controller, language, theme);
		JPanel rightUI = new JPanel();
		rightUI.setLayout(new BoxLayout(rightUI, BoxLayout.Y_AXIS));
		tableUI = new TableUI(controller, language, theme, new Dimension(1000, 1080));
		rightUI.add(tableUI, BorderLayout.NORTH);
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

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {
		updateGraphEditor();
		updateFilter();
		updateCorrelation();
		updateTable();
		updateStatusbar();
		updateLog();
	}

	public void updateGraphEditor() {
		graphEditorUI.update();
	}

	public void updateFilter() {
		filterUI.update();
	}

	public void updateCorrelation() {
		correlationUI.update();
	}

	public void updateTable() {
		tableUI.update();
	}

	public void updateStatusbar() {
		statusbarUI.update();
	}

	public void updateLog() {
		logUI.update();
	}
}
