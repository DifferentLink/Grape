package edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces;

import java.util.List;

public interface ProfileDensityAlgorithm<V,E> {
	Profile getProfile();

	public interface Profile<V,E> {
		List<BfsCodeAlgorithm.BfsCode> getProfile();
	}

	public class ProfileImpl<V,E> implements Profile {

		@Override
		public List<BfsCodeAlgorithm.BfsCode> getProfile() {
			return null;
		}
	}
}
