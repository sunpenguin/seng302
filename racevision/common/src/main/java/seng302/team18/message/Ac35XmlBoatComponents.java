package seng302.team18.message;

/**
 * Element and Attribute names for the Boats.xml of the AC35 Streaming Data Interface
 */
public enum Ac35XmlBoatComponents {
    ROOT_BOATS("BoatConfig"),

    ELEMENT_MODIFIED("Modified"),
    ELEMENT_VERSION("Version"),
    ELEMENT_SETTINGS("Settings"),
    ELEMENT_RACE_BOAT_TYPE("RaceBoatType"),
    ELEMENT_BOAT_DIMENSION("BoatDimension"),
    ELEMENT_ZONE_SIZE("ZoneSize"),
    ELEMENT_ZONE_LIMITS("ZoneLimits"),
    ELEMENT_BOAT_SHAPES("BoatShapes"),
    ELEMENT_BOAT_SHAPE("BoatShape"),
    ELEMENT_VERTICES("Vertices"),
    ELEMENT_VERTEX("Vtx"),
    ELEMENT_CATAMARAN("Catamaran"),
    ELEMENT_BOWSPRIT("Bowsprit"),
    ELEMENT_TRAMPOLINE("Trampoline"),
    ELEMENT_BOATS("Boats"),
    ELEMENT_BOAT("Boat"),
    ELEMENT_GPS_POSITION("GPSposition"),
    ELEMENT_FLAG_POSITION("FlagPosition"),
    ELEMENT_MAST_TOP("MastTop"),

    ATTRIBUTE_TYPE("Type"),
    ATTRIBUTE_BOAT_LENGTH("BoatLength"),
    ATTRIBUTE_HULL_LENGTH("HullLength"),
    ATTRIBUTE_MARK_ZONE_SIZE("MarkZoneSize"),
    ATTRIBUTE_COURSE_ZONE_SIZE("CourseZoneSize"),
    ATTRIBUTE_LIMIT_1("Limit1"),
    ATTRIBUTE_LIMIT_2("Limit2"),
    ATTRIBUTE_LIMIT_3("Limit3"),
    ATTRIBUTE_LIMIT_4("Limit4"),
    ATTRIBUTE_LIMIT_5("Limit5"),
    ATTRIBUTE_SHAPE_ID("ShapeID"),
    ATTRIBUTE_SEQUENCE("Seq"),
    ATTRIBUTE_X("X"),
    ATTRIBUTE_Y("Y"),
    ATTRIBUTE_Z("Z"),
    ATTRIBUTE_BOAT_TYPE("Type"),
    ATTRIBUTE_SOURCE_ID("SourceID"),
    ATTRIBUTE_HULL_NUMBER("HullNumber"),
    ATTRIBUTE_NAME_STOWE("StoweName"),
    ATTRIBUTE_NAME_SHORT("ShortName"),
    ATTRIBUTE_NAME_BOAT("BoatName"),

    VALUE_YACHT("Yacht");


    private final String value;

    Ac35XmlBoatComponents(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
