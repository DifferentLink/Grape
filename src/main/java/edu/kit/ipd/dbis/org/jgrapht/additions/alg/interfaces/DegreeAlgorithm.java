package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

/**
 * An algorithm that handles degrees in graphs, such as determining the maximal or minimal
 degree.

 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public interface DegreeAlgorithm<V, E> {
	/**
	 * Returns the degree.
	 * //TODO: now has return type Number, differs from design
	 *
	 * @return the degree
	 */
	Number getDegree();
}
