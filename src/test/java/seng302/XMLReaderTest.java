package seng302;

import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by csl62 on 17/03/17.
 */
public class XMLReaderTest {

    private File boatFile = new File("src/main/resources/boats.xml");
    private File courseFile = new File("src/main/resources/course.xml");

    @Test
    public void testParseBoats() throws IOException, SAXException, ParserConfigurationException {

        ArrayList<Boat> boats = XMLParser.parseBoats(boatFile);
        Boat testBoat = boats.get(4);
        assertEquals("Land Rover", testBoat.getBoatName());
        assertEquals("Britain", testBoat.getTeamName());
        assertEquals(50.0, testBoat.getSpeed(), 0.1);
        assertEquals(6, boats.size());

    }

    @Test
    public void testParseCourse() throws IOException, SAXException, ParserConfigurationException {

        Course course = XMLParser.parseCourse(courseFile);
        ArrayList<CompoundMark> actualCourse = course.getCompoundMarks();
        CompoundMark actualMark = actualCourse.get(0);
        assertEquals("Start", actualMark.getName());
        assertEquals(2, actualMark.getMarks().size());
        assertEquals("Start1", actualMark.getMarks().get(0).getName());
        assertEquals(new Coordinate(32.296577, -64.854304), actualMark.getMarks().get(0).getCoordinates());
        assertEquals("Start2", actualMark.getMarks().get(1).getName());
        assertEquals(new Coordinate(32.293771, -64.855242), actualMark.getMarks().get(1).getCoordinates());
        assertEquals(6, actualCourse.size());

        CompoundMark compoundMark1 = actualCourse.get(3);
        assertEquals(2, compoundMark1.getMarks().size());
        assertEquals("Southern gate 2", compoundMark1.getMarks().get(1).getName());
    }

}
