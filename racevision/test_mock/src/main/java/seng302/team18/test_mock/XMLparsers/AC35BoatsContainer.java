package seng302.team18.test_mock.XMLparsers;

import seng302.team18.model.Boat;

import java.util.List;

/**
 * A class contains list of boats.
 */
public class AC35BoatsContainer {
    private List<Boat> boats;

    /*
    Get list of boats.
     */
    public List<Boat> getBoats() {
        return boats;
    }

    /*
    Set the value of a list of boats.
     */
    public void setBoats(List<Boat> boats) {
        this.boats = boats;
    }
}
