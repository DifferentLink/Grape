package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.controller.StatusbarController;
import edu.kit.ipd.dbis.gui.listener.HasFocusListener;
import edu.kit.ipd.dbis.gui.themes.Theme;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.log.History;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * The log window which becomes visible with a click on the log button
 */
public class LogUI extends JFrame {
	private History history;
	private ResourceBundle language;
	private Theme theme;

	/**
	 * @param statusbarController the responsible controller
	 * @param language the language to use
	 * @param theme the theme to style the log
	 */
	public LogUI(StatusbarController statusbarController, ResourceBundle language, Theme theme) {
		this.history = statusbarController.getHistory();
		this.language = language;
		this.theme = theme;
		this.setUndecorated(true);
		this.setResizable(false);
	}

	/**
	 * @param history new history
	 */
	public void setHistory(History history) {
		this.history = history;
	}

	/**
	 * Render's GUI elements from the history into a log
	 * @param component the component used to position the log
	 */
	public void drawLog(Component component) {
		dispose();
		addFocusListener(new HasFocusListener(this));
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(container);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(Integer.MAX_VALUE, 10));

		for (String logEntry : history.toStringArray()) {
			container.add(new JLabel(logEntry));
		}

		this.add(scrollPane);
		this.setMinimumSize(new Dimension(500, 100));
		this.setMaximumSize(new Dimension(500, 200));
		Point position = new Point(component.getLocationOnScreen().x - this.getWidth() + component.getWidth(),
				component.getLocationOnScreen().y - this.getHeight());
		this.setLocation(position);
		this.setVisible(true);
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
		scrollPane.getHorizontalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getMaximum());
	}
}
