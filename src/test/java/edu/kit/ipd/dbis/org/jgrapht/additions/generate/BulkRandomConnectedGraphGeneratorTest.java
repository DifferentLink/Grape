package edu.kit.ipd.dbis.org.jgrapht.additions.generate;

import edu.kit.ipd.dbis.org.jgrapht.additions.alg.interfaces.BfsCodeAlgorithm;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.complex.BfsCode;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class BulkRandomConnectedGraphGeneratorTest {


	@Test(expected = IllegalArgumentException.class)
	public void negativeParameterTest() {
		BulkGraphGenerator bulkGen = new BulkRandomConnectedGraphGenerator();
		HashSet<PropertyGraph> target = new HashSet<>();
		bulkGen.generateBulk(target,-1, 2, 2, 1, 1);
	}

	@Test (expected = NotEnoughGraphsException.class)
	public void quantityTooBigTest() {
		BulkGraphGenerator bulkGen = new BulkRandomConnectedGraphGenerator<>();
		HashSet<PropertyGraph> target = new HashSet<>();
		bulkGen.generateBulk(target,10, 2, 2, 1, 1);
		Assert.assertTrue(target.size() == 1000);
	}

	@Test
	public void allGraphsWithFiveVerticesTest() {
		BulkGraphGenerator bulkGen = new BulkRandomConnectedGraphGenerator<>();
		HashSet<PropertyGraph> target = new HashSet<>();
		try {
			bulkGen.generateBulk(target, 2000, 5, 5, 1, 1000);
		} catch (IllegalArgumentException | NotEnoughGraphsException e) {
			System.out.println(target.size());
			Assert.assertTrue(target.size() == 21);
		}
	}

	@Test
	public void allGraphsWithFourVerticesTest() {
		BulkGraphGenerator bulkGen = new BulkRandomConnectedGraphGenerator<>();
		HashSet<PropertyGraph> target = new HashSet<>();
		try {
			bulkGen.generateBulk(target, 5000, 5, 5, 0, 1000);
		} catch (IllegalArgumentException | NotEnoughGraphsException e) {
			LinkedList<BfsCodeAlgorithm.BfsCode> allCodes = new LinkedList<>();

			for (PropertyGraph graph : target) {
				BfsCodeAlgorithm.BfsCode code = (BfsCodeAlgorithm.BfsCodeImpl) graph.getProperty(BfsCode.class).getValue();

				Iterator<BfsCodeAlgorithm.BfsCode> iterator = allCodes.iterator();
				int index = 0;
				while (iterator.hasNext()) {
					BfsCodeAlgorithm.BfsCode acode = iterator.next();
					if (code.compareTo(acode) < 0) {
						index++;
					}
				}
				allCodes.add(index, code);
			}
			for (BfsCodeAlgorithm.BfsCode code : allCodes) {
				int[] c = code.getCode();
				for (int i = 0; i < c.length; i++) {
					System.out.print(c[i]);
				}
				System.out.println();
			}
			System.out.println(target.size());
			//Assert.assertTrue(target.size() == 6);
		}
	}

	@Test
	public void twoVertiecesTest() {
		BulkGraphGenerator bulkGen = new BulkRandomConnectedGraphGenerator<>();
		HashSet<PropertyGraph> target = new HashSet<>();
		bulkGen.generateBulk(target,1, 2, 2, 1, 1);
		Assert.assertTrue(target.size() == 1);
	}

	@Ignore
	@Test
	public void largeParameterTest() {
		BulkGraphGenerator bulkGen = new BulkRandomConnectedGraphGenerator<>();
		HashSet<PropertyGraph> target = new HashSet<>();
		bulkGen.generateBulk(target,3000, 0, 20, 1, 3000);
		Assert.assertTrue(target.size() == 3000);
	}
}
