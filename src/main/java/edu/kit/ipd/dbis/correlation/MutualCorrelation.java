package edu.kit.ipd.dbis.correlation;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.TreeSet;

public class MutualCorrelation extends Correlation {

    @Override
    public TreeSet<CorrelationOutput> useMaximum() {
        return null;
    }

    @Override
    public TreeSet<CorrelationOutput> useMaximum(Property property1) {
        return null;
    }

    @Override
    public TreeSet<CorrelationOutput> useMinimum() {
        return null;
    }

    @Override
    public TreeSet<CorrelationOutput> useMinimum(Property property1) {
        return null;
    }
}
