package seng302.team18.messageparsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.Ac35XmlBoatComponents;
import seng302.team18.model.Boat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * The parser that will parse AC35 XML Boat message bodies.
 */
public class AC35XMLBoatParser implements MessageBodyParser {

    /**
     * Parse an input stream to create a boat message holding boat information.
     *
     * @param stream The input stream from the data source.
     * @return A message object holding the boat information
     */
    @Override
    public AC35XMLBoatMessage parse(InputStream stream) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(stream);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        }
        doc.getDocumentElement().normalize();
        Element boatsElement = (Element) doc.getElementsByTagName(Ac35XmlBoatComponents.ROOT_BOATS.toString()).item(0);

        Node boatsNode = boatsElement.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOATS.toString()).item(0);
        List<Boat> boats = parseBoats(boatsNode);

        return new AC35XMLBoatMessage(boats);
    }


    /**
     * Converts a byte array to a data stream which can be passed to the other parse method.
     *
     * @param bytes An array of bytes
     * @return A boat message returned by the other parse method.
     */
    @Override
    public AC35XMLBoatMessage parse(byte[] bytes) {
        InputStream stream = new ByteArrayInputStream(new String(bytes, StandardCharsets.UTF_8).trim().getBytes());
        return parse(stream);
    }

    /**
     * Used in the parse method to parse boats from a boat element in the boat xml file.
     *
     * @param boatsNode the noe of the boats element
     * @return A list of participating boats.
     */
    private List<Boat> parseBoats(Node boatsNode) {

        List<Boat> boats = new ArrayList<>();
        if (boatsNode.getNodeType() == Node.ELEMENT_NODE) {
            Element boatSequenceElement = (Element) boatsNode;
            NodeList boatSequenceNodeList = boatSequenceElement.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOAT.toString());
            for (int i = 0; i < boatSequenceNodeList.getLength(); i++) {
                Node boatNode = boatSequenceNodeList.item(i);
                if (boatNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element boatElement = (Element) boatNode;
                    if (boatElement.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_BOAT_TYPE.toString()).equals(Ac35XmlBoatComponents.VALUE_YACHT.toString())) {
                        String boatName = boatElement.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_NAME_BOAT.toString());
                        String boatShortName = boatElement.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_NAME_SHORT.toString());
                        int boatID = Integer.parseInt(boatElement.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_SOURCE_ID.toString()));
                        boats.add(new Boat(boatName, boatShortName, boatID));
                    }
                }
            }
        }
        return boats;
    }
}
