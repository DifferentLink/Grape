package edu.kit.ipd.dbis.Controller.Filter;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class FiltermanagementTest {

    @Test
    public void insertGroupToTable() throws Exception {
        Filtermanagement manager = new Filtermanagement();
        Filtergroup group = new Filtergroup("Testgroup", true, 1);
        try {
            manager.addFilterGroup(group);
            if (!manager.availableFilterGroups.contains(group)) throw new AssertionError();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertFilterToTable() throws Exception {
        Filtermanagement manager = new Filtermanagement();
        Filter filter = new BasicFilter("Testfilter", true, 1, Relation.GREATHEROREQUAL, null, 2);
        try {
            manager.addFilter(filter);
            if (!manager.availableFilter.contains(filter)) throw new AssertionError();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertFilterToGroupTable() throws Exception {
        Filtermanagement manager = new Filtermanagement();
        Filter filter = new BasicFilter("Testfilter", true, 1, Relation.EQUAL, null, 3);
        Filtergroup group = new Filtergroup("Testgroup", true, 4);
        try {
            manager.addFilterToFiltergroup(filter, 4);
            if (!manager.availableFilterGroups.contains(group)) throw new AssertionError();
            if (!group.availableFilter.contains(filter)) throw new AssertionError();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void removeFiltersegmentTest() throws Exception {
        Filtermanagement manager = new Filtermanagement();
        Filter standartFilter = new BasicFilter("Testfilter", true, 1, Relation.LESSOREQUAL, null,5);
        Filtergroup group = new Filtergroup("Testgroup", true, 6);
        Filter groupFilter = new BasicFilter("Testfilter", true, 1, Relation.LESSTHAN, null, 7);
        try {
            manager.addFilterGroup(group);
            manager.addFilter(standartFilter);
            manager.addFilterToFiltergroup(groupFilter, 6);
            manager.removeFiltersegment(5);
            manager.removeFiltersegment(6);
            manager.removeFiltersegment(7);
            if (group.availableFilter.size() != 0) throw new AssertionError();
            if (manager.availableFilterGroups.size() != 0) throw new AssertionError();
            if (manager.availableFilter.size() != 0) throw new AssertionError();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void activationTest() throws Exception {
        Filtermanagement manager = new Filtermanagement();
        Filter standartFilter = new BasicFilter("Testfilter", false, 1, Relation.LESSOREQUAL, null, 1);
        Filtergroup group = new Filtergroup("Testgroup", false, 2);
        Filter groupFilter = new BasicFilter("Testfilter", false, 1, Relation.EQUAL, null, 3);
        try {
            manager.addFilterGroup(group);
            manager.addFilter(standartFilter);
            manager.addFilterToFiltergroup(groupFilter, 2);
            manager.activate(2);
            manager.activate(1);
            manager.activate(3);
            if (!groupFilter.isActivated) throw new AssertionError();
            if (!standartFilter.isActivated) throw new AssertionError();
            if (!group.isActivated) throw new AssertionError();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deactivationTest() throws Exception {
        Filtermanagement manager = new Filtermanagement();
        Filter standartFilter = new BasicFilter("Testfilter", true, 1, Relation.EQUAL, null, 1);
        Filtergroup group = new Filtergroup("Testgroup", true, 2);
        Filter groupFilter = new BasicFilter("Testfilter", true, 1, Relation.LESSTHAN, null, 3);
        try {
            manager.addFilterGroup(group);
            manager.addFilter(standartFilter);
            manager.addFilterToFiltergroup(groupFilter, 2);
            manager.deactivate(2);
            manager.deactivate(1);
            manager.deactivate(3);
            if (groupFilter.isActivated) throw new AssertionError();
            if (standartFilter.isActivated) throw new AssertionError();
            if (group.isActivated) throw new AssertionError();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}