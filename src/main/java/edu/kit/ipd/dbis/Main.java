package edu.kit.ipd.dbis;


import edu.kit.ipd.dbis.Controller.Filter.Filtergroup;
import edu.kit.ipd.dbis.Controller.Filter.Filtermanagement;

public class Main {

    public static void main(String[] args) throws Exception {
        Filtermanagement manager = new Filtermanagement();
        Filtergroup myGroup = new Filtergroup("test", true, 1);
        manager.addFiltergroup(myGroup);
    }

}
