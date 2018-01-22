/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class GenerateGraphUI extends JFrame {
	public GenerateGraphUI(ResourceBundle language, Theme theme) {

		super(language.getString("generateGraphs"));
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
		JTextField numVerticesInput = new JTextField();
		properties.add(numVerticesInput);

		JLabel numEdges = new JLabel(language.getString("numberOfEdges"));
		properties.add(numEdges);
		JTextField numEdgesInput = new JTextField();
		properties.add(numEdgesInput);

		content.add(properties);

		JPanel generate = new JPanel();
		generate.setLayout(new GridLayout(1, 2, 2, 0));

		JTextField numGraphsInput = new JTextField();
		numGraphsInput.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		generate.add(numGraphsInput);

		JButton generateGraphsButton = new JButton(language.getString("generateGraphs"));
		generateGraphsButton.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		generateGraphsButton.setBackground(theme.assertiveBackground);
		generate.add(generateGraphsButton);

		content.add(generate);
		this.add(content);
	}
}
