package seng302.team18.test_mock.ac35_xml_encoding;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.message.AC35XMLRegattaMessage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Test for RegattaXmlEncoder.
 */
public class RegattaXmlEncoderTest {
    private AC35XMLRegattaMessage regattaMessage;
    private RegattaXmlEncoder regattaEncoder = new RegattaXmlEncoder();
    private Element root;


    /**
     * Create a new AC35 regatta message.
     */
    @Before
    public void setUp() {
        double centralLat = -36.8279;
        double centralLong = 174.812;
        String utcOffset = "12";
        int id = 3;
        String name = "New Zealand Test";

        regattaMessage = new AC35XMLRegattaMessage(id, name, centralLat, centralLong, utcOffset);
    }


    @Test
    public void RegattaXmlEncoderTest() throws ParserConfigurationException, TransformerException {
        DOMSource domSource = regattaEncoder.getDomSource(regattaMessage);
        Document doc = (Document) domSource.getNode();
        doc.getDocumentElement().normalize();

        final String REGATTA_TAG = "RegattaConfig";

        root = (Element) doc.getElementsByTagName(REGATTA_TAG).item(0);
    }

    @Test
    public void encodeIdTest() {
        final String REGATTA_ID = "RegattaID";

        int encodedId = Integer.parseInt(root.getElementsByTagName(REGATTA_ID).item(0).getTextContent());
        int id = regattaMessage.getId();
        assertEquals(id, encodedId);
    }

    @Test
    public void encodeNameTest() {
        final String REGATTA_NAME = "RegattaName";

        String encodedName = root.getElementsByTagName(REGATTA_NAME).item(0).getTextContent();
        String name = regattaMessage.getName();
        assertEquals(name, encodedName);
    }

    @Test
    public void encodeCenterLatTest() {
        final String CENTER_LAT = "CentralLatitude";

        double encodedLat = Double.parseDouble(root.getElementsByTagName(CENTER_LAT).item(0).getTextContent());
        double lat = regattaMessage.getCentralLat();
        assertEquals(lat, encodedLat);
    }

    @Test
    public void encodeCenterLongTest() {
        final String CENTER_LONG = "CentralLongitude";

        double encodedLon = Double.parseDouble(root.getElementsByTagName(CENTER_LONG).item(0).getTextContent());
        double lon = regattaMessage.getCentralLong();
        assertEquals(lon, encodedLon);
    }


    @Test
    public void encodeUtcOffsetTest() {
        final String UTC_OFFSET = "UtcOffset";

        String encodedOffset = root.getElementsByTagName(UTC_OFFSET).item(0).getTextContent();
        String offset = regattaMessage.getUtcOffset();
        assertEquals(offset, encodedOffset);
    }
}
