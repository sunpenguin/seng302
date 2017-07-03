package seng302.team18.model;

/**
 * Holds information about "boats", as conveyed in the AC35 protocol. Note that boats here means all boats (potentially
 * including marks, umpires and more), rather than just racing yachts.
 */
public abstract class Boat {
    /* Fields for potential later inclusion:
     *
     *  Double gpsX;
     *  Double gpsY;
     *  Double gpsZ;
     *
     *  Double flagX;
     *  Double flagY;
     *  Double flagZ;
     *
     *  BoatShape shape;
     */

    private String nameShort = "";
    private String nameStowe = "";
    private String name = "";
    private String hullNumber = "";
    private final Integer id;

    protected Boat(Integer id, String boatName, String shortName) {
        this.id = id;
        this.name = boatName;
        this.nameShort = shortName;
    }

    protected Boat(Integer id) {
        this.id = id;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getNameStowe() {
        return nameStowe;
    }

    public void setNameStowe(String nameStowe) {
        this.nameStowe = nameStowe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHullNumber() {
        return hullNumber;
    }

    public void setHullNumber(String hullNumber) {
        this.hullNumber = hullNumber;
    }

    public Integer getId() {
        return id;
    }

    public abstract BoatType getType();
}


