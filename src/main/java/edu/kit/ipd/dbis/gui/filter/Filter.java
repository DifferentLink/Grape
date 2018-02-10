package edu.kit.ipd.dbis.gui.filter;

/**
 * An abstract representation of the filter elements in the GUI.
 */
public abstract class Filter {
	private final int id;
	private boolean isActive = false;
	private String text = "";

	/**
	 * Every filter has a unique ID. This is ensured by the UIFilterManager.
	 * @param id the filter's unique id
	 */
	public Filter(final int id) {
		this.id = id;
	}

	/**
	 * @return the filter's text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the filter's text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return if the filter is active
	 */
	public boolean isActive() {
		return this.isActive;
	}

	/**
	 * @param active the new state of the filter
	 */
	public void setActive(boolean active) {
		isActive = active;
	}

	/**
	 * @return the filter's unique ID
	 */
	public int getID() {
		return id;
	}
}
