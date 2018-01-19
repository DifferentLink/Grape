package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import org.junit.Test;

import java.util.Set;

public class PropertyFactoryTest {

	@Test
	public void createAllProperties() {
		Set<Property> list = PropertyFactory.createAllProperties();
		System.out.println(list.size());
	}
}