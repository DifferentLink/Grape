/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class CorrelationUI extends GUIElement {

	public CorrelationUI(Controller controller, ResourceBundle language, Theme theme) {
		super(controller, language, theme);

		this.setLayout(new BorderLayout());
		this.setBackground(theme.backgroundColor);
		this.setForeground(theme.foregroundColor);
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

		JTextField correlationInput = new JTextField("Correlation request...");
		correlationInput.setColumns(1000);
		correlationInput.setBackground(theme.backgroundColor);
		correlationInput.setSize(10, 30);
		JButton go = new JButton("Go..."); // todo replaces with text from language resource
		theme.style(go);
		this.add(go, BorderLayout.EAST);
		this.add(correlationInput, BorderLayout.WEST);
	}

	/**
	 * Updates the GUIWindow element.
	 */
	@Override
	public void update() {

	}
}
