/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.controller.GenerateController;
import edu.kit.ipd.dbis.controller.GraphEditorController;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class GraphOptions extends JPopupMenu {

	private final int id;
	private final GraphEditorController graphEditorController;
	private final GenerateController generateController;
	private final ResourceBundle language;
	private final Theme theme;

	public GraphOptions(final int id, GraphEditorController graphEditorController,
	                    GenerateController generateController, ResourceBundle language, Theme theme) {
		this.id = id;
		this.graphEditorController = graphEditorController;
		this.generateController = generateController;
		this.language = language;
		this.theme = theme;
		JMenuItem showProfile = new JMenuItem(language.getString("showProfile"));
		showProfile.addActionListener(new ShowProfileAction());

		JMenuItem nextDenser = new JMenuItem(language.getString("findNextDenser"));
		nextDenser.addActionListener(new AddNextDenserAction());

		JMenuItem delete = new JMenuItem(language.getString("delete"));
		delete.addActionListener(new DeleteAction());

		this.add(showProfile);
		this.add(nextDenser);
		this.add(delete);
	}

	private class ShowProfileAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			JFrame profile = new ViewProfileUI(graphEditorController.getProfile(id), language, theme);
			profile.setVisible(true);
		}
	}

	private class AddNextDenserAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			graphEditorController.addNextDenserToDatabase(id);
		}
	}

	private class DeleteAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			generateController.deleteGraph(id);
		}
	}
}
