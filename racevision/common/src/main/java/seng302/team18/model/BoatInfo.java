package seng302.team18.model;

/**
 * Holds information about "boats", as conveyed in the AC35 protocol. Note that boats here means all boats (potentially
 * including marks, umpires and more), rather than just racing yachts.
 */
public class BoatInfo {
    private String nameShort;
    private String nameStowe;
    private String name;
    private String hullNumber;
    private Integer id;
    private BoatType type = BoatType._UNKNOWN;

    public BoatInfo() {}

    public BoatInfo(Integer id, String boatName, String shortName) {
        this.id = id;
        this.name = boatName;
        this.nameShort = shortName;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public BoatType getType() {
        return type;
    }

    public void setType(BoatType type) {
        this.type = type;
    }
}


