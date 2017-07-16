package seng302.team18.test_mock;

/**
 * Enum of file paths for xml files to be used by the test mock
 */
public enum TestXMLFiles {
    REGATTA_XML_1("/regatta_test1.xml"),
    BOATS_XML_2("/boats_test2.xml"),
    RACE_XML_2("/race_test2.xml");

    private final String text;

    TestXMLFiles(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
