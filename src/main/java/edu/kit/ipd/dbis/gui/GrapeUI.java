/**
  Created by Robin Link
*/

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.controller.*;
import edu.kit.ipd.dbis.gui.filter.FilterUI;
import edu.kit.ipd.dbis.gui.grapheditor.GraphEditorUI;
import edu.kit.ipd.dbis.gui.themes.Theme;
import edu.kit.ipd.dbis.log.Log;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class GrapeUI {

	private final CalculationController calculationController;
	private final CorrelationController correlationController;
	private final DatabaseController databaseController;
	private final FilterController filterController;
	private final GenerateController generateController;
	private final GraphEditorController graphEditorController;
	private final NonEditableTableModel tableModel;

	private GraphEditorUI graphEditorUI;
	private MenuUI menuUI;
	private FilterUI filterUI;
	private CorrelationUI correlationUI;
	private JTable tableUI;
	private StatusbarUI statusbarUI;
	private LogUI logUI;

	private String programName = "Grape";
	private JFrame mainWindow;

	private float verticalSplitRatio = .1f;

	public GrapeUI(CalculationController calculationController,
	               CorrelationController correlationController,
	               DatabaseController databaseController,
	               FilterController filterController,
	               GenerateController generateController,
	               GraphEditorController graphEditorController,
	               StatusbarController statusbarController,
	               ResourceBundle language,
	               Theme theme) {
		this.calculationController = calculationController;
		this.correlationController = correlationController;
		this.databaseController = databaseController;
		this.filterController = filterController;
		this.generateController = generateController;
		this.graphEditorController = graphEditorController;

		mainWindow = new JFrame(programName);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setMinimumSize(new Dimension(400, 400));
		mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);

		try {
			Image logo = ImageIO.read(getClass().getResource("/icons/GrapeLogo.png"));
			mainWindow.setIconImage(logo);
		} catch (IOException e) {}


		menuUI = new MenuUI(
				generateController, databaseController, statusbarController, graphEditorController, language, theme);
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
		graphEditorController.setGraphEditor(graphEditorUI);

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

		statusbarUI = new StatusbarUI(statusbarController, language, theme);
		JPanel rightUI = new JPanel(new BorderLayout());
		rightUI.setBackground(theme.backgroundColor);
		tableModel = new NonEditableTableModel(new String[0], new Object[0][0]);
		NonEditableTableModel tableModel = new NonEditableTableModel(new String[0], new Object[0][0]);
		calculationController.setTableModel(tableModel);
		databaseController.setTableModel(tableModel);
		filterController.setTableModel(tableModel);
		graphEditorController.setTableModel(tableModel);
		generateController.setTableModel(tableModel);
		tableUI = new JTable(tableModel);
		tableUI.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableUI.getSelectionModel().addListSelectionListener(new TableSelectionChangeAction());
		tableUI.addKeyListener(new DeleteGraphAction());
		JScrollPane scrollPane = new JScrollPane(tableUI);
		tableUI.setFillsViewportHeight(true);
		tableUI.setBackground(theme.backgroundColor);
		tableUI.setAutoCreateRowSorter(true); // todo use own row-sorter if necessary
		tableUI.setSelectionBackground(theme.lightNeutralColor);
		tableUI.setSelectionForeground(theme.foregroundColor);
		rightUI.add(scrollPane, BorderLayout.CENTER);
		rightUI.add(Box.createVerticalGlue(), BorderLayout.SOUTH);
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

	private class TableSelectionChangeAction implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent listSelectionEvent) {
			tableModel.fireTableDataChanged();
			tableModel.fireTableStructureChanged();
			int id = (Integer) tableUI.getValueAt(tableUI.getSelectedRow(), 0);
			PropertyGraph<Integer, Integer> graph = graphEditorController.getGraphById(id);
			graphEditorUI.displayGraph(graph);
		}
	}

	private class DeleteGraphAction implements KeyListener { // todo implement delete graphs
		@Override
		public void keyTyped(KeyEvent keyEvent) {
			if (keyEvent.getKeyChar() == KeyEvent.VK_DELETE) {
				if (tableUI.getSelectedRow() >= 0) {
					generateController.delGraph((int) tableModel.getValueAt(tableUI.getSelectedRow(), 0));
				}
			}
		}

		@Override
		public void keyPressed(KeyEvent keyEvent) {
		}

		@Override
		public void keyReleased(KeyEvent keyEvent) {
		}
	}
}
