package seng302.team18.message;

/**
 * Components for AC35 XML Regatta message.
 */
public enum  AC35XmlRegattaComponents {
    ROOT_REGATTA("RegattaConfig"),

    ELEMENT_REGATTA_ID("RegattaID"),
    ELEMENT_REGATTA_NAME("RegattaName"),
    ELEMENT_REGATTA_CENTER_LAT("CentralLatitude"),
    ELEMENT_REGATTA_CENTER_LONG("CENTER_LONG"),
    ELEMENT_REGATTA_OFFSET("UtcOffset");

    private final String value;

    AC35XmlRegattaComponents(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
