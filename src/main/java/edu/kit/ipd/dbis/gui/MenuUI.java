package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.controller.DatabaseController;
import edu.kit.ipd.dbis.controller.GenerateController;
import edu.kit.ipd.dbis.controller.GraphEditorController;
import edu.kit.ipd.dbis.controller.StatusbarController;
import edu.kit.ipd.dbis.gui.popups.AboutUI;
import edu.kit.ipd.dbis.gui.popups.ConfigureDatabaseUI;
import edu.kit.ipd.dbis.gui.popups.ConfigureDatabaseOfSelectionUI;
import edu.kit.ipd.dbis.gui.popups.GenerateGraphUI;
import edu.kit.ipd.dbis.gui.popups.ReadBFSCodeUI;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Grape's menu
 */
public class MenuUI extends JMenuBar {

	private final StatusbarController statusbarController;
	private final Theme theme;
	private final JTable tableUI;

	/**
	 * @param generateController the controller responsible for generating graphs
	 * @param databaseController the controller responsible for database management
	 * @param statusbarController the controller responsible for updating the statusbar
	 * @param graphEditorController the controller responsible for the graph editor
	 * @param language the language used
	 * @param theme the theme to style the menu
	 */
	public MenuUI (JTable tableUI, GenerateController generateController,
				   DatabaseController databaseController,
				   StatusbarController statusbarController,
				   GraphEditorController graphEditorController,
				   ResourceBundle language,
				   Theme theme) {

		this.tableUI = tableUI;
		this.statusbarController = statusbarController;
		this.theme = theme;

		JMenu file = new JMenu(language.getString("file"));
		JMenuItem newDatabase = new JMenuItem(language.getString("newDatabase"));
		newDatabase.addActionListener(new NewDatabaseAction(databaseController, language, theme));
		JMenuItem openDatabase = new JMenuItem(language.getString("openDatabase"));
		openDatabase.addActionListener(new OpenDatabaseAction(databaseController, language, theme));
		JMenuItem saveDatabase = new JMenuItem(language.getString("saveDatabase"));
		saveDatabase.addActionListener(new SaveAction(databaseController, language, theme));
		JMenuItem saveSelection = new JMenuItem(language.getString("saveSelection"));
		saveSelection.addActionListener(new SaveSelectionAction(databaseController, language, theme));
		JMenuItem importDatabase = new JMenuItem(language.getString("importDatabase"));
		importDatabase.addActionListener(new ImportDatabaseAction(databaseController, language, theme));
		JMenuItem saveDatabaseAs = new JMenuItem(language.getString("saveDatabaseAs"));
		saveDatabaseAs.addActionListener(new SaveAsAction(databaseController, language, theme));
		file.add(newDatabase);
		file.add(openDatabase);
		file.add(importDatabase);
		file.add(saveDatabase);
		file.add(saveDatabaseAs);
		file.add(saveSelection);
		this.add(file);

		JMenu edit = new JMenu(language.getString("edit"));
		JMenuItem generateGraphs = new JMenuItem(language.getString("generateGraphs"));
		generateGraphs.addActionListener(new GenerateGraphAction(generateController, language, theme));
		JMenuItem emptyGraph = new JMenuItem(language.getString("emptyGraph"));
		emptyGraph.addActionListener(new CreateEmptyGraphAction(graphEditorController));
		JMenuItem readBFSCode = new JMenuItem(language.getString("readBFSCode"));
		readBFSCode.addActionListener(new ReadBFSCodeAction(generateController, language, theme));
		JMenuItem undo = new JMenuItem(language.getString("undo"));
		undo.addActionListener(new UndoAction());
		JMenuItem redo = new JMenuItem(language.getString("redo"));
		redo.addActionListener(new RedoAction());
		edit.add(generateGraphs);
		edit.add(emptyGraph);
		edit.add(readBFSCode);
		edit.addSeparator();
		edit.add(undo);
		edit.add(redo);
		this.add(edit);

		JMenu help = new JMenu(language.getString("help"));
		JMenuItem info = new JMenuItem(language.getString("about"));
		info.addActionListener(new ShowAboutAction());
		JMenuItem documentation = new JMenuItem(language.getString("documentation"));
		help.add(info);
		help.add(documentation);
		this.add(help);

		this.setBackground(theme.backgroundColor);
		this.setForeground(theme.foregroundColor);
		this.setFont(theme.defaultFont);

	}

	private static class NewDatabaseAction implements ActionListener {

		private final DatabaseController databaseController;
		private final ResourceBundle language;
		private final Theme theme;

		NewDatabaseAction(DatabaseController databaseController, ResourceBundle language, Theme theme) {
			this.databaseController = databaseController;
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			JFrame configureDatabaseUI = new ConfigureDatabaseUI(databaseController, language, theme);
			configureDatabaseUI.setVisible(true);
			}
	}

	private static class GenerateGraphAction implements ActionListener {

		private final GenerateController generateController;
		private final ResourceBundle language;
		private final Theme theme;

		GenerateGraphAction(GenerateController generateController, ResourceBundle language, Theme theme) {
			this.generateController = generateController;
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			JFrame generateGraphUI = new GenerateGraphUI(generateController, language, theme);
			generateGraphUI.setVisible(true);
		}
	}

	private class ReadBFSCodeAction implements ActionListener {

		private final GenerateController generateController;
		private final ResourceBundle language;
		private final Theme theme;

