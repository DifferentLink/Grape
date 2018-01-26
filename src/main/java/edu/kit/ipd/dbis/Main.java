package edu.kit.ipd.dbis;

import edu.kit.ipd.dbis.controller.*;
import edu.kit.ipd.dbis.gui.GrapeUI;
import edu.kit.ipd.dbis.gui.themes.GrapeTheme;
import edu.kit.ipd.dbis.log.Log;

import javax.swing.*;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Grape's main class.
 */
public class Main {

	/**
	 * Grape's main method.
	 * @param args arguments provided when run using the command line.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new GrapeUI(CalculationController.getInstance(),
				new CorrelationController(),
				new DatabaseController(),
				FilterController.getInstance(),
				GenerateController.getInstance(),
				GraphEditorController.getInstance(),
				new Log(100),
				getLanguage(), new GrapeTheme()));
	}

	private static ResourceBundle getLanguage() {
		// Try to use system language.
		try {
			Locale currentLocale = Locale.getDefault();
			return ResourceBundle.getBundle("languages", currentLocale);
		} catch (MissingResourceException lanuageNotFound) {
			// Set default language to english (en_US)
			return ResourceBundle.getBundle(
					"languages",
					new Locale.Builder().setLanguage("en").setRegion("US").build());
		}
	}
}