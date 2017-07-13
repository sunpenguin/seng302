package seng302.team18.message;

/**
 * Element and Attribute names for the Race.xml of the AC35 Streaming Data Interface
 */
public enum AC35RaceXMLComponents {

    ROOT_RACE("Race"),

    ELEMENT_RACE_ID("RaceID"),
    ELEMENT_RACE_TYPE("RaceType"),
    ELEMENT_CREATION_TIME_DATE("CreationTimeDate"),
    ELEMENT_START_DATE_TIME("RaceStartTime"),
    ELEMENT_PARTICIPANTS("Participants"),
    ELEMENT_YACHT("Yacht"),
    ELEMENT_COURSE("Course"),
    ELEMENT_COMPOUND_MARK("CompoundMark"),
    ELEMENT_MARK("Mark"),
    ELEMENT_COMPOUND_MARK_SEQUENCE("CompoundMarkSequence"),
    ELEMENT_CORNER("Corner"),
    ELEMENT_COURSE_BOUNDARIES("CourseLimit"),
    ELEMENT_LIMIT("Limit"),

    ATTRIBUTE_START("Start"), // if program breaks change this to "Time"
    ATTRIBUTE_TIME("Time"),
    ATTRIBUTE_POSTPONE("Postpone"),
    ATTRIBUTE_SOURCE_ID("SourceID"),
    ATTRIBUTE_ENTRY("Entry"),
    ATTRIBUTE_COMPOUND_MARK_ID("CompoundMarkID"),
    ATTRIBUTE_NAME("Name"),
    ATTRIBUTE_SEQUENCE_ID("SeqID"),
    ATTRIBUTE_TARGET_LATITUDE("TargetLat"),
    ATTRIBUTE_TARGET_LONGITUDE("TargetLng"),
    ATTRIBUTE_ROUNDING("Rounding"),
    ATTRIBUTE_ZONE_SIZE("ZoneSize"),
    ATTRIBUTE_LATITUDE("Lat"),
    ATTRIBUTE_LONGITUDE("Lon");

    private final String value;

    AC35RaceXMLComponents(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
