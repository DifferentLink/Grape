package edu.kit.ipd.dbis;

import edu.kit.ipd.dbis.gui.MainWindow;

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
		new MainWindow(getLanguage());
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
