package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

/**
 * the not enough graphs exception
 */
public class NotEnoughGraphsException extends RuntimeException {
	/**
	 * constructs a new not enough graph exception
	 * @param s the exception message
	 */
	public NotEnoughGraphsException(String s) {
		super(s);
	}
}
