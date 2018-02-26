package edu.kit.ipd.dbis.filter;

import java.io.Serializable;

/**
 * class Filtersegment allows to handle objects of class filter and Filtergroup as
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
    public void activate() {
        this.isActivated = true;
    }

    /**
     * allows to disable a filtergroup or a filter. If a filter gets disabled, the criteria of
     * the filter are now ignored when filtering graphs. If a filtergroup gets disabled,
     * every filter of the group gets disabled even if the filter of the group are enabled.
     */
    public void deactivate() {
        this.isActivated = false;
    }

    /**
     * allows to get the name of a specific filtersegment
     * @return name of filtersegment
     */
    public String getName() {
        return name;
    }

    /**
     * allows to get the identifier of a specific filtersegment
     * @return identifier of a specific filtersegment
     */
    public int getID() {
        return id;
    }

    /**
     * allows to check if a specific filtersegment is activated
     * @return returns true if the filtersegment is activated
     */
    public boolean getIsActivated() {
        return isActivated;
    }
}
