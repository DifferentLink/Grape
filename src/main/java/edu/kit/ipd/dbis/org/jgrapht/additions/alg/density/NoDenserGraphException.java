package edu.kit.ipd.dbis.org.jgrapht.additions.alg.density;

/**
 * The exception if there is no denser graph
 */
public class NoDenserGraphException extends RuntimeException {
	/**
	 * constructs a new no denser graph exception
	 * @param s the exception message
	 */
	public NoDenserGraphException(String s) {
		super(s);
	}
}
