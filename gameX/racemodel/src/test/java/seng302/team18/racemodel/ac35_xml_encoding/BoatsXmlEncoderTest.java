package seng302.team18.racemodel.ac35_xml_encoding;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.Ac35XmlBoatComponents;
import seng302.team18.model.AbstractBoat;
import seng302.team18.model.Boat;
import seng302.team18.model.BoatType;

import javax.xml.transform.dom.DOMSource;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoatsXmlEncoderTest {

    private Element root;
    private AC35XMLBoatMessage message;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    private LocalTime beforeEncoding;
    private LocalTime afterEncoding;


    private void setUpMessage() {
        final int DEFAULT_VERSION = 12;
        final String DEFAULT_RACE_BOAT_TYPE = "AC45";
        final Double DEFAULT_BOAT_LENGTH = 14.019d;
        final Double DEFAULT_HULL_LENGTH = 13.449d;
        final Double DEFAULT_MARK_ZONE_SIZE = 40.347d;
        final Double DEFAULT_COURSE_ZONE_SIZE = 40.347d;
        final Double DEFAULT_LIMIT_1 = 200d;
        final Double DEFAULT_LIMIT_2 = 100d;
        final Double DEFAULT_LIMIT_3 = 40.347;
        final Double DEFAULT_LIMIT_4 = 0d;
        final Double DEFAULT_LIMIT_5 = -100d;
        final String DEFAULT_HULL_NUMBER = "AC4500";
        final String DEFAULT_STOWE_NAME = "ABC";
        final Double DEFAULT_GPS_X = 0.001;
        final Double DEFAULT_GPS_Y = 0.625;
        final Double DEFAULT_GPS_Z = 1.738;
        final Double DEFAULT_FLAG_X = 0.000;
        final Double DEFAULT_FLAG_Y = 4.233;
        final Double DEFAULT_FLAG_Z = 21.496;


        List<AbstractBoat> boats = Arrays.asList(
                new Boat("Zeroth Boat", "B0", 110, 14.019),
                new Boat("First Boat", "B1", 111, 14.019),
                new Boat("Second Boat", "B2", 112, 14.019),
                new Boat("Third Boat", "B3", 113, 14.019),
                new Boat("Fourth Boat", "B4", 114, 14.019)
        );

        boats.forEach(boat -> {
            boat.setHullNumber(DEFAULT_HULL_NUMBER);
            boat.setStoweName(DEFAULT_STOWE_NAME);
        });

        message = new AC35XMLBoatMessage(boats);

        message.setVersion(DEFAULT_VERSION);
        message.setRaceBoatType(DEFAULT_RACE_BOAT_TYPE);
        message.setBoatLength(DEFAULT_BOAT_LENGTH);
        message.setHullLength(DEFAULT_HULL_LENGTH);
        message.setMarkZoneSize(DEFAULT_MARK_ZONE_SIZE);
        message.setCourseZoneSize(DEFAULT_COURSE_ZONE_SIZE);

        message.setLimit1(DEFAULT_LIMIT_1);
        message.setLimit2(DEFAULT_LIMIT_2);
        message.setLimit3(DEFAULT_LIMIT_3);
        message.setLimit4(DEFAULT_LIMIT_4);
        message.setLimit5(DEFAULT_LIMIT_5);

        message.setDefaultGpsX(DEFAULT_GPS_X);
        message.setDefaultGpsY(DEFAULT_GPS_Y);
        message.setDefaultGpsZ(DEFAULT_GPS_Z);

        message.setDefaultFlagX(DEFAULT_FLAG_X);
        message.setDefaultFlagY(DEFAULT_FLAG_Y);
        message.setDefaultFlagZ(DEFAULT_FLAG_Z);
    }


    @Before
    public void setUp() throws Exception {
        setUpMessage();
        beforeEncoding = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        DOMSource domSource = (new BoatsXmlEncoder()).getDomSource(message);
        afterEncoding = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);

        Document doc = (Document) domSource.getNode();
        doc.getDocumentElement().normalize();

        root = (Element) doc.getElementsByTagName(Ac35XmlBoatComponents.ROOT_BOATS.toString()).item(0);
    }


    @Test
    public void encodeModifiedTimeTest() throws Exception {
        final Element elementModified = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_MODIFIED.toString()).item(0);
        final LocalTime modifiedTime = LocalTime.parse(elementModified.getTextContent(), dateTimeFormatter);
        boolean isAfterBefore = modifiedTime.equals(beforeEncoding) || modifiedTime.isAfter(beforeEncoding);
        boolean isBeforeAfter = modifiedTime.equals(afterEncoding) || modifiedTime.isBefore(afterEncoding);
        assertTrue("xml creation time has not been correctly encoded", isAfterBefore && isBeforeAfter);
    }



    @Test
    public void encodeVersionTest() throws Exception {
        final Element elementVersion = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_VERSION.toString()).item(0);
        final String version = elementVersion.getTextContent();
        assertEquals("version has not been correctly encoded", message.getVersion(), Integer.parseInt(version));
    }


    @Test
    public void encodeSettingsTest_raceBoatType() throws Exception {
        final Element elementSettings = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_SETTINGS.toString()).item(0);
        final Element elementType = (Element) elementSettings.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_RACE_BOAT_TYPE.toString()).item(0);
        final String type = elementType.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_TYPE.toString());

        assertEquals("race boat type has not been encoded correctly", message.getRaceBoatType(), type);
    }


    @Test
    public void encodeSettingsTest_boatDimension() throws Exception {
        final Element elementSettings = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_SETTINGS.toString()).item(0);
        final Element elementDimension = (Element) elementSettings.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOAT_DIMENSION.toString()).item(0);

        final String boatLength = elementDimension.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_BOAT_LENGTH.toString());
        final String hullLength = elementDimension.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_HULL_LENGTH.toString());

        assertEquals("boat dimension - boat length has not been encoded correctly", ((Double) message.getBoatLength()).toString(), boatLength);
        assertEquals("boat dimension - hull length has not been encoded correctly", ((Double) message.getHullLength()).toString(), hullLength);
    }


    @Test
    public void encodeSettingsTest_ZoneSize() throws Exception {
        final Element elementSettings = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_SETTINGS.toString()).item(0);
        final Element elementZoneSize = (Element) elementSettings.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_ZONE_SIZE.toString()).item(0);

        final String markZoneSize = elementZoneSize.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_MARK_ZONE_SIZE.toString());
        final String courseZoneSize = elementZoneSize.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_COURSE_ZONE_SIZE.toString());

        assertEquals("mark zone size has not been encoded correctly", ((Double) message.getMarkZoneSize()).toString(), markZoneSize);
        assertEquals("course zone size has not been encoded correctly", ((Double) message.getCourseZoneSize()).toString(), courseZoneSize);
    }


    @Test
    public void encodeSettingsTest_ZoneLimits() throws Exception {
        final Element elementSettings = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_SETTINGS.toString()).item(0);
        final Element elementZoneLimits = (Element) elementSettings.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_ZONE_LIMITS.toString()).item(0);

        final String limit1 = elementZoneLimits.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_1.toString());
        final String limit2 = elementZoneLimits.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_2.toString());
        final String limit3 = elementZoneLimits.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_3.toString());
        final String limit4 = elementZoneLimits.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_4.toString());
        final String limit5 = elementZoneLimits.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_5.toString());

        assertEquals("zone limit 1 has not been encoded correctly", ((Double) message.getLimit1()).toString(), limit1);
        assertEquals("zone limit 2 has not been encoded correctly", ((Double) message.getLimit2()).toString(), limit2);
        assertEquals("zone limit 3 has not been encoded correctly", ((Double) message.getLimit3()).toString(), limit3);
        assertEquals("zone limit 4 has not been encoded correctly", ((Double) message.getLimit4()).toString(), limit4);
        assertEquals("zone limit 5 has not been encoded correctly", ((Double) message.getLimit5()).toString(), limit5);
    }


    @Test
    public void encodeBoatTest_type() throws Exception {
        final Element elementBoats = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOATS.toString()).item(0);
        final NodeList boatNodes = elementBoats.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOAT.toString());

        for (int i = 0; i < boatNodes.getLength(); i++) {
            Element elementBoat = (Element) boatNodes.item(i);
            String type = elementBoat.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_TYPE.toString());
            assertEquals(String.format("type for boat %d has not been encoded correctly", i), BoatType.YACHT.toString(), type);
        }
    }


    @Test
    public void encodeBoatTest_sourceId() throws Exception {
        final Element elementBoats = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOATS.toString()).item(0);
        final NodeList boatNodes = elementBoats.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOAT.toString());

        for (int i = 0; i < boatNodes.getLength(); i++) {
            Element elementBoat = (Element) boatNodes.item(i);

            String encodedId = elementBoat.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_SOURCE_ID.toString());
            String expectedId = message.getBoats().get(i).getId().toString();

            assertEquals(String.format("sourceId for boat %d has not been encoded correctly", i), expectedId, encodedId);
        }
    }


    @Test
    public void encodeBoatTest_hullNumber() throws Exception {
        final Element elementBoats = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOATS.toString()).item(0);
        final NodeList boatNodes = elementBoats.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOAT.toString());

        for (int i = 0; i < boatNodes.getLength(); i++) {
            Element elementBoat = (Element) boatNodes.item(i);
            String encodedHullId = elementBoat.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_HULL_NUMBER.toString());
            assertEquals(String.format("hull number for boat %d has not been encoded correctly", i), message.getBoats().get(i).getHullNumber(), encodedHullId);
        }
    }


    @Test
    public void encodeBoatTest_stoweName() throws Exception {
        final Element elementBoats = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOATS.toString()).item(0);
        final NodeList boatNodes = elementBoats.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOAT.toString());

        for (int i = 0; i < boatNodes.getLength(); i++) {
            Element elementBoat = (Element) boatNodes.item(i);
            String encodedStoweName = elementBoat.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_NAME_STOWE.toString());
            assertEquals(String.format("stowe name for boat %d has not been encoded correctly", i), message.getBoats().get(i).getStoweName(), encodedStoweName);
        }
    }


    @Test
    public void encodeBoatTest_shortName() throws Exception {
        final Element elementBoats = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOATS.toString()).item(0);
        final NodeList boatNodes = elementBoats.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOAT.toString( ));

        for (int i = 0; i < boatNodes.getLength(); i++) {
            Element elementBoat = (Element) boatNodes.item(i);

            String encodedShortName = elementBoat.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_NAME_SHORT.toString());
            String expectedShortName = message.getBoats().get(i).getShortName();

            assertEquals(String.format("short name for boat %d has not been encoded correctly", i), expectedShortName, encodedShortName);
        }
    }


    @Test
    public void encodeBoatTest_boatName() throws Exception {
        final Element elementBoats = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOATS.toString()).item(0);
        final NodeList boatNodes = elementBoats.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOAT.toString());

        for (int i = 0; i < boatNodes.getLength(); i++) {
            Element elementBoat = (Element) boatNodes.item(i);

            String encodedName = elementBoat.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_NAME_BOAT.toString());
            String expectedName = message.getBoats().get(i).getName();

            assertEquals(String.format("name for boat %d has not been encoded correctly", i), expectedName, encodedName);
        }
    }


    @Test
    public void encodeBoatTest_colour() throws Exception {
        final Element elementBoats = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOATS.toString()).item(0);
        final NodeList boatNodes = elementBoats.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOAT.toString());

        for (int i = 0; i < boatNodes.getLength(); i++) {
            Element elementBoat = (Element) boatNodes.item(i);

            String encodedColour = elementBoat.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_COLOUR.toString());
            String expectedColourLong =  message.getBoats().get(i).getColour().toString();
            String expectedColour = expectedColourLong.substring(0, expectedColourLong.length() - 2);

            assertEquals(String.format("Colour for boat %d has not been encoded correctly", i), expectedColour, encodedColour);
        }
    }


    @Test
    public void encodeBoatTest_gps() throws Exception {
        final Element elementBoats = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOATS.toString()).item(0);
        final NodeList boatNodes = elementBoats.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOAT.toString());

        for (int i = 0; i < boatNodes.getLength(); i++) {
            Element elementBoat = (Element) boatNodes.item(i);
            Element elementGps = (Element) elementBoat.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_GPS_POSITION.toString()).item(0);

            String encodedX = elementGps.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_X.toString());
            String encodedY = elementGps.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_Y.toString());
            String encodedZ = elementGps.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_Z.toString());

            assertEquals(String.format("gps x for boat %d has not been encoded correctly", i), ((Double) message.getDefaultGpsX()).toString(), encodedX);
            assertEquals(String.format("gps y for boat %d has not been encoded correctly", i), ((Double) message.getDefaultGpsY()).toString(), encodedY);
            assertEquals(String.format("gps z for boat %d has not been encoded correctly", i), ((Double) message.getDefaultGpsZ()).toString(), encodedZ);
        }
    }


    @Test
    public void encodeBoatTest_flag() throws Exception {
        final Element elementBoats = (Element) root.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOATS.toString()).item(0);
        final NodeList boatNodes = elementBoats.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOAT.toString());

        for (int i = 0; i < boatNodes.getLength(); i++) {
            Element elementBoat = (Element) boatNodes.item(i);
            Element elementFlag = (Element) elementBoat.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_FLAG_POSITION.toString()).item(0);

            String encodedX = elementFlag.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_X.toString());
            String encodedY = elementFlag.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_Y.toString());
            String encodedZ = elementFlag.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_Z.toString());

            assertEquals(String.format("flag x for boat %d has not been encoded correctly", i), ((Double) message.getDefaultFlagX()).toString(), encodedX);
            assertEquals(String.format("flag y for boat %d has not been encoded correctly", i), ((Double) message.getDefaultFlagY()).toString(), encodedY);
            assertEquals(String.format("flag z for boat %d has not been encoded correctly", i), ((Double) message.getDefaultFlagZ()).toString(), encodedZ);
        }
    }
}