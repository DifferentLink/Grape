/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.controller.GenerateController;
import edu.kit.ipd.dbis.controller.InvalidBfsCodeInputException;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class ReadBFSCodeUI extends JFrame{

	private JTextField bfsCodeInput;

	public ReadBFSCodeUI(GenerateController generateController, ResourceBundle language, Theme theme) { // todo use basic regex matching for BFS-Code
		super(language.getString("generateGraphs"));
		this.setSize(350, 200);
		this.setResizable(false);

		JPanel content = new JPanel();
		content.setBackground(theme.backgroundColor);
		content.setForeground(theme.foregroundColor);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		bfsCodeInput = new JTextField();
		bfsCodeInput.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		content.add(bfsCodeInput);

		JButton readGraph = new JButton(language.getString("generateGraphs"));
		readGraph.addActionListener(new ReadBFSCodeAction(generateController));
		readGraph.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		readGraph.setBackground(theme.assertiveBackground);
		content.add(readGraph);

		this.add(content);
	}

	private class ReadBFSCodeAction implements ActionListener {

		private final GenerateController generateController;

		public ReadBFSCodeAction(GenerateController generateController) {
			this.generateController = generateController;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				generateController.generateBFSGraph(bfsCodeInput.getText());
			} catch (InvalidBfsCodeInputException e) {
				//TODO: implement me
				e.printStackTrace();
			}
		}
	}
}
