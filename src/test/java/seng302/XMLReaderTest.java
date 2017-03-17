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

    private File boatFile = new File("/home/cosc/student/csl62/Desktop/Seng302/team-18/src/main/resources/boats.xml");
    private File courseFile = new File("/home/cosc/student/csl62/Desktop/Seng302/team-18/src/main/resources/course.xml");

    @Test
    public void testParseBoats() throws IOException, SAXException, ParserConfigurationException {

        ArrayList<Boat> boats = XMLParser.parseBoats(boatFile);
        Boat testBoat = boats.get(0);
        assertEquals("Emirates", testBoat.getBoatName());
        assertEquals("New Zealand", testBoat.getTeamName());
        assertEquals(70.0, testBoat.getSpeed(), 0.1);
        assertEquals(6, boats.size());
    }

    @Test
    public void testParseCourse() throws IOException, SAXException, ParserConfigurationException {

        Course course = XMLParser.parseCourse(courseFile);
        ArrayList<CompoundMark> compoundMarks = course.getCompoundMarks();
        CompoundMark compoundMark = compoundMarks.get(0);
        assertEquals(compoundMark.getName(), "Start");

    }

}
