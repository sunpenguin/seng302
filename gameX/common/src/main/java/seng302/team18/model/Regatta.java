package seng302.team18.model;

/**
 * A class which stores information about a regatta.
 */
// TODO: Check if the class provides us any use. If not, can remove.
public class Regatta {

    private int RegattaID;
    private String RegattaName;

    public int getRegattaID() {
        return RegattaID;
    }

    public void setRegattaID(int regattaID) {
        RegattaID = regattaID;
    }

    public String getName() {
        return RegattaName;
    }

    public void setRegattaName(String regattaName) {
        RegattaName = regattaName;
    }
}