		ReadBFSCodeAction(GenerateController generateController, ResourceBundle language, Theme theme) {
			this.generateController = generateController;
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			JFrame readBFSCodeUI = new ReadBFSCodeUI(generateController, language, theme);
			readBFSCodeUI.setVisible(true);
		}
	}

	private class OpenDatabaseAction implements ActionListener {

		private final DatabaseController databaseController;
		private final ResourceBundle language;
		private final Theme theme;

		OpenDatabaseAction(DatabaseController databaseController, ResourceBundle language, Theme theme) {
			this.databaseController = databaseController;
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			final JFileChooser fileChooser = new JFileChooser();
			final int returnValue = fileChooser.showDialog(null, "Open"); // todo replace with language resource
			File file = fileChooser.getSelectedFile();

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				System.out.println("Open a database!"); // todo remove sout
				if (file != null) {
					file = new File(file.getParentFile(), file.getName());
					try {
						databaseController.loadDatabase(file.getPath());
					} catch (Exception e) { // todo replace with correct exception as soon as available
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("Creating file failed"); // todo remove sout
			}
		}
	}

	private class ImportDatabaseAction implements ActionListener {

		private final DatabaseController databaseController;
		private final ResourceBundle language;
		private final Theme theme;

		ImportDatabaseAction(DatabaseController databaseController, ResourceBundle language, Theme theme) {
			this.databaseController = databaseController;
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			final JFileChooser fileChooser = new JFileChooser();
			final int returnValue = fileChooser.showDialog(null, "Import"); // todo replace with language resource
			File file = fileChooser.getSelectedFile();

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				System.out.println("Import a database!"); // todo remove sout
				if (file != null) {
					file = new File(file.getParentFile(), file.getName());
					try {
						databaseController.mergeDatabase(file.getPath());
					} catch (Exception e) { // todo replace with correct exception as soon as available
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("Importing database failed"); // todo remove sout
			}
		}
	}

	private class SaveAction implements ActionListener {

		private final DatabaseController databaseController;
		private final ResourceBundle language;
		private final Theme theme;

		SaveAction(DatabaseController databaseController, ResourceBundle language, Theme theme) {
			this.databaseController = databaseController;
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			final JFileChooser fileChooser = new JFileChooser();
			final int returnValue = fileChooser.showDialog(null, "Save"); // todo replace with language resource
			File file = fileChooser.getSelectedFile();

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				System.out.println("Saved a database!"); // todo remove sout
				if (file != null) {
					file = new File(file.getParentFile(), file.getName() + ".txt");
					try {
						databaseController.saveDatabase(file.getPath());
					} catch (Exception e) { // todo replace with correct exception as soon as available
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("Saving database failed"); // todo remove sout
			}
		}
	}

	private class SaveAsAction implements ActionListener { // todo same as Save

		private final DatabaseController databaseController;
		private final ResourceBundle language;
		private final Theme theme;

		public SaveAsAction(DatabaseController databaseController, ResourceBundle language, Theme theme) {
			this.databaseController = databaseController;
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			final JFileChooser fileChooser = new JFileChooser();
			final int returnValue = fileChooser.showDialog(null, "Save"); // todo replace with language resource
			File file = fileChooser.getSelectedFile();

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				System.out.println("Saved a database under new name!"); // todo remove sout
				if (file != null) {
					file = new File(file.getParentFile(), file.getName() + ".txt");
					try {
						databaseController.saveDatabase(file.getPath());
					} catch (Exception e) { // todo replace with correct exception as soon as available
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("Saving database under a new name failed"); // todo remove sout
			}
		}
	}

	private class SaveSelectionAction implements ActionListener {

		private final DatabaseController databaseController;
		private final ResourceBundle language;
		private final Theme theme;

		SaveSelectionAction(DatabaseController databaseController, ResourceBundle language, Theme theme) {
			this.databaseController = databaseController;
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			final JFileChooser fileChooser = new JFileChooser();
			final int returnValue = fileChooser.showDialog(null, "Save"); // todo replace with language resource
			File file = fileChooser.getSelectedFile();

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				System.out.println("Saved selection to a database!"); // todo remove sout
				if (file != null) {
					file = new File(file.getParentFile(), file.getName() + ".txt");
					//Inserting selected graphIds
					LinkedList<Integer> graphs = new LinkedList<>();
					int[] selectedRows = tableUI.getSelectedRows();
					if (selectedRows.length == 1) {
						graphs.add((Integer) tableUI.getValueAt(selectedRows[0], 0));
					}
					else {
						for (int row : selectedRows) {
							graphs.add((Integer) tableUI.getValueAt(selectedRows[row], 0));
						}
					}
					//Open Database configuration window
					JFrame configureDatabaseOfSelectionUI = new ConfigureDatabaseOfSelectionUI(databaseController, language, theme, file.getPath(), graphs);
					configureDatabaseOfSelectionUI.setVisible(true);
				}
			} else {
				System.out.println("Saving selection as database failed"); // todo remove sout
			}
		}
	}

	private class ShowAboutAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			JFrame aboutUI = new AboutUI(theme);
			aboutUI.setVisible(true);
		}
	}

	private class UndoAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			statusbarController.undo();
		}
	}

	private class RedoAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			statusbarController.redo();
		}
	}

	private class CreateEmptyGraphAction implements ActionListener {
		private final GraphEditorController graphEditorController;

		CreateEmptyGraphAction(GraphEditorController graphEditorController) {
			this.graphEditorController = graphEditorController;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			graphEditorController.emptyGraphToGraphEditor();
		}
	}
}