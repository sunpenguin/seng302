package seng302.team18.data;

import org.junit.Test;
import org.xml.sax.SAXException;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.model.Mark;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by csl62 on 17/03/17.
 */
public class XMLCourseParserTest {

//
//    private InputStream courseFile = new BufferedInputStream(getClass().getResourceAsStream("/test-course.xml"));
//
//
//    @Test
//    public void testParseCourse() throws IOException, SAXException, ParserConfigurationException {
//
//        Course actual = XMLCourseParser.parseCourse(courseFile);
//        List<CompoundMark> expectedCompoundMarks = new ArrayList<>();
//        List<Mark> expectedMarks = new ArrayList<>();
//        expectedMarks.add(new Mark("Start1", new Coordinate(30, -60.0)));
//        expectedCompoundMarks.add(new CompoundMark("Start", new ArrayList<>(expectedMarks)));
//        expectedMarks.clear();
//        expectedMarks.add(new Mark("Mark1", new Coordinate(40.0, -70.0)));
//        expectedCompoundMarks.add(new CompoundMark("Mark", new ArrayList<>(expectedMarks)));
//        expectedMarks.clear();
//        expectedMarks.add(new Mark("Finish1", new Coordinate(50.0, -80.0)));
//        expectedMarks.add(new Mark("Finish2", new Coordinate(51.0, -81.0)));
//        expectedCompoundMarks.add(new CompoundMark("Finish", expectedMarks));
//
//        Course expected = new Course(expectedCompoundMarks, new ArrayList<>(), 45.0, ZoneId.of("Atlantic/Bermuda"));
//
//        assertEquals(expected.getTimeZone(), actual.getTimeZone());
//        assertEquals(expected.getWindDirection(), actual.getWindDirection(), 0.1);
//        assertEquals(expected.getBoundaries().size(), actual.getBoundaries().size());
//        for (int i = 0; i < expected.getBoundaries().size(); i++) {
//            assertEquals(expected.getBoundaries().get(i), actual.getBoundaries().get(i));
//        }
//        assertEquals(expected.getCompoundMarks().size(), actual.getCompoundMarks().size());
//        for (int i = 0; i < expected.getCompoundMarks().size(); i++) {
//            CompoundMark expectedMark = expected.getCompoundMarks().get(i);
//            CompoundMark actualMark = actual.getCompoundMarks().get(i);
//            assertEquals(expectedMark.getName(), actualMark.getName());
//            assertEquals(expectedMark.getPassed().size(), actualMark.getPassed().size());
//            for (int j = 0; j < expectedMark.getPassed().size(); j++) {
//                assertEquals(expectedMark.getPassed().get(j), actualMark.getPassed().get(j));
//            }
//            assertEquals(expectedMark.getMarks().size(), actualMark.getMarks().size());
//            for (int j = 0; j < expectedMark.getMarks().size(); j++) {
//                assertEquals(expectedMark.getMarks().get(j).getName(), actualMark.getMarks().get(j).getName());
//                assertEquals(expectedMark.getMarks().get(j).getCoordinates(), actualMark.getMarks().get(j).getCoordinates());
//            }
//        }
//
//    }

}
