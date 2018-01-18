/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.filter;

public abstract class Filter {
	protected final int id;
	protected boolean isActive;
	protected String text = "";

	public Filter(final int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
