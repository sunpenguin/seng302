package seng302.team18.messageparsing;

import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.Ac35XmlBoatComponents;
import seng302.team18.model.AbstractBoat;
import seng302.team18.model.Boat;
import seng302.team18.model.BoatType;
import seng302.team18.model.Mark;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        Element boatDimensionsNode = (Element) doc.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOAT_DIMENSION.toString()).item(0);

        Node boatsNode = boatsElement.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOATS.toString()).item(0);
        List<AbstractBoat> boats = parseBoats(boatsNode, boatDimensionsNode);

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
    private List<AbstractBoat> parseBoats(Node boatsNode, Element boatDimensionsNode) {
        List<AbstractBoat> boats = new ArrayList<>();

        if (boatsNode.getNodeType() == Node.ELEMENT_NODE) {
            Element boatSequenceElement = (Element) boatsNode;
            NodeList boatSequenceNodeList = boatSequenceElement.getElementsByTagName(Ac35XmlBoatComponents.ELEMENT_BOAT.toString());

            for (int i = 0; i < boatSequenceNodeList.getLength(); i++) {
                Node boatNode = boatSequenceNodeList.item(i);
                parseBoat(boats, boatNode, boatDimensionsNode);
            }
        }
        return boats;
    }


    /**
     * Parses a node describing a boat, adding it to the given list. If the node cannot be parsed or is in error,
     * the list is unchanged.
     *
     * @param boats    the list to add to boat to
     * @param boatNode the node describing the boat
     */
    private void parseBoat(List<AbstractBoat> boats, Node boatNode, Element boatDimensionsNode) {
        if (boatNode.getNodeType() == Node.ELEMENT_NODE) {
            Element boatElement = (Element) boatNode;

            AbstractBoat boat;

            String boatType = boatElement.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_TYPE.toString());
            String boatName = boatElement.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_NAME_BOAT.toString());
            String boatShortName = boatElement.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_NAME_SHORT.toString());
            String colourString = boatElement.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_COLOUR.toString());
            Color colour = null;
            if (!colourString.isEmpty()) {
                colour = Color.web(colourString);
            }
            int boatId = Integer.parseInt(boatElement.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_SOURCE_ID.toString()));
            double boatLength = Double.parseDouble(boatDimensionsNode.getAttribute(Ac35XmlBoatComponents.ATTRIBUTE_BOAT_LENGTH.toString()));

            switch (BoatType.ofTypeName(boatType)) {
                case YACHT:
                    boat = new Boat(boatName, boatShortName, boatId, boatLength);
                    boat.setColour(colour);
                    break;
                case MARK:
                    boat = new Mark(boatId, boatName, boatShortName);
                    break;
                default:
                    return;
            }

            boats.add(boat);
        }
    }
}
