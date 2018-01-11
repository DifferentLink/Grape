package edu.kit.ipd.dbis.org.jgrapht.additions.graph;

import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyTest {

	@Test
	public void constructorTest() {
		Property property = new Property(PropertyType.BFSCODE);
		assertEquals(property.type, PropertyType.BFSCODE);
	}

	@Test
	public void calculate() {
	}
}