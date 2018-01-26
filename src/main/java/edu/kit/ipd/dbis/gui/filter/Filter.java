/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.filter;

public abstract class Filter {
	private final int id;
	private boolean isActive = false;
	private String text = "";

	public Filter(final int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public int getID() {
		return id;
	}
}
