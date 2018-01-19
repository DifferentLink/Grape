/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.popups.AboutUI;
import edu.kit.ipd.dbis.gui.popups.GenerateGraphUI;
import edu.kit.ipd.dbis.gui.popups.NewDatabaseUI;
import edu.kit.ipd.dbis.gui.popups.ReadBFSCodeUI;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class MenuUI extends GUIMenu {

	public MenuUI(ResourceBundle language, Theme theme) {
		super(language, theme);

		JMenu file = new JMenu(language.getString("file"));
		JMenuItem newDatabase = new JMenuItem(language.getString("newDatabase"));
		newDatabase.addActionListener(new NewDatabaseAction(language, theme));
		JMenuItem openDatabase = new JMenuItem(language.getString("openDatabase"));
		openDatabase.addActionListener(new OpenDatabaseAction(language, theme));
		JMenuItem saveDatabase = new JMenuItem(language.getString("saveDatabase"));
		saveDatabase.addActionListener(new SaveAction(language, theme));
		JMenuItem saveSelection = new JMenuItem(language.getString("saveSelection"));
		saveSelection.addActionListener(new SaveSelectionAction(language, theme));
		JMenuItem importDatabase = new JMenuItem(language.getString("importDatabase"));
		importDatabase.addActionListener(new ImportDatabaseAction(language, theme));
		JMenuItem saveDatabaseAs = new JMenuItem(language.getString("saveDatabaseAs"));
		saveDatabaseAs.addActionListener(new SaveAsAction(language, theme));
		file.add(newDatabase);
		file.add(openDatabase);
		file.add(importDatabase);
		file.add(saveDatabase);
		file.add(saveDatabaseAs);
		file.add(saveSelection);
		this.add(file);

		JMenu edit = new JMenu(language.getString("edit"));
		JMenuItem generateGraphs = new JMenuItem(language.getString("generateGraphs"));
		generateGraphs.addActionListener(new GenerateGraphAction(language, theme));
		JMenuItem emptyGraph = new JMenuItem(language.getString("emptyGraph"));
		JMenuItem readBFSCode = new JMenuItem(language.getString("readBFSCode"));
		readBFSCode.addActionListener(new ReadBFSCodeAction(language, theme));
		JMenuItem Undo = new JMenuItem(language.getString("undo"));
		JMenuItem Redo = new JMenuItem(language.getString("redo"));
		edit.add(generateGraphs);
		edit.add(emptyGraph);
		edit.add(readBFSCode);
		edit.addSeparator();
		edit.add(Undo);
		edit.add(Redo);
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

	private static class GenerateGraphAction implements ActionListener {

		private final ResourceBundle language;
		private final Theme theme;

		public GenerateGraphAction(ResourceBundle language, Theme theme) {
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			JFrame generateGraphUI = new GenerateGraphUI(language, theme);
			generateGraphUI.setVisible(true);
		}
	}

	private class ReadBFSCodeAction implements ActionListener {

		private final ResourceBundle language;
		private final Theme theme;

		public ReadBFSCodeAction(ResourceBundle language, Theme theme) {
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			JFrame readBFSCodeUI = new ReadBFSCodeUI(language, theme);
			readBFSCodeUI.setVisible(true);
		}
	}

	private class NewDatabaseAction implements ActionListener {

		private final ResourceBundle language;
		private final Theme theme;

		public NewDatabaseAction(ResourceBundle language, Theme theme) {
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			JFrame newDatabaseUI = new NewDatabaseUI(language, theme);
			newDatabaseUI.setVisible(true);
		}
	}

	private class OpenDatabaseAction implements ActionListener {

		private final ResourceBundle language;
		private final Theme theme;

		public OpenDatabaseAction(ResourceBundle language, Theme theme) {
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {

		}
	}

	private class ImportDatabaseAction implements ActionListener {

		private final ResourceBundle language;
		private final Theme theme;

		public ImportDatabaseAction(ResourceBundle language, Theme theme) {
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {

		}
	}

	private class SaveAction implements ActionListener {

		private final ResourceBundle language;
		private final Theme theme;

		public SaveAction(ResourceBundle language, Theme theme) {
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {

		}
	}

	private class SaveAsAction implements ActionListener {

		private final ResourceBundle language;
		private final Theme theme;

		public SaveAsAction(ResourceBundle language, Theme theme) {
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {

		}
	}

	private class SaveSelectionAction implements ActionListener {

		private final ResourceBundle language;
		private final Theme theme;

		public SaveSelectionAction(ResourceBundle language, Theme theme) {
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {

		}
	}

	private class ShowAboutAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			JFrame aboutUI = new AboutUI(theme);
			aboutUI.setVisible(true);
		}
	}
}
