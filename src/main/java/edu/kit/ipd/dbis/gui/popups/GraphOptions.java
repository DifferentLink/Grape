/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.popups;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphOptions extends JPopupMenu {
	public GraphOptions(final int id) {
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

		}
	}

	private class DeleteAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {

		}
	}
}
