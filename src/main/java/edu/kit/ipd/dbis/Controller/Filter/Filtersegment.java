package edu.kit.ipd.dbis.Controller.Filter;

import java.io.Serializable;

/**
 * class Filtersegment allows to handle objects of class Filter and Filtergroup as
 * same objects
 */
public abstract class Filtersegment implements Serializable {

    protected String name;
    protected boolean isActivated;
    protected int id;

    /**
     * allows to enable a filtergroup or a filter. If a filter gets enabled, the criteria of
     * the filter are now used to filter graphs. If a filtergroup gets enabled every enabled
     * filter in this group is now used to filter graphs. Every filter of a filtergroup which
     * was disabled stays disabled even if the filtergroup gets enabled.
     */
    void activate() {
        this.isActivated = true;
    }

    /**
     * allows to disable a filtergroup or a filter. If a filter gets disabled, the criteria of
     * the filter are now ignored when filtering graphs. If a filtergroup gets disabled,
     * every filter of the group gets disabled even if the filter of the group are enabled.
     */
    void deactivate() {
        this.isActivated = false;
    }

    /**
     * sets a name for a specific filter. The name should be identical to the user input.
     * If the user wrote Kanten = 10, the name of the filtersegment should be Kanten
     * = 10
     * @param name name of the filtersegment
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * allows to get the name of a specific filtersegment
     * @return name of filtersegment
     */
    String getName() {
        return name;
    }

    /**
     * allows to get the identifier of a specific filtersegment
     * @return identifier of a specific filtersegment
     */
    int getID() {
        return id;
    }
}
