package seng302.team18.test_mock.XMLparsers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import seng302.team18.data.AC35XMLBoatParser;
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

    private AC35BoatsContainer boatsContainer = new AC35BoatsContainer();

    public AC35BoatsContainer parse(InputStream stream) {
        final String BOATS_ELEMENT = "BoatConfig";
        final String BOAT_SETTINGS = "Settings";
        final String BOAT_SHAPES = "BoatShapes";
        final String BOATS = "Boats";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            AC35XMLBoatParser parser = new AC35XMLBoatParser();

            builder = factory.newDocumentBuilder();
            doc = builder.parse(stream);

            doc.getDocumentElement().normalize();
            Element boatsElement = (Element) doc.getElementsByTagName(BOATS_ELEMENT).item(0); // root

            // Settings contains RaceBoatType, BoatDimension, ZoneSize, ZoneLimits
            Node settingsNode = boatsElement.getElementsByTagName(BOAT_SETTINGS).item(0); // currently don't use any of this

            // Has BoatShape (ShapeID), which has Vertices (with Vtx) and can have Catamaran, Bowsprit, Trampoline
            Node shapeNode = boatsElement.getElementsByTagName(BOAT_SHAPES).item(0); // currently don't use any of this

            Node boatsNode = boatsElement.getElementsByTagName(BOATS).item(0);
            List<Boat> boats = parser.parseBoats(boatsNode);
            boatsContainer.setBoats(boats);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }

        return boatsContainer;
    }
}
