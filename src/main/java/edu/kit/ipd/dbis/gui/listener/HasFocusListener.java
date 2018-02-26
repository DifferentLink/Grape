package edu.kit.ipd.dbis.gui.listener;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * An implementation of FocusListener that closes a JFrame on focus loss
 */
public class HasFocusListener implements FocusListener {
	private final JFrame frame;

	/**
	 * @param frame the JFrame to close when it loses focus
	 */
	public HasFocusListener(JFrame frame) {
		this.frame = frame;
	}

	/**
	 * Nothing happens when the JFrame gains the focus
	 * @param focusEvent information about the context
	 */
	@Override
	public void focusGained(FocusEvent focusEvent) {

	}

	/**
	 * The given JFrame closes when it loses the focus
	 * @param focusEvent information about the context
	 */
	@Override
	public void focusLost(FocusEvent focusEvent) {
		frame.dispose();
	}
}