package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

public interface BfsCodeAlgorithm<V,E> {
	BfsCode getBfsCode();

	public interface BfsCode<V,E> {
		int getLength();
		int [] getCode();
	}

	public class BfsCodeImpl<V,E> implements BfsCode {

		@Override
		public int getLength() {
			return 0;
		}

		@Override
		public int[] getCode() {
			return new int[0];
		}
	}
}
