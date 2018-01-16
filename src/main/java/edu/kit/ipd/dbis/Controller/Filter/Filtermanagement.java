package edu.kit.ipd.dbis.Controller.Filter;

import edu.kit.ipd.dbis.org.jgrapht.additions.graph.PropertyGraph;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * class which communicates with other packages of Grpape
 */
public class Filtermanagement {

    private List<Filtergroup> availableFilterGroups;
    private List<Filter> availableFilter;

    public Filtermanagement() {
        availableFilterGroups = new ArrayList<>();
        availableFilter = new ArrayList<>();
    }

    public static Connection getConnection() throws java.sql.SQLException, java.lang.ClassNotFoundException {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/world";
        String username = "Collin Lorbeer";
        String password = "zwei2flaschen";
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connected.");
        return connection;
    }

    private static int addFilterGroupToDB(Filtergroup filtergroup) throws java.sql.SQLException, java.lang.ClassNotFoundException {
        int n = 0;
        Connection conn = getConnection();
        PreparedStatement create = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS graphs (id int NOT NULL AUTO_INCREMENT, filtergroup Filtergroup, PRIMARY KEY(id))");
        create.executeUpdate();
        PreparedStatement post = conn.prepareStatement("INSERT INTO graphs (filtergroup) VALUES (filtergroup)");
        post.executeUpdate();
        return n;
    }

    /**
     * adds a filtergroup to the list availableFilterGroups of class Filtermanagement
     * @param filtergroup filtersegment which should be added
     */
    public void addFiltergroup(Filtergroup filtergroup) throws java.sql.SQLException, java.lang.ClassNotFoundException {
        availableFilterGroups.add(filtergroup);
        Filtermanagement.addFilterGroupToDB(filtergroup);
    }

    /**
     * adds a filter to the list availableFilter of class Filtermanagement
     * @param filter filtersegment which should be added
     */
    public void addFilter(Filter filter) {
        availableFilter.add(filter);
    }

    /**
     * removes a filtersegment out of the list of class Filtermanagement
     * @param id unique identifier of the filtersegment which should be removed
     */
    public void removeFiltersegment(int id) {
        for (Filtersegment element: availableFilter) {
            if (element.id == id) {
                availableFilter.remove(element);
                return;
            }
        }
        for (Filtergroup element: availableFilterGroups) {
            if (element.id == id) {
                availableFilterGroups.remove(element);
                return;
            }
            element.removeFilter(id);
        }
    }

    /**
     * enables a filtersegment which means that the criteria of the fitersegment are now
     * used to filter graphs
     * @param id unique identifier of the filtersegment which should be enabled

     */
    public void activate(int id) {
        for (Filtersegment element: availableFilter) {
            if (element.id == id) {
                element.activate();
                return;
            }
        }
        for (Filtergroup element: availableFilterGroups) {
            if (element.id == id) {
                element.activate();
            }
            for (Filter currentFilter: element.availableFilter) {
                if (currentFilter.id == id) {
                    currentFilter.activate();
                }
            }
        }
    }

    /**
     * disables a filtersegment which means that the criteria of the filtersegment are
     * ignored while filtering graphs
     * @param id unique identifier of the filtersegment which should be disabled
     */
    public void deactivate(int id) {
            for (Filter element: availableFilter) {
                if (element.id == id) {
                    element.deactivate();
                    return;
                }
            }
            for (Filtergroup currentElement: availableFilterGroups) {
                if (currentElement.id == id) {
                    currentElement.deactivate();
                }
                for (Filter currentFilter : currentElement.availableFilter) {
                    if (currentFilter.id == id) {
                        currentFilter.deactivate();
                    }
                }
            }

    }

    /**
     * allows to prevent a NullPointerException by asking if there is a graph to return
     * @return returns true if there is a graph which meets the current filter criteria
     * but was not returned yet
     */
    public boolean hasNextValidGraph() {
        return false;
    }

    /**
     * allows to get only graphs from database which meet the current filter criteria
     * @return graph which meets all current filter criteria
     * @throws NullPointerException this exception is thrown if all graphs of database
     * which meet the current criteria were already returned
     */
    public PropertyGraph getNextValidGraph() throws NullPointerException {
        return null;
    }

    /**
     * checks whether the input string codes a valid filter. In case of success the method
     * addFiltersegment(filtersegment: Filtersegment): void is called and a new
     * filter is added to the list of class Filtermanagement
     * @param input string which might code a filter
     * @param id unique identifier of the new filterobject
     * @throws InvalidInputException this exception is thrown if the input string
     * does not code a valid filter
     * @throws DoubledIdentifierException this exception is thrown if there is already
     * a filter with same identifier
     */
    public void checkFilterInput(String input, int id) throws DoubledIdentifierException,
            InvalidInputException {

    }

    /**
     * checks whether the input string codes a valid Filter. In case of success the method
     * addFiltersegment(filtersegment: Filtersegment): void is called and a new
     * Filter is added to the list of class Filtermanagement
     * @param input string which might code a Filter
     * @param id unique identifier of the new Filterobject
     */
    public void checkGroupInput(String input, int id) {

    }

    /**
     * used when initializing Grape or switching a database. The methode clears the current
     * list of Filtersegments and calls the methode addFiltersegment(filtersegment:
     * Filtersegment): void for every Filter element of the new database
     * @param database new database which should be used in future
     */
    /* public void switchDB(Database database) {

    } */
}
