package seng302.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import seng302.model.Boat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhl25 on 30/03/17.
 */
public class XMLBoatParser {

    /**
     * Creates a new ArrayList of Boat by parsing a XML file.
     *
     * @param file
     * @return The boats from the XML file
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static List<Boat> parseBoats(InputStream file) throws ParserConfigurationException, IOException, SAXException {
        final String BOATS_TAG = "boats";
        final String BOAT_TAG = "boat";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        Element boatsElement = (Element) doc.getElementsByTagName(BOATS_TAG).item(0);
        NodeList boatNodes = boatsElement.getElementsByTagName(BOAT_TAG);
        List<Boat> boats = new ArrayList<>();
        for (int i = 0; i < boatNodes.getLength(); i++) {
            Node boatNode = boatNodes.item(i);
            if (boatNode.getNodeType() == Node.ELEMENT_NODE) {
                Element boatElement = (Element) boatNode;
                boats.add(parseBoat(boatElement));
            }
        }
        return boats;
    }

    /**
     * Returns a boat given an Element containing a boat
     * @param boatElement
     * @return Boat contained within the Element
     */
    private static Boat parseBoat(Element boatElement) {
        final String SPEED_TAG = "speed";
        final String BOAT_NAME_TAG = "boatname";
        final String TEAM_NAME_TAG = "teamname";
        double speed = Double.parseDouble(boatElement.getElementsByTagName(SPEED_TAG).item(0).getTextContent());
        String boatName = boatElement.getElementsByTagName(BOAT_NAME_TAG).item(0).getTextContent();
        String teamName = boatElement.getElementsByTagName(TEAM_NAME_TAG).item(0).getTextContent();
        return new Boat(boatName, teamName, speed);
    }

}
