package seng302.team18.test_mock.ac35_xml_encoding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.message.AC35XmlRegattaComponents;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

/**
 * Class to encode AC35XMLRegattaMessage.
 */
public class RegattaXmlEncoder extends XmlEncoder<AC35XMLRegattaMessage> {

    /**
     * Method used for generating a DOMSource from a AC35XMLRegattaMessage.
     * @param regattaMessage AC35XMLRegattaMessage, raceMessage
     * @return returns a DOMSource.
     * @throws ParserConfigurationException a ParserConfigurationException
     */
    @Override
    public DOMSource getDomSource(AC35XMLRegattaMessage regattaMessage) throws ParserConfigurationException {
        //Root
//        final String REGATTA_TAG = "RegattaConfig";
        Document doc = createDocument();
        Element root = doc.createElement(AC35XmlRegattaComponents.ROOT_REGATTA.toString());
        doc.appendChild(root);

        // REGATTA_ID
//        final String REGATTA_ID = "RegattaID";
        Element id = doc.createElement(AC35XmlRegattaComponents.ELEMENT_REGATTA_ID.toString());
        id.appendChild(doc.createTextNode(String.valueOf(regattaMessage.getId())));
        root.appendChild(id);

        // REGATTA_NAME
//        final String REGATTA_NAME = "RegattaName";
        Element regattaName = doc.createElement(AC35XmlRegattaComponents.ELEMENT_REGATTA_NAME.toString());
        regattaName.appendChild(doc.createTextNode(regattaMessage.getName()));
        root.appendChild(regattaName);

        // CENTER_LAT
//        final String CENTER_LAT = "CentralLatitude";
        Element centerLat = doc.createElement(AC35XmlRegattaComponents.ELEMENT_REGATTA_CENTER_LAT.toString());
        centerLat.appendChild(doc.createTextNode(String.valueOf(regattaMessage.getCentralLat())));
        root.appendChild(centerLat);

        // CENTER_LONG
//        final String CENTER_LONG = "CentralLongitude";
        Element centerLong = doc.createElement(AC35XmlRegattaComponents.ELEMENT_REGATTA_CENTER_LONG.toString());
        centerLong.appendChild(doc.createTextNode(String.valueOf(regattaMessage.getCentralLong())));
        root.appendChild(centerLong);

        // UTC_OFFSET
//        final String UTC_OFFSET = "UtcOffset";
        Element utcOffset = doc.createElement(AC35XmlRegattaComponents.ELEMENT_REGATTA_OFFSET.toString());
        utcOffset.appendChild(doc.createTextNode(regattaMessage.getUtcOffset()));
        root.appendChild(utcOffset);

        return new DOMSource(doc);
    }
}
