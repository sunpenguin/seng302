package seng302;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Created by david on 3/21/17.
 */
public class CoordinateTest {

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setErr(null);
    }

    @Test
    public void constructorTest() {
        String expected = "";
        Coordinate testCoordinate1 = new Coordinate(1, 1);
        assertEquals(expected, errContent.toString());

        expected += "Latitude must be between -90 and 90\n";
        Coordinate testCoordinate2 = new Coordinate(100, 1);
        assertEquals(expected, errContent.toString());

        expected += "Longitude must be between -180 and 180\n";
        Coordinate testCoordinate3 = new Coordinate(1, 200);
        assertEquals(expected, errContent.toString());
    }

    @Test
    public void latitudeTest() {
        double expected = 12;
        Coordinate actual = new Coordinate(12, 13);
        assertEquals(expected, actual.getLatitude(), 0.000001);

        expected = 10;
        actual.setLatitude(10);
        assertEquals(expected, actual.getLatitude(), 0.000001);

        actual.setLatitude(100000);
        assertEquals(expected, actual.getLatitude(), 0.000001);
    }

    @Test
    public void longitudeTest() {
        double expected = 13;
        Coordinate actual = new Coordinate(12, 13);
        assertEquals(expected, actual.getLongitude(), 0.000001);

        expected = 2;
        actual.setLongitude(2);
        assertEquals(expected, actual.getLongitude(), 0.000001);

        actual.setLongitude(10000000);
        assertEquals(expected, actual.getLongitude(), 0.000001);
    }
}
