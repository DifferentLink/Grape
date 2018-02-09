/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.controller.GraphEditorController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphOptions extends JPopupMenu {

	private final int id;
	private final GraphEditorController graphEditorController;

	public GraphOptions(final int id, GraphEditorController graphEditorController) {
		this.id = id;
		this.graphEditorController = graphEditorController;
		JMenuItem showProfile = new JMenuItem("Show Profile...");
		showProfile.addActionListener(new ShowProfileAction());

		JMenuItem nextDenser = new JMenuItem("Find next-denser");
		nextDenser.addActionListener(new AddNextDenserAction());

		JMenuItem delete = new JMenuItem("Delete");
		delete.addActionListener(new DeleteAction());

		this.add(showProfile);
		this.add(nextDenser);
		this.add(delete);
	}

	private class ShowProfileAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
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

		}
	}
}
