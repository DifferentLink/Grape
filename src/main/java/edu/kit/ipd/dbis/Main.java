package edu.kit.ipd.dbis;

import edu.kit.ipd.dbis.controller.CalculationController;
import edu.kit.ipd.dbis.controller.CorrelationController;
import edu.kit.ipd.dbis.controller.DatabaseController;
import edu.kit.ipd.dbis.controller.FilterController;
import edu.kit.ipd.dbis.controller.GenerateController;
import edu.kit.ipd.dbis.controller.GraphEditorController;
import edu.kit.ipd.dbis.controller.StatusbarController;
import edu.kit.ipd.dbis.gui.GrapeUI;
import edu.kit.ipd.dbis.gui.themes.GrapeTheme;

import javax.swing.SwingUtilities;
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
				CorrelationController.getInstance(),
				new DatabaseController(),
				FilterController.getInstance(),
				GenerateController.getInstance(),
				GraphEditorController.getInstance(),
				StatusbarController.getInstance(),
				getLanguage(), new GrapeTheme()));
	}

	private static ResourceBundle getLanguage() {
		// Try to use system language.
		try {
			// Locale currentLocale = Locale.getDefault();
			// return ResourceBundle.getBundle("languages", currentLocale);
			return ResourceBundle.getBundle(
					"languages",
					new Locale.Builder().setLanguage("en").setRegion("US").build());
		} catch (MissingResourceException languageNotFound) {
			return null;
		}
	}
}