package seng302.team18.racemodel.ac35_xml_encoding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.Ac35XmlBoatComponents;
import seng302.team18.message.XmlMessage;
import seng302.team18.model.AbstractBoat;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import java.time.ZonedDateTime;

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
        // Root
        Document doc = createDocument();
        Element root = doc.createElement(Ac35XmlBoatComponents.ROOT_BOATS.toString());
        doc.appendChild(root);

        // Modified
        Element modified = doc.createElement(Ac35XmlBoatComponents.ELEMENT_MODIFIED.toString());
        final String time = ZonedDateTime.now().format(XmlMessage.DATE_TIME_FORMATTER);
        modified.appendChild(doc.createTextNode(time));
        root.appendChild(modified);

        // Version
        Element version = doc.createElement(Ac35XmlBoatComponents.ELEMENT_VERSION.toString());
        version.appendChild(doc.createTextNode(((Integer) boatMessage.getVersion()).toString()));
        root.appendChild(version);

        // Settings
        root.appendChild(encodeSettings(doc, boatMessage));

        // Boat Shapes
        root.appendChild(encodeBoatShapes(doc));

        // Boats
        root.appendChild(encodeBoats(doc, boatMessage));

        return new DOMSource(doc);
    }


    private Element encodeSettings(Document doc, AC35XMLBoatMessage boatMessage) {

        Element settings = doc.createElement(Ac35XmlBoatComponents.ELEMENT_SETTINGS.toString());

        // Race Boat Type
        Element raceBoatType = doc.createElement(Ac35XmlBoatComponents.ELEMENT_RACE_BOAT_TYPE.toString());
        raceBoatType.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_TYPE.toString(), boatMessage.getRaceBoatType());
        settings.appendChild(raceBoatType);

        // Boat Dimension
        Element boatDimension = doc.createElement(Ac35XmlBoatComponents.ELEMENT_BOAT_DIMENSION.toString());
        boatDimension.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_BOAT_LENGTH.toString(), ((Double) boatMessage.getBoatLength()).toString());
        boatDimension.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_HULL_LENGTH.toString(), ((Double) boatMessage.getHullLength()).toString());
        settings.appendChild(boatDimension);

        // Zone Size
        Element zoneSize = doc.createElement(Ac35XmlBoatComponents.ELEMENT_ZONE_SIZE.toString());
        zoneSize.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_MARK_ZONE_SIZE.toString(), ((Double) boatMessage.getMarkZoneSize()).toString());
        zoneSize.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_COURSE_ZONE_SIZE.toString(), ((Double) boatMessage.getCourseZoneSize()).toString());
        settings.appendChild(zoneSize);

        // Zone Limits
        Element zoneLimits = doc.createElement(Ac35XmlBoatComponents.ELEMENT_ZONE_LIMITS.toString());
        zoneLimits.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_1.toString(), ((Double) boatMessage.getLimit1()).toString());
        zoneLimits.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_2.toString(), ((Double) boatMessage.getLimit2()).toString());
        zoneLimits.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_3.toString(), ((Double) boatMessage.getLimit3()).toString());
        zoneLimits.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_4.toString(), ((Double) boatMessage.getLimit4()).toString());
        zoneLimits.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_LIMIT_5.toString(), ((Double) boatMessage.getLimit5()).toString());
        settings.appendChild(zoneLimits);

        return settings;
    }


    private Element encodeBoatShapes(Document doc) {
        return doc.createElement(Ac35XmlBoatComponents.ELEMENT_BOAT_SHAPES.toString());
    }


    private Element encodeBoats(Document doc, AC35XMLBoatMessage boatMessage) {
        Element element = doc.createElement(Ac35XmlBoatComponents.ELEMENT_BOATS.toString());

        for (AbstractBoat boat : boatMessage.getBoats()) {
            element.appendChild(encodeBoat(doc, boatMessage, boat));
        }

        return element;
    }


    private Element encodeBoat(Document doc, AC35XMLBoatMessage boatMessage, AbstractBoat boat) {
        final int ALPHA_VALUE_LENGTH = 2;

        Element element = doc.createElement(Ac35XmlBoatComponents.ELEMENT_BOAT.toString());
        element.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_SOURCE_ID.toString(), boat.getId().toString());
        element.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_TYPE.toString(), boat.getType().toString());
        element.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_HULL_NUMBER.toString(), boat.getHullNumber());
        element.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_NAME_STOWE.toString(), boat.getStoweName());
        element.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_NAME_SHORT.toString(), boat.getShortName());
        element.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_NAME_BOAT.toString(), boat.getName());
        String color = boat.getColour().toString();
        color = color.substring(0, color.length() - ALPHA_VALUE_LENGTH);
        element.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_COLOUR.toString(), color);

        Element gpsPosition = doc.createElement(Ac35XmlBoatComponents.ELEMENT_GPS_POSITION.toString());
        gpsPosition.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_Z.toString(), ((Double) boatMessage.getDefaultGpsZ()).toString());
        gpsPosition.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_Y.toString(), ((Double) boatMessage.getDefaultGpsY()).toString());
        gpsPosition.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_X.toString(), ((Double) boatMessage.getDefaultGpsX()).toString());
        element.appendChild(gpsPosition);

        Element flagPosition = doc.createElement(Ac35XmlBoatComponents.ELEMENT_FLAG_POSITION.toString());
        flagPosition.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_Z.toString(), ((Double) boatMessage.getDefaultFlagZ()).toString());
        flagPosition.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_Y.toString(), ((Double) boatMessage.getDefaultFlagY()).toString());
        flagPosition.setAttribute(Ac35XmlBoatComponents.ATTRIBUTE_X.toString(), ((Double) boatMessage.getDefaultFlagX()).toString());
        element.appendChild(flagPosition);

        return element;
    }
}
