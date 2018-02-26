package edu.kit.ipd.dbis.gui.listener;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class HasFocusListener implements FocusListener {
	private final JFrame frame;

	public HasFocusListener(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void focusGained(FocusEvent focusEvent) {

	}

	@Override
	public void focusLost(FocusEvent focusEvent) {
		frame.dispose();
	}
}