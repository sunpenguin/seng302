package seng302.team18.util;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import seng302.team18.data.XMLCourseParser;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by jth102 on 17/03/17.
 */
public class GPSCalculationsTest {

    private Course testCourse;
    private GPSCalculations g;

    @Before
    public void setUp() throws IOException, SAXException, ParserConfigurationException {
        testCourse = XMLCourseParser.parseCourse(new BufferedInputStream(getClass().getResourceAsStream("/test-course.xml")));
        g = new GPSCalculations(testCourse);
    }


    @Test
    public void findMinMaxPointsTest() throws IOException, SAXException, ParserConfigurationException {

        g.findMinMaxPoints(testCourse);

        double minX = g.getMinX();
        double maxX = g.getMaxX();
        double minY = g.getMinY();
        double maxY = g.getMaxY();

        XYPair expectedMinXCoord = GPSCalculations.GPSxy(new Coordinate(51.0, -81.0));
        XYPair expectedMaxXCoord = GPSCalculations.GPSxy(new Coordinate(30.0, -60.0));
        XYPair expectedMinYCoord = GPSCalculations.GPSxy(new Coordinate(30.0, -60.0));
        XYPair expectedMaxYCoord = GPSCalculations.GPSxy(new Coordinate(51.0, -81.0));

        assertEquals(expectedMinXCoord.getX(), minX, 1);
        assertEquals(expectedMaxXCoord.getX(), maxX, 1);
        assertEquals(expectedMinYCoord.getY(), minY, 1);
        assertEquals(expectedMaxYCoord.getY(), maxY, 1);
    }
}