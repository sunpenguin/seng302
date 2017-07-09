package seng302.team18.test_mock.ac35_xml_encoding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.Ac35XmlBoatComponents;
import seng302.team18.model.AbstractBoat;
import seng302.team18.model.BoatType;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Encodes a AC35XMLBoatMessage into a XML-formatted string
 */
public class BoatsXmlEncoder extends XmlEncoder<AC35XMLBoatMessage> {

    /**
     * Build a model of the XML structure from a AC35XMLBoatMessage
     *
     * @param boatMessage the message to build the XML structure from
     * @return the XML-structured message
     * @throws ParserConfigurationException if the XML structure cannot be created
     */
    public DOMSource getDomSource(AC35XMLBoatMessage boatMessage) throws ParserConfigurationException {
        final Integer DEFAULT_VERSION = 12;

        // Root
        Document doc = createDocument();
        Element root = doc.createElement(Ac35XmlBoatComponents.ROOT_BOATS.toString());
        doc.appendChild(root);

        // Modified
        Element modified = doc.createElement(Ac35XmlBoatComponents.ELEMENT_MODIFIED.toString());
        final String time = ZonedDateTime.now().format(DATE_TIME_FORMATTER);
        modified.appendChild(doc.createTextNode(time));
        root.appendChild(modified);

        // Version
        Element version = doc.createElement(Ac35XmlBoatComponents.ELEMENT_VERSION.toString());
        version.appendChild(doc.createTextNode(DEFAULT_VERSION.toString()));
        root.appendChild(version);

        // Settings
        root.appendChild(encodeSettings(doc));

        // Boat Shapes
        root.appendChild(encodeBoatShapes(doc));

        // Boats
        root.appendChild(encodeBoats(doc, boatMessage.getBoats()));

        return new DOMSource(doc);
    }


    private Element encodeSettings(Document doc) {
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

        Element settings = doc.createElement(Ac35XmlBoatComponents.ELEMENT_SETTINGS.toString());

        // Race Boat Type
        Element raceBoatType = doc.createElement(Ac35XmlBoatComponents.ELEMENT_RACE_BOAT_TYPE.toString());
        raceBoatType.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_TYPE.toString(), DEFAULT_RACE_BOAT_TYPE);
        settings.appendChild(raceBoatType);

        // Boat Dimension
        Element boatDimension = doc.createElement(Ac35XmlBoatComponents.ELEMENT_BOAT_DIMENSION.toString());
        boatDimension.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_BOAT_LENGTH.toString(), DEFAULT_BOAT_LENGTH.toString());
        boatDimension.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_HULL_LENGTH.toString(), DEFAULT_HULL_LENGTH.toString());
        settings.appendChild(boatDimension);

        // Zone Size
        Element zoneSize = doc.createElement(Ac35XmlBoatComponents.ELEMENT_ZONE_SIZE.toString());
        zoneSize.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_MARK_ZONE_SIZE.toString(), DEFAULT_MARK_ZONE_SIZE.toString());
        zoneSize.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_COURSE_ZONE_SIZE.toString(), DEFAULT_COURSE_ZONE_SIZE.toString());
        settings.appendChild(zoneSize);

        // Zone Limits
        Element zoneLimits = doc.createElement(Ac35XmlBoatComponents.ELEMENT_ZONE_LIMITS.toString());
        zoneLimits.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_1.toString(), DEFAULT_LIMIT_1.toString());
        zoneLimits.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_2.toString(), DEFAULT_LIMIT_2.toString());
        zoneLimits.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_3.toString(), DEFAULT_LIMIT_3.toString());
        zoneLimits.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_4.toString(), DEFAULT_LIMIT_4.toString());
        zoneLimits.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_5.toString(), DEFAULT_LIMIT_5.toString());
        settings.appendChild(zoneLimits);

        return settings;
    }


    private Element encodeBoatShapes(Document doc) {
        return doc.createElement(Ac35XmlBoatComponents.ELEMENT_BOAT_SHAPES.toString());
    }


    private Element encodeBoats(Document doc, List<AbstractBoat> boats) {
        Element element = doc.createElement(Ac35XmlBoatComponents.ELEMENT_BOATS.toString());

        for (AbstractBoat boat : boats) {
            element.appendChild(encodeBoat(doc, boat));
        }

        return element;
    }


    private Element encodeBoat(Document doc, AbstractBoat boat) {
        final Double DEFAULT_GPS_X = 0.001;
        final Double DEFAULT_GPS_Y = 0.625;
        final Double DEFAULT_GPS_Z = 1.738;
        final Double DEFAULT_FLAG_X = 0.000;
        final Double DEFAULT_FLAG_Y = 4.233;
        final Double DEFAULT_FLAG_Z = 21.496;
        final String DEFAULT_HULL_NUMBER = "AC4500";
        final String DEFAULT_STOWE_NAME = "ABC";

        Element element = doc.createElement(Ac35XmlBoatComponents.ELEMENT_BOAT.toString());
        element.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_SOURCE_ID.toString(), boat.getId().toString());
        element.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_TYPE.toString(), BoatType.YACHT.toString());
        element.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_HULL_NUMBER.toString(), DEFAULT_HULL_NUMBER);
        element.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_NAME_STOWE.toString(), DEFAULT_STOWE_NAME);
        element.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_NAME_SHORT.toString(), boat.getShortName());
        element.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_NAME_BOAT.toString(), boat.getName());

        Element gpsPosition = doc.createElement(Ac35XmlBoatComponents.ELEMENT_GPS_POSITION.toString());
        gpsPosition.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_Z.toString(), DEFAULT_GPS_Z.toString());
        gpsPosition.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_Y.toString(), DEFAULT_GPS_Y.toString());
        gpsPosition.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_X.toString(), DEFAULT_GPS_X.toString());
        element.appendChild(gpsPosition);

        Element flagPosition = doc.createElement(Ac35XmlBoatComponents.ELEMENT_FLAG_POSITION.toString());
        flagPosition.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_Z.toString(), DEFAULT_FLAG_Z.toString());
        flagPosition.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_Y.toString(), DEFAULT_FLAG_Y.toString());
        flagPosition.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_X.toString(), DEFAULT_FLAG_X.toString());
        element.appendChild(flagPosition);

        return element;
    }
}
