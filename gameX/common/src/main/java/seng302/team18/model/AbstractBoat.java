package seng302.team18.model;

/**
 * Holds information about "boats", as conveyed in the AC35 protocol. Note that boats here means all boats (potentially
 * including marks, umpires and more), rather than just racing yachts.
 */
public abstract class AbstractBoat {
    private String nameShort;
    private String nameStowe;
    private String name;
    private String hullNumber;
    private Integer id;

    public AbstractBoat() {
    }


    public AbstractBoat(Integer id, String boatName, String shortName) {
        this.id = id;
        this.name = boatName;
        this.nameShort = shortName;
    }

    public abstract Coordinate getCoordinate();

    public abstract double getLength();

    public String getShortName() {
        return nameShort;
    }


    public void setShortName(String nameShort) {
        this.nameShort = nameShort;
    }


    public String getStoweName() {
        return nameStowe;
    }


    public void setStoweName(String nameStowe) {
        this.nameStowe = nameStowe;
    }


    public String getName() {
        return name;
    }


    public void setBoatName(String name) {
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


    public void setId(Integer id) {
        this.id = id;
    }


    public abstract BoatType getType();
}


