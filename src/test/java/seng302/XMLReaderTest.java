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

    private File boatFile = new File("/home/cosc/student/jth102/302/project302/team-18/src/main/resources/boats.xml");
    private File courseFile = new File("/home/cosc/student/jth102/302/project302/team-18/src/main/resources/course.xml");

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
        assertEquals(compoundMark.getMarks().size(), 2);
        assertEquals(compoundMarks.size(), 6);

        CompoundMark compoundMark1 = compoundMarks.get(3);
        assertEquals(compoundMark1.getMarks().size(), 2);
        assertEquals(compoundMark1.getMarks().get(1).getMarkName(), "Southern gate 2");
    }

}
