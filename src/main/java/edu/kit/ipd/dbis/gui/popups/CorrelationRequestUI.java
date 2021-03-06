package edu.kit.ipd.dbis.gui.popups;

import edu.kit.ipd.dbis.controller.CorrelationController;
import edu.kit.ipd.dbis.correlation.CorrelationOutput;
import edu.kit.ipd.dbis.correlation.exceptions.InvalidCorrelationInputException;
import edu.kit.ipd.dbis.gui.AlternateTable;
import edu.kit.ipd.dbis.gui.themes.Theme;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * A window that displays the result of a correlation request
 */
public class CorrelationRequestUI extends JFrame {

	private final CorrelationController correlationController;
	private final String correlationRequest;
	private final ResourceBundle language;
	private final Theme theme;

	/**
	 * @param correlationController the responsible controller
	 * @param correlationRequest the correlation request as a string
	 * @param language the language to use
	 * @param theme the theme to style the window
	 * @throws InvalidCorrelationInputException if the correlation request is invalid
	 */
	public CorrelationRequestUI(CorrelationController correlationController,
	                            String correlationRequest,
	                            ResourceBundle language, Theme theme) throws InvalidCorrelationInputException {
		this.correlationController = correlationController;
		this.correlationRequest = correlationRequest;
		this.language = language;
		this.theme = theme;

		try {
			Image logo = ImageIO.read(getClass().getResource("/icons/GrapeLogo.png"));
			this.setIconImage(logo);
		} catch (IOException ignored) { }

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(panel);

		JTable table = populateTable();
		panel.add(table);

		String[] separatedCorrelationRequest = correlationRequest.split(" ");
		String amount = separatedCorrelationRequest[separatedCorrelationRequest.length - 1];
		String cutCorrelationRequest =
				correlationRequest.substring(0, correlationRequest.length() - amount.length() - 1);
		JLabel correlationText = new JLabel(language.getString("request") + " "
				+ cutCorrelationRequest + " " + (table.getColumnCount() - 1));
		theme.style(correlationText);
		panel.add(correlationText);

		JPanel buttonAlignment = new JPanel();
		buttonAlignment.setLayout(new BoxLayout(buttonAlignment, BoxLayout.X_AXIS));
		buttonAlignment.add(Box.createHorizontalGlue());
		JButton addToTable = new JButton(language.getString("close"));
		addToTable.addActionListener(new CloseAction(this));
		theme.style(addToTable);
		buttonAlignment.add(addToTable);

		panel.add(buttonAlignment);
		this.add(scrollPane);
		this.pack();
		this.setPreferredSize(new Dimension(400, 300));
		this.setLocationRelativeTo(null);
	}

	private class CloseAction implements ActionListener {
		private final CorrelationRequestUI correlationRequestUI;

		CloseAction(CorrelationRequestUI correlationRequestUI) {
			this.correlationRequestUI = correlationRequestUI;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			correlationRequestUI.dispose();
		}
	}

	private JTable populateTable() throws InvalidCorrelationInputException {
		List<CorrelationOutput> columns = correlationController.addNewCorrelation(correlationRequest);
		int tableSize = columns.size() + 1;
		String[][] data = new String[3][tableSize];

		data[0][0] = language.getString("firstProperty");
		data[1][0] = language.getString("secondProperty");
		data[2][0] = language.getString("correlation");
		for (int i = 1; i < tableSize; i++) {
			CorrelationOutput column = columns.get(i - 1);
			data[0][i] = column.getFirstProperty();
			data[1][i] = column.getSecondProperty();
			data[2][i] = String.valueOf(column.getOutputNumber());
		}

		String[] columnsHeader = new String[tableSize];
		for (int i = 0; i < tableSize; i++) {
			columnsHeader[i] = "";
		}

		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext fontRenderer = new FontRenderContext(affinetransform,true,true);
		Font font = theme.defaultFont;

		JTable table = new AlternateTable(new DefaultTableModel(data, columnsHeader), theme);

		for (int i = 0; i < tableSize; i++) {
			String text = (String) table.getValueAt(0, i);
			final int optimalWidth =
					(int) (font.getStringBounds("  " + text + "  ", fontRenderer).getWidth());
			table.getColumnModel().getColumn(i).setMinWidth(optimalWidth);
		}

		return table;
	}
}
