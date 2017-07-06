package seng302.team18.model;

/**
 * Holds information about "boats", as conveyed in the AC35 protocol. Note that boats here means all boats (potentially
 * including marks, umpires and more), rather than just racing yachts.
 */
public enum BoatType {
    _UNKNOWN(""),
    YACHT("Yacht"),
    RC("RC"),
    MARK("Mark"),
    UMPIRE("Umpire"),
    MARSHALL("Marshall"),
    PIN("Pin");

    /**
     * The value name of the boat type, as used in Boats.xml of the AC35 SDI protocol.
     */
    private final String typeName;

    BoatType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
