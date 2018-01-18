package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

public interface TotalColoringAlgorithm<V,E> {
	TotalColoring getColoring();

	public interface TotalColoring<V,E> {
		int getNumberColors();
	}

	public class TotalColoringImplementation<V,E> implements TotalColoring {

		@Override
		public int getNumberColors() {
			return 0;
		}
	}
}
