package seng302.team18.racemodel.ac35_xml_encoding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.message.AC35XmlRegattaComponents;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

/**
 * Encodes a AC35XMLRegattaMessage into a XML-formatted string
 */
public class RegattaXmlEncoder extends XmlEncoder<AC35XMLRegattaMessage> {

    /**
     * Build a model of the XML structure from a AC35XMLRegattaMessage
     *
     * @param regattaMessage the message to build the XML structure from
     * @return the XML-structured message
     * @throws ParserConfigurationException if the XML structure cannot be created
     */
    @Override
    public DOMSource getDomSource(AC35XMLRegattaMessage regattaMessage) throws ParserConfigurationException {
        //Root
        Document doc = createDocument();
        Element root = doc.createElement(AC35XmlRegattaComponents.ROOT_REGATTA.toString());
        doc.appendChild(root);

        // REGATTA_ID
        Element id = doc.createElement(AC35XmlRegattaComponents.ELEMENT_REGATTA_ID.toString());
        id.appendChild(doc.createTextNode(String.valueOf(regattaMessage.getId())));
        root.appendChild(id);

        // REGATTA_NAME
        Element regattaName = doc.createElement(AC35XmlRegattaComponents.ELEMENT_REGATTA_NAME.toString());
        regattaName.appendChild(doc.createTextNode(regattaMessage.getRegattaName()));
        root.appendChild(regattaName);

        // REGATTA_NAME
        Element courseName = doc.createElement(AC35XmlRegattaComponents.ELEMENT_COURSE_NAME.toString());
        courseName.appendChild(doc.createTextNode(regattaMessage.getCourseName()));
        root.appendChild(courseName);

        // CENTER_LAT
        Element centerLat = doc.createElement(AC35XmlRegattaComponents.ELEMENT_REGATTA_CENTER_LAT.toString());
        centerLat.appendChild(doc.createTextNode(String.valueOf(regattaMessage.getCentralLat())));
        root.appendChild(centerLat);

        // CENTER_LONG
        Element centerLong = doc.createElement(AC35XmlRegattaComponents.ELEMENT_REGATTA_CENTER_LONG.toString());
        centerLong.appendChild(doc.createTextNode(String.valueOf(regattaMessage.getCentralLong())));
        root.appendChild(centerLong);

        // UTC_OFFSET
        Element utcOffset = doc.createElement(AC35XmlRegattaComponents.ELEMENT_REGATTA_OFFSET.toString());
        utcOffset.appendChild(doc.createTextNode(regattaMessage.getUtcOffset()));
        root.appendChild(utcOffset);

        return new DOMSource(doc);
    }
}
