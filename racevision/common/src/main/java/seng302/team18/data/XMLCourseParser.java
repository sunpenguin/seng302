package seng302.team18.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.model.Mark;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for parsing XML files which represent Courses into a Course class.
 */
public class XMLCourseParser {

    /**
     * Creates a new Course by parsing a XML file.
     *
     * @param courseFile the XML file to be read from
     * @return the course that is represented by the file
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Course parseCourse(InputStream courseFile) throws ParserConfigurationException, IOException, SAXException {
        final String COMPOUND_MARK_TAG = "compoundMark"; // declaring things as final is fun
        final String COURSE_TAG = "course";
        final String WIND_TAG = "windDirection";
        final String BOUNDARIES_TAG = "boundaries";
        // Gets file ready for parsing
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(courseFile);
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
        // wind direction
        NodeList windDirectionNodes = courseElement.getElementsByTagName(WIND_TAG);
        double windDirection = -1.0;
        if (windDirectionNodes.getLength() != 0) {
            windDirection = Double.parseDouble(windDirectionNodes.item(0).getTextContent());
        }
        // boundaries
        Element boundariesElement = (Element) courseElement.getElementsByTagName(BOUNDARIES_TAG).item(0);
        List<Coordinate> boundaries = parseBoundaries(boundariesElement); // parses boundaries
        ZoneId courseTimeZone = ZoneId.of(parseTimeZone(courseElement).trim());
        return new Course(compoundMarks, boundaries, windDirection, courseTimeZone);
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
        List<Mark> marks = new ArrayList<>();
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
     * @param boundariesElement Element which contains all boundary elements
     * @return the four Coordinates which represent the boundaries of the Course
     */
    private static ArrayList<Coordinate> parseBoundaries(Element boundariesElement) {
        final String BOUNDARY_TAG = "boundary";

        ArrayList<Coordinate> boundaries = new ArrayList<>();
        NodeList boundaryNodes = boundariesElement.getElementsByTagName(BOUNDARY_TAG);
        for (int i = 0; i < boundaryNodes.getLength(); i++) {
            Node boundaryNode = boundaryNodes.item(i);
            if (boundaryNode.getNodeType() == Node.ELEMENT_NODE) {
                Element boundaryElement = (Element) boundaryNode;
                boundaries.add(parseBoundary(boundaryElement));
            }
        }
        return boundaries;
    }

    private static Coordinate parseBoundary(Element boundaryElement) {
        final String LATITUDE_TAG = "latitude";
        final String LONGITUDE_TAG = "longitude";

        String latString = boundaryElement.getElementsByTagName(LATITUDE_TAG).item(0).getTextContent();
        String longString = boundaryElement.getElementsByTagName(LONGITUDE_TAG).item(0).getTextContent();
        return new Coordinate(Double.parseDouble(latString), Double.parseDouble(longString));
    }



    private static String parseTimeZone(Element courseElement) throws ParserConfigurationException, IOException, SAXException {
        final String COURSE_TAG = "course";
        final String TIME_ZONE_TAG = "timeZone";
        NodeList timeZoneNodes = courseElement.getElementsByTagName(TIME_ZONE_TAG);
        return timeZoneNodes.item(0).getTextContent();
    }

}
