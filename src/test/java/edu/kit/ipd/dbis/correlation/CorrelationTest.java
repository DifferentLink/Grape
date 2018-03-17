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
        testCorrelation.setFirstArgument("min");
        assert testCorrelation.getFirstArgument().equals("min");
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
        if (testSet.contains("NumberOfEdges") || testSet.contains("SmallestDegree")) {
            throw new AssertionError();
        }
    }

    @Test
    public void testCutListMinimumNotEnoughValues() {
        CorrelationOutput output1 = new CorrelationOutput("AverageDegree", "StructureDensity", 2.43);
        CorrelationOutput output2 = new CorrelationOutput("LargestCliqueSize", "NumberOfVertices", 4.32);
        TreeSet<CorrelationOutput> testSet = new TreeSet<>();
        testSet.add(output1);
        testSet.add(output2);
        TreeSet<CorrelationOutput> outputSet = Correlation.cutListMinimum(testSet, 3);
        assert outputSet.size() == 2;
        int counter = 0;
        for (CorrelationOutput current: outputSet) {
            if (counter == 0) {
                assert current.getFirstProperty().equals("LargestCliqueSize");
                assert current.getSecondProperty().equals("NumberOfVertices");
                assert current.getOutputNumber() == 4.32;

            } else {
                assert current.getFirstProperty().equals("AverageDegree");
                assert current.getSecondProperty().equals("StructureDensity");
                assert current.getOutputNumber() == 2.43;
            }
            counter++;
        }
    }

    @Test
    public  void testCutListMinimum() {
        CorrelationOutput output1 = new CorrelationOutput("AverageDegree", "StructureDensity", 2.43);
        CorrelationOutput output2 = new CorrelationOutput("LargestCliqueSize", "NumberOfVertices", 4.32);
        CorrelationOutput output3 = new CorrelationOutput("TotalColoringNumberOfColors", "NumberOfCliques", 1.89);
        CorrelationOutput output4 = new CorrelationOutput("VertexColoringNumberOfColors", "NumberOfDisjointVerticesFromKkGraph", 6.83);
        CorrelationOutput output5 = new CorrelationOutput("SmallestDegree", "NumberOfDisjointEdgesFromKkGraph", 8.71);
        //first property is equal to second property --> correlation values are only interesting between different properties
        CorrelationOutput output7 = new CorrelationOutput("GreatestDegree", "GreatestDegree", 9.32);
        TreeSet<CorrelationOutput> testSet = new TreeSet<>();
        testSet.add(output1);
        testSet.add(output2);
        testSet.add(output3);
        testSet.add(output4);
        testSet.add(output5);
        testSet.add(output7);
        TreeSet<CorrelationOutput> outputSet = Correlation.cutListMinimum(testSet, 3);
        assert outputSet.size() == 3;
        int counter = 0;
        for (CorrelationOutput current: outputSet) {
            if (counter == 0) {
                assert current.getFirstProperty().equals("LargestCliqueSize");
                assert current.getSecondProperty().equals("NumberOfVertices");
                assert current.getOutputNumber() == 4.32;

            } else if (counter == 1) {
                assert current.getFirstProperty().equals("AverageDegree");
                assert current.getSecondProperty().equals("StructureDensity");
                assert current.getOutputNumber() == 2.43;
            } else {
                assert current.getFirstProperty().equals("TotalColoringNumberOfColors");
                assert current.getSecondProperty().equals("NumberOfCliques");
                assert current.getOutputNumber() == 1.89;
            }
            counter++;
        }
    }

    @Test
    public void testCutListMaximumNotEnoughValues() {
        CorrelationOutput output1 = new CorrelationOutput("SmallestDegree", "NumberOfDisjointEdgesFromKkGraph", 8.71);
        CorrelationOutput output2 = new CorrelationOutput("VertexColoringNumberOfColors", "NumberOfDisjointVerticesFromKkGraph", 6.83);
        TreeSet<CorrelationOutput> testSet = new TreeSet<>();
        testSet.add(output1);
        testSet.add(output2);
        TreeSet<CorrelationOutput> outputSet = Correlation.cutListMaximum(testSet, 3);
        assert outputSet.size() == 2;
        int counter = 0;
        for (CorrelationOutput current: outputSet) {
            if (counter == 0) {
                assert current.getFirstProperty().equals("SmallestDegree");
                assert current.getSecondProperty().equals("NumberOfDisjointEdgesFromKkGraph");
                assert current.getOutputNumber() == 8.71;

            } else {
                assert current.getFirstProperty().equals("VertexColoringNumberOfColors");
                assert current.getSecondProperty().equals("NumberOfDisjointVerticesFromKkGraph");
                assert current.getOutputNumber() == 6.83;
            }
            counter++;
        }
    }

    @Test
    public void testCutListMaximum() {
        CorrelationOutput output1 = new CorrelationOutput("AverageDegree", "StructureDensity", 2.43);
        CorrelationOutput output2 = new CorrelationOutput("LargestCliqueSize", "NumberOfVertices", 4.32);
        CorrelationOutput output3 = new CorrelationOutput("TotalColoringNumberOfColors", "NumberOfCliques", 1.89);
        CorrelationOutput output4 = new CorrelationOutput("VertexColoringNumberOfColors", "NumberOfDisjointVerticesFromKkGraph", 6.83);
        CorrelationOutput output5 = new CorrelationOutput("SmallestDegree", "NumberOfDisjointEdgesFromKkGraph", 8.71);
        //first property is equal to second property --> correlation values are only interesting between different properties
        CorrelationOutput output7 = new CorrelationOutput("GreatestDegree", "GreatestDegree", 9.32);
        TreeSet<CorrelationOutput> testSet = new TreeSet<>();
        testSet.add(output1);
        testSet.add(output2);
        testSet.add(output3);
        testSet.add(output4);
        testSet.add(output5);
        testSet.add(output7);
        TreeSet<CorrelationOutput> outputSet = Correlation.cutListMaximum(testSet, 3);
        assert outputSet.size() == 3;
        int counter = 0;
        for (CorrelationOutput current: outputSet) {
            if (counter == 0) {
                assert current.getFirstProperty().equals("SmallestDegree");
                assert current.getSecondProperty().equals("NumberOfDisjointEdgesFromKkGraph");
                assert current.getOutputNumber() == 8.71;

            } else if (counter == 1) {
                assert current.getFirstProperty().equals("VertexColoringNumberOfColors");
                assert current.getSecondProperty().equals("NumberOfDisjointVerticesFromKkGraph");
                assert current.getOutputNumber() == 6.83;
            } else {
                assert current.getFirstProperty().equals("LargestCliqueSize");
                assert current.getSecondProperty().equals("NumberOfVertices");
                assert current.getOutputNumber() == 4.32;
            }
            counter++;
        }
    }
}
