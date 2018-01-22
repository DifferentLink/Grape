/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.swing.*;
import java.util.ResourceBundle;

public class ReadBFSCodeUI extends JFrame{
	public ReadBFSCodeUI(ResourceBundle language, Theme theme) {
		super(language.getString("generateGraphs"));
		this.setSize(350, 200);
		this.setResizable(false);

		JPanel content = new JPanel();
		content.setBackground(theme.backgroundColor);
		content.setForeground(theme.foregroundColor);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		JTextField bfsCodeInput = new JTextField();
		bfsCodeInput.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		content.add(bfsCodeInput);

		JButton readGraph = new JButton(language.getString("generateGraphs"));
		readGraph.setBorder(BorderFactory.createLineBorder(theme.foregroundColor, 1));
		readGraph.setBackground(theme.assertiveBackground);
		content.add(readGraph);

		this.add(content);

	}
}
