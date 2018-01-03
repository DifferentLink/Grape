/**
 * Created by Robin Link
 */

package edu.kit.ipd.dbis.gui;

import javax.swing.*;
import java.util.ResourceBundle;

public class StatusbarUI {
	public static JPanel makeStatusbarUI(ResourceBundle language) {
		JPanel statusbarUI = new JPanel();
		statusbarUI.add(new JLabel("StatusbarUI"));
		return statusbarUI;
	}
}
