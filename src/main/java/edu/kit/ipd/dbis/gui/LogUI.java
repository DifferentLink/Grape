package edu.kit.ipd.dbis.gui;

import edu.kit.ipd.dbis.controller.StatusbarController;
import edu.kit.ipd.dbis.gui.listener.HasFocusListener;
import edu.kit.ipd.dbis.gui.themes.Theme;
import edu.kit.ipd.dbis.log.Event;
import edu.kit.ipd.dbis.log.History;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	 * set history
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
		this.dispose();
		this.addFocusListener(new HasFocusListener(this));
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(container);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
		history.getEvents().forEach(event -> container.add(renderEvent(event)));
		this.add(scrollPane);
		this.setMinimumSize(new Dimension(500, 100));
		this.setMaximumSize(new Dimension(500, 200));
		Point position = new Point(component.getLocationOnScreen().x - this.getWidth() + component.getWidth(),
				component.getLocationOnScreen().y - this.getHeight());
		this.setLocation(position);
		this.setVisible(true);
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
	}

	/**
	 * Takes an event as the input and calls the respective method to make a JPanel out of it
	 * @param event
	 * @return
	 */
	private JPanel renderEvent(Event event) {
		switch (event.getType()) {
			case MESSAGE : return renderMESSAGE(event);
			case REMOVE : return renderREMOVE(event);
			case ADD : return renderADD(event);
			default : break;
		}
		return null;
	}

	private JPanel renderMESSAGE(Event event) {
		JPanel container = new JPanel(new BorderLayout());
		container.add(new JLabel(event.getMessage()));
		container.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, theme.neutralColor));
		return container;
	}

	private JPanel renderREMOVE(Event event) {
		JPanel container = new JPanel(new BorderLayout());
		String changedGraphs = "";
		if (event.getChangedGraphs().size() > 3) {
			Iterator iterator = event.getChangedGraphs().iterator();
			changedGraphs = String.valueOf(iterator.next()) + ", "
					+ String.valueOf(iterator.next()) + ", "
					+ String.valueOf(iterator.next());
		} else if (event.getChangedGraphs().size() > 0) {
			Iterator iterator = event.getChangedGraphs().iterator();
			while (iterator.hasNext()) {
				changedGraphs += String.valueOf(iterator.next()) + ", ";
			}
			changedGraphs = changedGraphs.substring(changedGraphs.length() - 2);
		}
		container.add(new JLabel(event.getMessage() + ": " + changedGraphs));
		return container;
	}

	private JPanel renderADD(Event event) {
		JPanel container = new JPanel(new BorderLayout());
		container.add(new JLabel(event.getMessage()));
		return container;
	}
}
