package seng302;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import seng302.model.Coordinate;
import seng302.model.Course;
import seng302.parser.XMLParser;
import seng302.raceutil.GPSCalculations;
import seng302.raceutil.XYPair;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by jth102 on 17/03/17.
 */
public class GPSCalculationsTest {

    private Course testCourse;
    private GPSCalculations g;

    @Before
    public void setUp() throws IOException, SAXException, ParserConfigurationException {
        testCourse = XMLParser.parseCourse(new File("src/main/resources/course.xml"));
        g = new GPSCalculations(testCourse);
    }


    @Test
    public void findMinMaxPointsTest() throws IOException, SAXException, ParserConfigurationException {

        g.findMinMaxPoints(testCourse);

        double minX = g.getMinX();
        double maxX = g.getMaxX();
        double minY = g.getMinY();
        double maxY = g.getMaxY();

        XYPair expectedMinXCoord = GPSCalculations.GPSxy(new Coordinate(32.293771, -64.855242));
        XYPair expectedMaxXCoord = GPSCalculations.GPSxy(new Coordinate(32.308046, -64.831785));
        XYPair expectedMinYCoord = GPSCalculations.GPSxy(new Coordinate(32.280164, -64.847591));
        XYPair expectedMaxYCoord = GPSCalculations.GPSxy(new Coordinate(32.317379, -64.839291));

        assertEquals(minX, expectedMinXCoord.getX(), 1);
        assertEquals(maxX, expectedMaxXCoord.getX(), 1);
        assertEquals(minY, expectedMinYCoord.getY(), 1);
        assertEquals(maxY, expectedMaxYCoord.getY(), 1);
    }

    @Test
    public void XYtoGPSConversionTest() {
        // Get the coordinates of the first Mark in the second CompoundMark
        Coordinate testCoordinate = testCourse.getCompoundMarks().get(1).getMarks().get(0).getCoordinates();

        // Convert GPS coordinates to cartesian coordinates
        XYPair testCoordinateXY = GPSCalculations.GPSxy(testCoordinate);

        // Convert the cartesian coordinates back to GPS coordinates
        Coordinate testCoordinate1 = GPSCalculations.XYToCoordinate(testCoordinateXY);

        // Check that the 2 sets of GPS coordinates are the same within a delta of 1
//        System.out.printf("Original lat: %.6f | New lat: %.6f\n", testCoordinate.getLatitude(), testCoordinate1.getLatitude());
        assertEquals(testCoordinate.getLatitude(), testCoordinate1.getLatitude(), 0.000001);
//        System.out.printf("Original long: %.6f | New long: %.6f\n", testCoordinate.getLongitude(), testCoordinate1.getLongitude());
        assertEquals(testCoordinate.getLongitude(), testCoordinate1.getLongitude(), 0.000001);
    }
}
