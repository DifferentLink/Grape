/**
  Created by Robin Link
*/

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.controller.*;
import edu.kit.ipd.dbis.gui.filter.FilterUI;
import edu.kit.ipd.dbis.gui.grapheditor.GraphEditorUI;
import edu.kit.ipd.dbis.gui.themes.Theme;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyFactory;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GrapeUI {

	private final CalculationController calculationController;
	private final CorrelationController correlationController;
	private final DatabaseController databaseController;
	private final FilterController filterController;
	private final GenerateController generateController;
	private final GraphEditorController graphEditorController;
	private final StatusbarController statusbarController;

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

	private Theme theme;

	private String lastSortedColumn = "";
	private boolean isSortedAscending = true;
	private int[] columnWidths = new int[(new PropertyGraph<>()).getProperties().size()];

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
		this.statusbarController = statusbarController;
		this.theme = theme;

		this.calculationController.setGrapeUI(this);
		this.databaseController.setGrapeUI(this);
		this.filterController.setGrapeUI(this);
		this.generateController.setGrapeUI(this);
		this.graphEditorController.setGrapeUI(this);
		this.statusbarController.setGrapeUI(this);

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

		statusbarUI = new StatusbarUI(statusbarController, databaseController, language, theme);
		JPanel rightUI = new JPanel(new BorderLayout());
		rightUI.setBackground(theme.backgroundColor);
		tableModel = new NonEditableTableModel(new String[0], new Object[0][0]);
		tableUI = new JTable(tableModel);
		tableUI.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tableUI.getSelectionModel().addListSelectionListener(new TableSelectionChangeAction());
		tableUI.getTableHeader().setReorderingAllowed(false);
		tableUI.addKeyListener(new DeleteGraphAction());
		JScrollPane scrollPane = new JScrollPane(tableUI,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableUI.setFillsViewportHeight(true);
		tableUI.setBackground(theme.backgroundColor);
		tableUI.getTableHeader().addMouseListener(new TableHeaderAction());
		tableUI.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
			try {
				final int selectedRow = tableUI.getSelectedRow();
				int id = (Integer) tableUI.getValueAt(selectedRow, 0);
				PropertyGraph<Integer, Integer> graph = graphEditorController.getGraphById(id);
				graphEditorUI.displayGraph(graph);
				statusbarUI.changeSelectedRow(tableUI.getSelectedRow());
			} catch (IndexOutOfBoundsException ignored) {}
		}
	}

	private class DeleteGraphAction implements KeyListener { // todo implement delete graphs
		@Override
		public void keyTyped(KeyEvent keyEvent) {
			if (keyEvent.getKeyChar() == KeyEvent.VK_DELETE) {
				try {
					generateController.delGraph((int) tableUI.getValueAt(tableUI.getSelectedRow(), 0));
					tableModel.update(filterController.getFilteredAndSortedGraphs());
				} catch (IndexOutOfBoundsException | SQLException ignored) {}
			}
		}

		@Override
		public void keyPressed(KeyEvent keyEvent) {
		}

		@Override
		public void keyReleased(KeyEvent keyEvent) {
		}
	}

	private class TableHeaderAction implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent mouseEvent) {

		}

		@Override
		public void mousePressed(MouseEvent mouseEvent) {

		}

		@Override
		public void mouseReleased(MouseEvent mouseEvent) {
			final int column = tableUI.columnAtPoint(mouseEvent.getPoint());
			if (columnWidths[column] == tableUI.getColumnModel().getColumn(column).getWidth()) {
				final String columnName = tableUI.getColumnName(tableUI.columnAtPoint(mouseEvent.getPoint()));
				isSortedAscending = !columnName.equals(lastSortedColumn) || !isSortedAscending;
				lastSortedColumn = columnName;
				updateTable();
			} else {
				columnWidths[column] = tableUI.getColumnModel().getColumn(column).getWidth();
			}
		}

		@Override
		public void mouseEntered(MouseEvent mouseEvent) {

		}

		@Override
		public void mouseExited(MouseEvent mouseEvent) {

		}
	}

	public void updateTable() {
		boolean isSorted = false;
		for (Property property : (new PropertyGraph<>()).getProperties()) {
			if ((property.getClass().getSimpleName().toLowerCase()).equals(lastSortedColumn)) {
				try {
					if (isSortedAscending) {
						tableModel.update(filterController.getFilteredAndAscendingSortedGraphs(property));
					} else {
						tableModel.update(filterController.getFilteredAndDescendingSortedGraphs(property));
					}
					isSorted = true;
				} catch (SQLException ignored) {}
			}
		}
		if (!isSorted) {
			try {
				tableModel.update(filterController.getFilteredAndSortedGraphs());
			} catch (SQLException ignored) {}
		}

		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext fontRenderer = new FontRenderContext(affinetransform,true,true);
		Font font = theme.defaultFont;
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		for (int i = 0; i < tableUI.getColumnModel().getColumnCount(); i++) {
			final TableColumn column = tableUI.getColumnModel().getColumn(i);
			if (columnWidths[i] > 0) {
				column.setPreferredWidth(columnWidths[i]);
			} else {
				final int optimalWidth =
						(int) (font.getStringBounds("  " + tableUI.getColumnName(i) + "  ", fontRenderer).getWidth());
				column.setPreferredWidth(optimalWidth);
				columnWidths[i] = optimalWidth;
			}
			if (i != 1) {
				column.setCellRenderer(centerRenderer);
			}
		}
	}
}
