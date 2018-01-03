/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import javax.swing.*;
import java.util.ResourceBundle;

public class FilterUI extends JPanel {

	private FilterUI() {}

	public static JPanel makeFilterUI(ResourceBundle language) {
		JPanel filterUI = new JPanel();
		filterUI.add(new JLabel("FilterUI"));
		return filterUI;
	}
}
