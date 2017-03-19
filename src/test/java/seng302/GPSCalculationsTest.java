package seng302;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Created by jth102 on 17/03/17.
 */
public class GPSCalculationsTest {

    private Course testCourse;
    private GPSCalculations g = new GPSCalculations();

    @Before
    public void setUpTestCourse() throws IOException, SAXException, ParserConfigurationException {
        testCourse = XMLParser.parseCourse(new File("/home/cosc/student/jth102/302/project302/team-18/src/main/resources/course.xml"));
    }


    @Test
    public void findMinMaxPointstest() throws IOException, SAXException, ParserConfigurationException {

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
        Coordinate testCoordinate = testCourse.getCompoundMarks().get(1).getMarks().get(0).getMarkCoordinates();

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
