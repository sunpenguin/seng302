package seng302;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for parsing XML files which represent Courses into a Course class.
 */
public class XMLParser {

    /**
     * Creates a new Course by parsing a XML file.
     *
     * @param file the XML file to be read from
     * @return the course that is represented by the file
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Course parseCourse(File file) throws ParserConfigurationException, IOException, SAXException {
        final String COMPOUND_MARK_TAG = "compoundMark"; // declaring things as final is fun
        final String COURSE_TAG = "course";
        // Gets file ready for parsing
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        // creates all Compound Marks
        Element courseElement = (Element) doc.getElementsByTagName(COURSE_TAG).item(0);
        NodeList compoundNodes = courseElement.getElementsByTagName(COMPOUND_MARK_TAG);
        ArrayList<CompoundMark> compoundMarks = new ArrayList<>();
        for(int i = 0; i < compoundNodes.getLength(); i++) {
            Node compoundMarkNode = compoundNodes.item(i);
            if (compoundMarkNode.getNodeType() == Node.ELEMENT_NODE) {
                Element compoundMarkElement = (Element) compoundMarkNode;
                compoundMarks.add(parseCompoundMark(compoundMarkElement));
            }
        }
        // parses boundaries
//        Element boundaryElements = (Element) doc.getElementsByTagName(BOUNDARY_TAG).item(0);
//        ArrayList<Coordinate> boundaries = parseBoundaries(boundaryElements);
//        return new Course(compoundMarks, boundaries.get(TOP_LEFT_INDEX), boundaries.get(TOP_RIGHT_INDEX),
//                boundaries.get(BOTTOM_LEFT_INDEX), boundaries.get(BOTTOM_RIGHT_INDEX));
        return new Course(compoundMarks);
    }

    /**
     * Creates a CompoundMark given a XML Element which represents a CompoundMark.
     *
     * @param compoundMarkElement a XML Element which represents a CompoundMark
     * @return the CompoundMark parsed from the compoundMarkElement
     */
    private static CompoundMark parseCompoundMark(Element compoundMarkElement) {
        final String LATITUDE_TAG = "latitude";
        final String LONGITUDE_TAG = "longitude";
        final String MARK_TAG = "mark";
        final String COMPOUND_MARK_NAME_TAG = "name";
        final String MARK_NAME_TAG = "markName";

        String name = compoundMarkElement.getElementsByTagName(COMPOUND_MARK_NAME_TAG).item(0).getTextContent();
        NodeList markList = compoundMarkElement.getElementsByTagName(MARK_TAG);
        ArrayList<Mark> marks = new ArrayList<>();
        for (int i = 0; i < markList.getLength(); i++) {
            Node markNode = markList.item(i);
            Element markElement = (Element) markNode;
            String markName = markElement.getElementsByTagName(MARK_NAME_TAG).item(0).getTextContent();
            String latString = markElement.getElementsByTagName(LATITUDE_TAG).item(0).getTextContent();
            String longString = markElement.getElementsByTagName(LONGITUDE_TAG).item(0).getTextContent();
            double markLatitude = Double.parseDouble(latString);
            double markLongitude = Double.parseDouble(longString);
            Mark mark = new Mark(markName, new Coordinate(markLatitude, markLongitude));
            marks.add(mark);
        }
        return new CompoundMark(name, marks);
    }

    /**
     * Creates an ArrayList of four Coordinates which represent the boundaries of the Course.
     * Boundaries in the list will always be in the order top left, top right, bottom left, and bottom right.
     *
     * @param boundaryElements Element which contains all boundary elements
     * @return the four Coordinates which represent the boundaries of the Course
     */
    private static ArrayList<Coordinate> parseBoundaries(Element boundaryElements) {
        final ArrayList<String> BOUNDARY_TAGS = new ArrayList<>(
                Arrays.asList("topLeft", "topRight", "bottomLeft", "bottomRight"));
        final String LATITUDE_TAG = "latitude";
        final String LONGITUDE_TAG = "longitude";

        ArrayList<Coordinate> boundries = new ArrayList<>();
        for (int i = 0; i < BOUNDARY_TAGS.size(); i++) {
            Element boundaryElement = (Element) boundaryElements.getElementsByTagName(BOUNDARY_TAGS.get(i)).item(0);
            String latString = boundaryElement.getElementsByTagName(LATITUDE_TAG).item(0).getTextContent();
            String longString = boundaryElement.getElementsByTagName(LONGITUDE_TAG).item(0).getTextContent();
            Coordinate boundary = new Coordinate(Double.parseDouble(latString), Double.parseDouble(longString));
            boundries.add(boundary);
        }
        return boundries;
    }


    public static ArrayList<Boat> parseBoats(File file) throws ParserConfigurationException, IOException, SAXException {
        final String BOATS_TAG = "boats";
        final String BOAT_TAG = "boat";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        Element boatsElement = (Element) doc.getElementsByTagName(BOATS_TAG).item(0);
        NodeList boatNodes = boatsElement.getElementsByTagName(BOAT_TAG);
        ArrayList<Boat> boats = new ArrayList<>();
        for (int i = 0; i < boatNodes.getLength(); i++) {
            Node boatNode = boatNodes.item(i);
            if (boatNode.getNodeType() == Node.ELEMENT_NODE) {
                Element boatElement = (Element) boatNode;
                boats.add(parseBoat(boatElement));
            }
        }
        return boats;
    }


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
