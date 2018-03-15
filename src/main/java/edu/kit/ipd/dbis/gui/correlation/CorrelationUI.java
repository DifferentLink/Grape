package edu.kit.ipd.dbis.gui.correlation;

import edu.kit.ipd.dbis.controller.CorrelationController;
import edu.kit.ipd.dbis.correlation.CorrelationRequest;
import edu.kit.ipd.dbis.correlation.exceptions.InvalidCorrelationInputException;
import edu.kit.ipd.dbis.gui.popups.CorrelationRequestUI;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * The correlation panel in Grape's GUI
 */
public class CorrelationUI extends JPanel {

	private final CorrelationController correlationController;
	private final Theme theme;
	private JTextField correlationInput;

	/**
	 * Constructs the correlation panel
	 * @param correlationController the correlationController responsible for the correlation request
	 * @param language the language used
	 * @param theme theme theme used to style to correlation window
	 */
	public CorrelationUI(CorrelationController correlationController, ResourceBundle language, Theme theme) {
		this.correlationController = correlationController;
		this.theme = theme;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(theme.backgroundColor);
		this.setForeground(theme.foregroundColor);
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

		JPanel title = new JPanel();
		title.setLayout(new BorderLayout());
		title.setBackground(theme.lightNeutralColor);
		JLabel correlationLabel = new JLabel(language.getString("correlationRequest"));
		title.add(correlationLabel);

		JPanel inputContainer = new JPanel(new BorderLayout());
		inputContainer.add(Box.createHorizontalStrut(8), BorderLayout.WEST);
		correlationInput = new JTextField("Max Pearson 3");
		correlationInput.getDocument().addDocumentListener(new CorrelationInputChangeListener(correlationInput));
		correlationInput.setBackground(Color.WHITE);
		JButton go = new JButton(language.getString("go"));
		go.addActionListener(new CorrelationRequestAction(correlationController, correlationInput, language, theme));
		go.setBackground(theme.assertiveBackground);
		go.setMinimumSize(new Dimension(120, 30));
		inputContainer.add(correlationInput, BorderLayout.CENTER);
		inputContainer.add(go, BorderLayout.EAST);

		this.add(Box.createVerticalStrut(4));
		this.add(title);
		this.add(inputContainer);
		this.add(Box.createVerticalStrut(4));
		this.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, theme.foregroundColor));
	}

	private static class CorrelationRequestAction implements ActionListener {

		private final CorrelationController controller;
		private final JTextField correlationInput;
		ResourceBundle language;
		Theme theme;

		CorrelationRequestAction(CorrelationController controller, JTextField correlationInput,
		                         ResourceBundle language, Theme theme) {
			this.controller = controller;
			this.correlationInput = correlationInput;
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				JFrame correlationRequest = new CorrelationRequestUI(
						controller, correlationInput.getText(), language, theme);
				correlationRequest.setVisible(true);
				correlationInput.setBackground(Color.WHITE);
			} catch (InvalidCorrelationInputException e) {
				correlationInput.setBackground(theme.lightNeutralColor);
			}
		}
	}

	private class CorrelationInputChangeListener implements DocumentListener {
		private final JTextField textField;

		CorrelationInputChangeListener(JTextField textField) {
			this.textField = textField;
		}

		@Override
		public void insertUpdate(DocumentEvent documentEvent) {
			update();
		}

		@Override
		public void removeUpdate(DocumentEvent documentEvent) {
			update();
		}

		@Override
		public void changedUpdate(DocumentEvent documentEvent) {
			update();
		}

		private void update() {
			(new CorrelationSuggestions(textField)).show(
					textField, textField.getX(), textField.getY() + textField.getHeight());
			textField.requestFocus();

			try {
				CorrelationRequest.parseCorrelationToString(textField.getText());
				textField.setBackground(Color.WHITE);
			} catch (InvalidCorrelationInputException e) {
				textField.setBackground(theme.lightNeutralColor);
			}
		}
	}
}
