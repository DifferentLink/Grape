/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.util.ResourceBundle;

public class MenuUI {
	public static JMenuBar makeMenuBar(ResourceBundle language) {
		JMenuBar menuBar = new JMenuBar();

		JMenu file = new JMenu(language.getString("file"));
		JMenuItem newDatabase = new JMenuItem(language.getString("newDatabase"));
		JMenuItem openDatabase = new JMenuItem(language.getString("openDatabase"));
		JMenuItem saveDatabase = new JMenuItem(language.getString("saveDatabase"));
		JMenuItem saveSelection = new JMenuItem(language.getString("saveSelection"));
		JMenuItem importDatabase = new JMenuItem(language.getString("importDatabase"));
		JMenuItem saveDatabaseAs = new JMenuItem(language.getString("saveDatabaseAs"));
		file.add(newDatabase);
		file.add(openDatabase);
		file.add(importDatabase);
		file.add(saveDatabase);
		file.add(saveDatabaseAs);
		file.add(saveSelection);
		menuBar.add(file);

		JMenu edit = new JMenu(language.getString("edit"));
		JMenuItem generateGraphs = new JMenuItem(language.getString("generateGraph"));
		JMenuItem emptyGraph = new JMenuItem(language.getString("emptyGraph"));
		JMenuItem readBFSCode = new JMenuItem(language.getString("readBFSCode"));
		JMenuItem Undo = new JMenuItem(language.getString("undo"));
		JMenuItem Redo = new JMenuItem(language.getString("redo"));
		edit.add(generateGraphs);
		edit.add(emptyGraph);
		edit.add(readBFSCode);
		edit.addSeparator();
		edit.add(Undo);
		edit.add(Redo);
		menuBar.add(edit);

		JMenu help = new JMenu(language.getString("help"));
		JMenuItem info = new JMenuItem(language.getString("info"));
		JMenuItem documentation = new JMenuItem(language.getString("documentation"));
		help.add(info);
		help.add(documentation);
		menuBar.add(help);

		return menuBar;
	}
}
