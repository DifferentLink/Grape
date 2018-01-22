/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.filter;

public abstract class Filter {
	protected final int id;
	protected boolean isActive = true;
	protected String text = "...";

	public Filter(final int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void toggle() {
		isActive = !isActive;
	}

	public void disable() {
		isActive = false;
	}

	public void enable() {
		isActive = true;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public int getID() {
		return id;
	}
}
