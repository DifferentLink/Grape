/**
 * Created by robinlink
 */

package edu.kit.ipd.dbis.gui.filter;


public class SimpleFilter extends Filter {

	public SimpleFilter(int id) {
		super(id);
	}

	public SimpleFilter(int id, String text) {
		super(id);
		super.setText(text);
	}
}
