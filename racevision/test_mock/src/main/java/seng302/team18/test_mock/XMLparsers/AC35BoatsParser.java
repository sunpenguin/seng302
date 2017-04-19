package seng302.team18.test_mock.XMLparsers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import seng302.team18.data.MessageBody;
import seng302.team18.model.Boat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jth102 on 19/04/17.
 */
public class AC35BoatsParser {

    public AC35BoatsContainer parse(InputStream stream) {
        final String BOATS_ELEMENT = "BoatConfig";
        final String BOAT_SETTINGS = "Settings";
        final String BOAT_SHAPES = "BoatShapes";
        final String BOATS = "Boats";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(stream);

            doc.getDocumentElement().normalize();
            Element boatsElement = (Element) doc.getElementsByTagName(BOATS_ELEMENT).item(0); // root

            // Settings contains RaceBoatType, BoatDimension, ZoneSize, ZoneLimits
            Node settingsNode = boatsElement.getElementsByTagName(BOAT_SETTINGS).item(0); // currently don't use any of this

            // Has BoatShape (ShapeID), which has Vertices (with Vtx) and can have Catamaran, Bowsprit, Trampoline
            Node shapeNode = boatsElement.getElementsByTagName(BOAT_SHAPES).item(0); // currently don't use any of this

            Node boatsNode = boatsElement.getElementsByTagName(BOATS).item(0);
            List<Boat> boats = parseBoats(boatsNode);


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }
//        return message;
    }

    private List<Boat> parseBoats(Node boatsNode) {
        // A boat has Type, SourceID, ShapeID, HullNum, StoweName with elements GPSposition and FlagPosition
        final String BOAT_ELEMENT = "Boat";
        final String BOAT_NAME = "BoatName";
        final String BOAT_SHORT_NAME = "ShortName";
        final String BOAT_ID = "SourceID";
        final String BOAT_TYPE = "Type";
        final String YACHT = "Yacht";

        List<Boat> boats = new ArrayList<>();
        if (boatsNode.getNodeType() == Node.ELEMENT_NODE) {
            Element boatSequenceElement = (Element) boatsNode;
            NodeList boatSequenceNodeList = boatSequenceElement.getElementsByTagName(BOAT_ELEMENT);
            for (int i = 0; i < boatSequenceNodeList.getLength(); i++) {
                Node boatNode = boatSequenceNodeList.item(i);
                if (boatNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element boatElement = (Element) boatNode;
                    if (boatElement.getAttribute(BOAT_TYPE).equals(YACHT)) {
                        String boatName = boatElement.getAttribute(BOAT_NAME);
                        String boatShortName = boatElement.getAttribute(BOAT_SHORT_NAME);
                        int boatID = Integer.parseInt(boatElement.getAttribute(BOAT_ID));
                        boats.add(new Boat(boatName, boatShortName, boatID));
                    }
                }
            }
        }
        return boats;
    }

}
