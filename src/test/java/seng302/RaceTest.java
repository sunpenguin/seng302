package seng302;

import org.junit.Test;
import org.xml.sax.SAXException;
import seng302.model.*;
import seng302.data.XMLCourseParser;
import seng302.util.GPSCalculations;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.*;


/**
 * Test class for the Race class
 */
public class RaceTest {


    /**
     * A test to ensure that the boats, when updated are travelling to the correct position.
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    @Test
    public void updateBoatsTest() throws IOException, SAXException, ParserConfigurationException {
        int time = 5;
        InputStream file = new BufferedInputStream(new BufferedInputStream(getClass().getResourceAsStream("/course.xml")));
        Course course = XMLCourseParser.parseCourse(file);
        CompoundMark start = course.getCompoundMarks().get(0);
        Boat boat1 = new Boat("Emirates", "NZL", 45);
        ArrayList<Boat> boats = new ArrayList<>();
        boats.add(boat1);
        Race race = new Race(boats, course);
        race.updateBoats(time);
        double distance = (boat1.getSpeed()/3.6)*time;
        Coordinate testCoord = GPSCalculations.coordinateToCoordinate(start.getMidCoordinate(),
                boat1.getHeading(), distance);
        assertEquals(testCoord.getLatitude(), boat1.getCoordinate().getLatitude(), 0.01);
        assertEquals(testCoord.getLongitude(), boat1.getCoordinate().getLongitude(), 0.01);
    }


    /**
     * A test to ensure the course is correctly set for the boat(This method is called inside the race constructor
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    @Test
    public void setCourseForBoatsTest() throws IOException, SAXException, ParserConfigurationException {
        InputStream file = new BufferedInputStream(new BufferedInputStream(getClass().getResourceAsStream("/course.xml")));
        Course course = XMLCourseParser.parseCourse(file);
        Boat boat1 = new Boat("Emirates", "NZL", 45);
        ArrayList<Boat> boats = new ArrayList<>();
        boats.add(boat1);
        Race testRace = new Race(boats, course);
        CompoundMark expectedMark = course.getCompoundMarks().get(1);
        CompoundMark actualMark = boat1.getLeg().getDestination();
        assertEquals(expectedMark.getMidCoordinate().getLatitude(), actualMark.getMidCoordinate().getLatitude(), 0.01);
        assertEquals(expectedMark.getMidCoordinate().getLongitude(), actualMark.getMidCoordinate().getLongitude(), 0.01);
    }
}
