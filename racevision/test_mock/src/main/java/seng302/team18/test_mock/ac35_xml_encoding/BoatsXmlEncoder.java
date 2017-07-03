package seng302.team18.test_mock.ac35_xml_encoding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.Ac35XmlBoatComponents;
import seng302.team18.model.Boat;
import seng302.team18.model.Mark;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by afj19 on 1/07/17.
 */
public class BoatsXmlEncoder extends XmlEncoder<AC35XMLBoatMessage> {

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

    private Element encodeBoats(Document doc, List<Boat> boats) {
        Element element = doc.createElement(Ac35XmlBoatComponents.ELEMENT_BOATS.toString());

        for (Boat boat: boats) {
//            encodeBoat()
        }


        return element;
    }
}
