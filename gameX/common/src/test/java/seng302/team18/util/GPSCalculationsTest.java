package seng302.team18.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;

/**
 * Created by jth102 on 17/03/17.
 */
public class GPSCalculationsTest {

    private GPSCalculations gps;

    @Before
    public void setUp() {
        gps = new GPSCalculations();
    }


    @Test
    public void getBearingTest() {
        double expected = 21.8774307492418;
        double delta = 0.0001;

        Coordinate origin = new Coordinate(0, 0);
        Coordinate destination = new Coordinate(90, 180);

        double actual = gps.getBearing(origin, destination);
        Assert.assertEquals(expected, actual, delta);
    }


    @Test
    public void midPointTest() {
        Coordinate expected = new Coordinate(0, 0);
        double delta = 0.0001;

        Coordinate coordinate1 = new Coordinate(50, 10);
        Coordinate coordinate2 = new Coordinate(-50, -10);

        Coordinate actual = gps.midPoint(coordinate1, coordinate2);
        Assert.assertEquals(expected.getLatitude(), actual.getLatitude(), delta);
        Assert.assertEquals(expected.getLongitude(), actual.getLongitude(), delta);
    }


    @Test
    public void toCoordinateTest() {
        Coordinate expected = new Coordinate(0, 0);
        double delta = 0.0001;

        Coordinate origin = new Coordinate(0, 0);

        Coordinate actual = gps.toCoordinate(origin, 10, 0);
        Assert.assertEquals(expected.getLatitude(), actual.getLatitude(), delta);
        Assert.assertEquals(expected.getLongitude(), actual.getLongitude(), delta);
    }


    /**
     * Test for when start is greater than 0 and finish is greater than start.
     */
    @Test
    public void isBetweenTestBlueSkies() {
        Assert.assertTrue(gps.isBetween(60, 30, 90));
        Assert.assertFalse(gps.isBetween(100, 30, 90));
    }


    /**
     * When start angle is greater than finish.
     */
    @Test
    public void isBetweenTestAlternative() {
        Assert.assertTrue(gps.isBetween(60, 350, 90));
        Assert.assertFalse(gps.isBetween(100, 350, 90));
    }
}