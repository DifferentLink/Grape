package edu.kit.ipd.dbis.correlation;

import org.junit.*;

public class CorrelationOutputTest {

    @Test
    public void testCorrelationOutput() {
        CorrelationOutput testOutput = new CorrelationOutput("AverageDegree", "SmallestDegree", 4.44);
        assert testOutput.getFirstProperty().equals("AverageDegree");
        assert testOutput.getSecondProperty().equals("SmallestDegree");
        assert Math.abs(testOutput.getOutputNumber() - 4.44) < 0.01;
    }

    @Test
    public void testCompareTo() {
        CorrelationOutput firstOutput = new CorrelationOutput("BinomialDensity", "ProportionDensity", 8.63);
        CorrelationOutput secondOutput = new CorrelationOutput("NumberOfCliques", "NumberOfEdges", 6.93);
        assert firstOutput.compareTo(secondOutput) < 0;
        assert secondOutput.compareTo(firstOutput) > 0;
    }
}
