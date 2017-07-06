package seng302.team18.test_mock.ac35_xml_encoding;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.message.AC35XmlRegattaComponents;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;

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

//        final String REGATTA_TAG = "RegattaConfig";

        root = (Element) doc.getElementsByTagName(AC35XmlRegattaComponents.ROOT_REGATTA.toString()).item(0);
    }

    @Test
    public void encodeIdTest() {
        System.out.println(root.getChildNodes());
        int encodedId = Integer.parseInt(root.getElementsByTagName(AC35XmlRegattaComponents.ELEMENT_REGATTA_ID.toString())
                .item(0).getTextContent());
        int id = regattaMessage.getId();
        assertEquals(id, encodedId);
    }

    @Test
    public void encodeNameTest() {
        String encodedName = root.getElementsByTagName(AC35XmlRegattaComponents.ELEMENT_REGATTA_NAME.toString())
                .item(0).getTextContent();
        String name = regattaMessage.getName();
        assertEquals(name, encodedName);
    }

    @Test
    public void encodeCenterLatTest() {
        double encodedLat = Double.parseDouble(root.getElementsByTagName(AC35XmlRegattaComponents.ELEMENT_REGATTA_CENTER_LAT.toString())
                .item(0).getTextContent());
        double lat = regattaMessage.getCentralLat();
        assertEquals(lat, encodedLat);
    }

    @Test
    public void encodeCenterLongTest() {
        double encodedLon = Double.parseDouble(root.getElementsByTagName(AC35XmlRegattaComponents.ELEMENT_REGATTA_CENTER_LONG.toString())
                .item(0).getTextContent());
        double lon = regattaMessage.getCentralLong();
        assertEquals(lon, encodedLon);
    }


    @Test
    public void encodeUtcOffsetTest() {
        String encodedOffset = root.getElementsByTagName(AC35XmlRegattaComponents.ELEMENT_REGATTA_OFFSET.toString())
                .item(0).getTextContent();
        String offset = regattaMessage.getUtcOffset();
        assertEquals(offset, encodedOffset);
    }
}
