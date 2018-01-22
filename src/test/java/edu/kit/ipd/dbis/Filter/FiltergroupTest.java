package edu.kit.ipd.dbis.Filter;

import edu.kit.ipd.dbis.Filter.BasicFilter;
import edu.kit.ipd.dbis.Filter.Filter;
import edu.kit.ipd.dbis.Filter.Filtergroup;
import edu.kit.ipd.dbis.Filter.Relation;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.Property;
import edu.kit.ipd.dbis.org.jgrapht.additions.graph.properties.integer.GreatestDegree;
import org.junit.jupiter.api.Test;

public class FiltergroupTest {

    @Test
    public void addFilterToGroupTest() {
        Property myProperty = new GreatestDegree();
        Filtergroup myGroup = new Filtergroup("Testgroup", true, 1);
        Filter filterToAdd = new BasicFilter("Testfilter", true, 1, Relation.EQUAL, myProperty,2);
        myGroup.addFilter(filterToAdd);
        assert myGroup.availableFilter.contains(filterToAdd);
    }

    @Test
    public void removeFilterFromGroup() {
        Property myProperty = new GreatestDegree();
        Filtergroup myGroup = new Filtergroup("Testgroup", true, 1);
        Filter filterToAdd = new BasicFilter("Testfilter", true, 1, Relation.EQUAL, myProperty,2);
        myGroup.addFilter(filterToAdd);
        myGroup.removeFilter(filterToAdd.id);
        assert !myGroup.availableFilter.contains(filterToAdd);
    }
}
