package edu.kit.ipd.dbis.correlation;

import org.junit.*;

import java.util.HashSet;
import java.util.TreeSet;

public class CorrelationTest {

    @Test
    public void testSetAttributeCounter() {
        Correlation testCorrelation = new Pearson();
        testCorrelation.setAttributeCounter(3);
        assert testCorrelation.getAttributeCounter() == 3;
    }

    @Test
    public void testSetMaximum() {
        Correlation testCorrelation = new MutualCorrelation();
        testCorrelation.setMaximum(true);
        assert testCorrelation.getMaximum();
    }

    @Test
    public void testSetProperty() {
        Correlation testCorrelation = new Pearson();
        testCorrelation.setProperty("AverageDegree");
        assert testCorrelation.getProperty().equals("AverageDegree");
    }

    @Test
    public void testGetValidProperties() {
        String[] validProperties = MutualCorrelation.getValidProperties();
        HashSet<String> testSet = new HashSet<>();
        testSet.add("NumberOfEdges");
        testSet.add("SmallestDegree");
        testSet.add("Profile");
        testSet.add("Cliques");
        for (String current: validProperties) {
            testSet.remove(current);
        }
        if (testSet.size() != 2) {
            throw new AssertionError();
        }
        if (!testSet.contains("Profile") || !testSet.contains("Cliques")) {
            throw new AssertionError();
        }
    }

    @Test
    public  void testCutListMinimum() {
        CorrelationOutput output1 = new CorrelationOutput("AverageDegree", "StructureDensity", 2.43);
        CorrelationOutput output2 = new CorrelationOutput("LargestCliqueSize", "NumberOfVertices", 4.32);
        CorrelationOutput output3 = new CorrelationOutput("TotalColoringNumberOfColors", "NumberOfCliques", 1.89);
        CorrelationOutput output4 = new CorrelationOutput("VertexColoringNumberOfColors", "NumberOfDisjointVerticesFromKkGraph", 6.83);
        CorrelationOutput output5 = new CorrelationOutput("SmallestDegree", "NumberOfDisjointEdgesFromKkGraph", 8.71);
        TreeSet<CorrelationOutput> testSet = new TreeSet<>();
        testSet.add(output1);
        testSet.add(output2);
        testSet.add(output3);
        testSet.add(output4);
        testSet.add(output5);
        TreeSet<CorrelationOutput> outputSet = Correlation.cutListMinimum(testSet, 3);
        for (CorrelationOutput current: outputSet) {
            System.out.println(current.getFirstProperty());
        }
        assert outputSet.size() == 3;
        assert outputSet.contains(output1);
        assert outputSet.contains(output2);
        assert outputSet.contains(output3);
        for (CorrelationOutput current: outputSet) {
            System.out.println(current.getFirstProperty());
        }
    }
}
