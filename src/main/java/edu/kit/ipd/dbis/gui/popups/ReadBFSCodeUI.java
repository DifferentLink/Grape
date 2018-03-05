/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.controller.GenerateController;
import edu.kit.ipd.dbis.controller.exceptions.InvalidBfsCodeInputException;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * A window to read a BFS-Code
 */
public class ReadBFSCodeUI extends JFrame {

	private JTextField bfsCodeInput;

	/**
	 * @param generateController the controller responsible for parsing the BFS-Code
	 * @param language the language to use
	 * @param theme the theme to style the window with
	 */
	public ReadBFSCodeUI(GenerateController generateController, ResourceBundle language, Theme theme) {
		super(language.getString("readBFSCode"));
		this.setSize(350, 200);
		this.setResizable(false);

		JPanel content = new JPanel();
		content.setBackground(theme.backgroundColor);
		content.setForeground(theme.foregroundColor);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		bfsCodeInput = new JTextField();
		bfsCodeInput.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		content.add(bfsCodeInput);

		JButton readGraph = new JButton(language.getString("readBFSCode"));
		readGraph.addActionListener(new ReadBFSCodeAction(generateController, this));
		readGraph.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		readGraph.setBackground(theme.assertiveBackground);
		content.add(readGraph);
		this.add(content);
		this.setLocationRelativeTo(null);
	}

	private class ReadBFSCodeAction implements ActionListener {

		private final GenerateController generateController;
		private final ReadBFSCodeUI readBFSCodeUI;

		ReadBFSCodeAction(GenerateController generateController, ReadBFSCodeUI readBFSCodeUI) {
			this.generateController = generateController;
			this.readBFSCodeUI = readBFSCodeUI;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				generateController.generateBFSGraph(bfsCodeInput.getText());
			} catch (InvalidBfsCodeInputException e) {
				// TODO: implement me
				e.printStackTrace();
			}
			readBFSCodeUI.dispose();
		}
	}
}
