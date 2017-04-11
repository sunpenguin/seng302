package seng302.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import seng302.model.Boat;
import sun.security.provider.SHA;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhl25 on 30/03/17.
 */
public class XMLBoatParser {

//    public static ArrayList<BoatShapes> parseBoatShape(InputStream source) throws ParserConfigurationException, IOException, SAXException{
//        ArrayList<BoatShapes> shapes = new ArrayList<>();
//        final String BOAT_SHAPE_TAG = "BoatShapes";
//        final String BOAT_SHAPES_TAG = "BoatShapes";
//
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.parse(source);
//        doc.getDocumentElement().normalize();
//        Element shapesElement = (Element) doc.getElementsByTagName(BOAT_SHAPES_TAG).item(0);
//        NodeList shapeNodes = shapesElement.getElementsByTagName(BOAT_SHAPE_TAG);
//        for(int i = 0; i < shapeNodes.getLength(); i++){
//
//        }
//        return shapes;
//    }


    /**
     * Creates a new ArrayList of Boat by parsing a XML file.
     *
     * @param source
     * @return The boats from the XML file
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static List<Boat> parseBoats(InputStream source) throws ParserConfigurationException, IOException, SAXException {
        final String BOATS_TAG = "Boats";
        final String BOAT_TAG = "Boat";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(source);
        doc.getDocumentElement().normalize();
        Element boatsElement = (Element) doc.getElementsByTagName(BOATS_TAG).item(0);
        NodeList boatNodes = boatsElement.getElementsByTagName(BOAT_TAG);
        List<Boat> boats = new ArrayList<>();
        for (int i = 0; i < boatNodes.getLength(); i++) {
            Node boatNode = boatNodes.item(i);
            System.out.println(boatNode.getTextContent());
            if (boatNode.getNodeType() == Node.ELEMENT_NODE) {
                Element boatElement = (Element) boatNode;
                parseBoat(boatElement);
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
        final String SOURCE_ID = "SourceID";
        final String SHORT_NAME = "ShortName";
        final String BOAT_NAME = "BoatName";

        int sourceId = Integer.parseInt(boatElement.getAttributes().getNamedItem(SOURCE_ID).getTextContent());
        String shortName = boatElement.getAttributes().getNamedItem(SHORT_NAME).getTextContent();
        String boatName = boatElement.getAttributes().getNamedItem(BOAT_NAME).getTextContent();
        return new Boat(boatName, shortName, sourceId);
    }

}
