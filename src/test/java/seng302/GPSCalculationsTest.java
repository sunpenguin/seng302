package seng302;

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

    @Test
    public void findMinMaxPointstest() throws IOException, SAXException, ParserConfigurationException {

        Course testCourse = XMLParser.parseCourse(new File("/home/cosc/student/jth102/302/project302/team-18/src/main/resources/course.xml"));


        GPSCalculations g = new GPSCalculations();
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
}
