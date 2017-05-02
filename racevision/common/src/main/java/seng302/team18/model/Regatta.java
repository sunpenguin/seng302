package seng302.team18.model;

/**
 * Created by Justin on 18/04/2017.
 */
public class Regatta {

    private int RegattaID;
    private String RegattaName;
    private String UTcOffset;

    public Regatta() {}

    public int getRegattaID() {
        return RegattaID;
    }

    public void setRegattaID(int regattaID) {
        RegattaID = regattaID;
    }

    public String getRegattaName() {
        return RegattaName;
    }

    public void setRegattaName(String regattaName) {
        RegattaName = regattaName;
    }

    public String getUTcOffset() {
        return UTcOffset;
    }

    public void setUTcOffset(String UTcOffset) {
        this.UTcOffset = UTcOffset;
    }
}
