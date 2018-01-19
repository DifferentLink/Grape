/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.controller.CorrelationController;
import edu.kit.ipd.dbis.gui.popups.CorrelationRequestUI;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class CorrelationUI extends JPanel {

	public CorrelationUI(CorrelationController controller, ResourceBundle language, Theme theme) {

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
		JTextField correlationInput = new JTextField("Correlation request...");
		correlationInput.setColumns(10000);
		correlationInput.setBackground(theme.backgroundColor);
		JButton go = new JButton("Go..."); // todo replaces with text from language resource
		go.addActionListener(new CorrelationRequestAction(language, theme));
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

		ResourceBundle language;
		Theme theme;

		public CorrelationRequestAction(ResourceBundle language, Theme theme) {
			this.language = language;
			this.theme = theme;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			JFrame correlationRequest = new CorrelationRequestUI(language, theme);
			correlationRequest.setVisible(true);
		}
	}

}
