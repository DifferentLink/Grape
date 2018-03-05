package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.controller.GenerateController;
import edu.kit.ipd.dbis.controller.exceptions.InvalidGeneratorInputException;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * A window to generate graphs
 */
public class GenerateGraphUI extends JFrame {

	private final GenerateController generateController;
	private final ResourceBundle language;
	private final Theme theme;

	private final JTextField numVerticesInput;
	private final JTextField numEdgesInput;
	private final JTextField numGraphsInput;
	private final JButton generateGraphsButton;

	private int minVertices = 2;
	private int maxVertices = 5;
	private int minEdges = 1;
	private int maxEdges = 7;
	private int amount = 4;

	/**
	 * @param generateController the controller responsible for generate the graphs
	 * @param language the language to use
	 * @param theme the theme to style the window
	 */
	public GenerateGraphUI(GenerateController generateController, ResourceBundle language, Theme theme) {

		super(language.getString("generateGraphs"));
		this.generateController = generateController;
		this.language = language;
		this.theme = theme;

		this.setSize(350, 200);
		this.setResizable(false);

		JPanel content = new JPanel();
		content.setBackground(theme.backgroundColor);
		content.setForeground(theme.foregroundColor);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		JPanel properties = new JPanel();
		properties.setLayout(new GridLayout(2, 2, 2, 5));
		properties.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		properties.setBackground(theme.backgroundColor);
		properties.setForeground(theme.foregroundColor);

		JLabel numVertices = new JLabel(language.getString("numberOfVertices"));
		properties.add(numVertices);
		numVerticesInput = new JTextField(Integer.toString(minVertices) + "-" + Integer.toString(maxVertices));
		numVerticesInput.getDocument().addDocumentListener(new TextInputChangeListener());
		numVerticesInput.setBackground(theme.backgroundColor);
		properties.add(numVerticesInput);

		JLabel numEdges = new JLabel(language.getString("numberOfEdges"));
		properties.add(numEdges);
		numEdgesInput = new JTextField(Integer.toString(minEdges) + "-" + Integer.toString(maxEdges));
		numEdgesInput.getDocument().addDocumentListener(new TextInputChangeListener());
		numEdgesInput.setBackground(theme.backgroundColor);
		properties.add(numEdgesInput);

		content.add(properties);

		JPanel generate = new JPanel();
		generate.setLayout(new GridLayout(1, 2, 2, 0));

		numGraphsInput = new JTextField(Integer.toString(amount));
		numGraphsInput.getDocument().addDocumentListener(new TextInputChangeListener());
		numGraphsInput.setBackground(theme.backgroundColor);
		numGraphsInput.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		generate.add(numGraphsInput);

		generateGraphsButton = new JButton(language.getString("generateGraphs"));
		generateGraphsButton.addActionListener(
				new GenerateGraphsAction(generateController, this));
		generateGraphsButton.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		generateGraphsButton.setBackground(theme.assertiveBackground);
		generate.add(generateGraphsButton);

		content.add(generate);
		this.getRootPane().setDefaultButton(generateGraphsButton);
		this.add(content);
		this.setLocationRelativeTo(null);
	}

	@SuppressWarnings("Duplicates")
	private void updateInput() {
		if (numVerticesInput.getText().matches("\\d+")) {
			minVertices = Integer.parseInt(numVerticesInput.getText());
			maxVertices = minVertices;
		} else if (numVerticesInput.getText().matches("\\d+-\\d+")) {
			minVertices = Integer.parseInt(numVerticesInput.getText().split("-")[0]);
			maxVertices = Integer.parseInt(numVerticesInput.getText().split("-")[1]);
		}

		if (numEdgesInput.getText().matches("\\d+")) {
			minEdges = Integer.parseInt(numEdgesInput.getText());
			maxEdges = minEdges;
		} else if (numEdgesInput.getText().matches("\\d+-\\d+")) {
			minEdges = Integer.parseInt(numEdgesInput.getText().split("-")[0]);
			maxEdges = Integer.parseInt(numEdgesInput.getText().split("-")[1]);
		}

		if (numGraphsInput.getText().matches("\\d+")) {
			amount = Integer.parseInt(numGraphsInput.getText());
		}

		update();
	}

	private void update() {
		final boolean verticesMatch = GenerateController.isValidVerticesInput(numVerticesInput.getText());
		final boolean edgesMatch = GenerateController.isValidEdgesInput(numEdgesInput.getText());
		final boolean amountMatch = GenerateController.isValidNumberOfGraphs(numGraphsInput.getText());

		if (verticesMatch) {
			numVerticesInput.setBackground(theme.backgroundColor);
		} else if (!numVerticesInput.getText().equals("")) {
			numVerticesInput.setBackground(theme.unassertiveBackground);
		}

		if (edgesMatch) {
			numEdgesInput.setBackground(theme.backgroundColor);
		} else if (!numEdgesInput.getText().equals("")) {
			numEdgesInput.setBackground(theme.unassertiveBackground);
		}

		if (amountMatch) {
			numGraphsInput.setBackground(theme.backgroundColor);
		} else if (!numGraphsInput.getText().equals("")) {
			numGraphsInput.setBackground(theme.unassertiveBackground);
		}

		if (verticesMatch && edgesMatch && amountMatch) {
			generateGraphsButton.setEnabled(true);
			generateGraphsButton.setBackground(theme.assertiveBackground);
		} else {
			generateGraphsButton.setEnabled(false);
			generateGraphsButton.setBackground(theme.buttonDisabledColor);
		}
	}

	private class GenerateGraphsAction implements ActionListener {
		private final GenerateController generateController;
		private final GenerateGraphUI generateGraphUI;

		GenerateGraphsAction(GenerateController generateController, GenerateGraphUI generateGraphUI) {
			this.generateController = generateController;
			this.generateGraphUI = generateGraphUI;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				generateController.generateGraphs(
						minVertices, maxVertices, minEdges, maxEdges, amount);
				generateGraphUI.dispose();
			} catch (InvalidGeneratorInputException | InterruptedException e) {
				e.printStackTrace();
			}
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
