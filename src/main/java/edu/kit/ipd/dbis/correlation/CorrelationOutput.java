package edu.kit.ipd.dbis.correlation;

/**
 * class which allows the GUI to get a list of relevant correlations
 */
public class CorrelationOutput implements Comparable<CorrelationOutput> {
    private String firstProperty;
    private String secondProperty;
    private double outputNumber;

    /**
     * Constructor of class CorelationOutput
     * @param firstProperty first property of a specific correlation
     * @param secondProperty second property of a specific correlation
     * @param outputNumber double value which shows how strong a specific correlation is
     */
    CorrelationOutput(String firstProperty, String secondProperty, double outputNumber) {
        this.firstProperty = firstProperty;
        this.secondProperty = secondProperty;
        this.outputNumber = outputNumber;
    }

    /**
     * used to get the first property of a specific correlation
     * @return retuns the first property of a specific correlation
     */
    public String getFirstProperty() {
        return firstProperty;
    }

    /**
     * used to get the second property of a specific correlation
     * @return retuns the second property of a specific correlation
     */
    public String getSecondProperty() {
        return secondProperty;
    }

    /**
     * used to get a number which shows how strong the correlation is
     * @return retuns a number which shows how strong a specific correlation is
     */
    public double getOutputNumber() {
        return outputNumber;
    }

    @Override
    public int compareTo(CorrelationOutput other) {
        if (this.getOutputNumber() < other.getOutputNumber()) {
            return 1;
        }
        return -1;
    }
}
