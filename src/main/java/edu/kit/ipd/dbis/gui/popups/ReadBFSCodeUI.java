/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.controller.GenerateController;
import edu.kit.ipd.dbis.controller.exceptions.InvalidBfsCodeInputException;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * A window to read a BFS-Code
 */
public class ReadBFSCodeUI extends JFrame {

	private final GenerateController generateController;
	private final ResourceBundle language;
	private final Theme theme;

	private JTextField bfsCodeInput;
	private JButton readGraph;
	private String bfsCode;

	/**
	 * @param generateController the controller responsible for parsing the BFS-Code
	 * @param language the language to use
	 * @param theme the theme to style the window with
	 */
	public ReadBFSCodeUI(GenerateController generateController, ResourceBundle language, Theme theme) {
		super(language.getString("readBFSCode"));
		this.generateController = generateController;
		this.language = language;
		this.theme = theme;

		try {
			Image logo = ImageIO.read(getClass().getResource("/icons/GrapeLogo.png"));
			this.setIconImage(logo);
		} catch (IOException e) { }

		this.setSize(350, 200);
		this.setResizable(false);

		JPanel content = new JPanel();
		content.setBackground(theme.backgroundColor);
		content.setForeground(theme.foregroundColor);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		bfsCodeInput = new JTextField();
		bfsCodeInput.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		bfsCodeInput.getDocument().addDocumentListener(new TextInputChangeListener());
		content.add(bfsCodeInput);

		JButton readGraph = new JButton(language.getString("readBFSCode"));
		readGraph.addActionListener(new ReadBFSCodeAction(generateController, this));
		readGraph.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		readGraph.setBackground(theme.assertiveBackground);
		content.add(readGraph);
		this.add(content);
		this.setLocationRelativeTo(null);
	}

	private void updateInput() {
		if (bfsCodeInput.getText().matches("(-?1,\\d+,\\d+)(,-?1,\\d+,\\d+)*")) {
			bfsCode = bfsCodeInput.getText();
		}

		update();
	}

	private void update() {
		final boolean bfsCodeMatch = GenerateController.isValidBFS(bfsCodeInput.getText());

		if (bfsCodeMatch) {
			bfsCodeInput.setBackground(theme.backgroundColor);
		} else if (!bfsCodeInput.getText().equals("")) {
			bfsCodeInput.setBackground(theme.unassertiveBackground);
		}
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
				generateController.generateBFSGraph(bfsCode);
			} catch (InvalidBfsCodeInputException e) {
				e.printStackTrace();
			}
			readBFSCodeUI.dispose();
		}
	}

	private class TextInputChangeListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent documentEvent) {
			updateInput();
		}

		@Override
		public void removeUpdate(DocumentEvent documentEvent) {
			updateInput();
		}

		@Override
		public void changedUpdate(DocumentEvent documentEvent) {
			updateInput();
		}
	}
}
