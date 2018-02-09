/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.controller.CorrelationController;
import edu.kit.ipd.dbis.correlation.exceptions.InvalidCorrelationInputException;
import edu.kit.ipd.dbis.gui.popups.CorrelationRequestUI;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class CorrelationUI extends JPanel {

	private final CorrelationController controller;
	private JTextField correlationInput;

	public CorrelationUI(CorrelationController controller, ResourceBundle language, Theme theme) {
		this.controller = controller;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(theme.backgroundColor);
		this.setForeground(theme.foregroundColor);
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

		JPanel title = new JPanel();
		title.setLayout(new BorderLayout());
		title.setBackground(theme.lightNeutralColor);
		JLabel correlationLabel = new JLabel("Correlation Request"); // todo use language resource
		title.add(correlationLabel);

		JPanel inputContainer = new JPanel(new BorderLayout());
		inputContainer.add(Box.createHorizontalStrut(8), BorderLayout.WEST);
		correlationInput = new JTextField("Max Pearson 3");
		correlationInput.setBackground(theme.backgroundColor);
		JButton go = new JButton("Go..."); // todo replaces with text from language resource
		go.addActionListener(new CorrelationRequestAction(controller, correlationInput, language, theme));
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

		public CorrelationRequestAction(CorrelationController controller, JTextField correlationInput,
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

}
