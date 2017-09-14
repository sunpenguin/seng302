package seng302.team18.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.model.Coordinate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test for methods in GPSCalculator class.
 */
public class GPSCalculatorTest {

    private GPSCalculator gps;
    private List<Coordinate> boundaryMarks1; // course case 1
    private List<Coordinate> boundaryMarks2; // course case 2
    private List<Coordinate> boundaryMarks3; // course case 3


    @Before
    public void setUp() {
        gps = new GPSCalculator();
        boundaryMarks1 = new ArrayList<>();
        boundaryMarks2 = new ArrayList<>();
        boundaryMarks3 = new ArrayList<>();

        boundaryMarks1.add(new Coordinate(5.27666, -4.42406));
        boundaryMarks1.add(new Coordinate(5.27694, -4.42554));
        boundaryMarks1.add(new Coordinate(5.27586, -4.42831));
        boundaryMarks1.add(new Coordinate(5.27501, -4.42845));
        boundaryMarks1.add(new Coordinate(5.2744, -4.42791));
        boundaryMarks1.add(new Coordinate(5.2738, -4.42859));
        boundaryMarks1.add(new Coordinate(5.27329, -4.42968));
        boundaryMarks1.add(new Coordinate(5.27218, -4.42941));
        boundaryMarks1.add(new Coordinate(5.27228, -4.42777));
        boundaryMarks1.add(new Coordinate(5.27272, -4.42569));
        boundaryMarks1.add(new Coordinate(5.27134, -4.42408));
        boundaryMarks1.add(new Coordinate(5.27069, -4.42487));
        boundaryMarks1.add(new Coordinate(5.2699, -4.42465));
        boundaryMarks1.add(new Coordinate(5.2697, -4.42303));
        boundaryMarks1.add(new Coordinate(5.2701, -4.42094));
        boundaryMarks1.add(new Coordinate(5.2716, -4.41916));
        boundaryMarks1.add(new Coordinate(5.27249, -4.41912));

        boundaryMarks2.add(new Coordinate(32.31056, -64.84599));
        boundaryMarks2.add(new Coordinate(32.30125, -64.82783));
        boundaryMarks2.add(new Coordinate(32.28718, -64.83796));
        boundaryMarks2.add(new Coordinate(32.28108, -64.85023));
        boundaryMarks2.add(new Coordinate(32.29022, -64.86268));
        boundaryMarks2.add(new Coordinate(32.30510, -64.85530));

        boundaryMarks3.add(new Coordinate(20.2802, 38.5126));
        boundaryMarks3.add(new Coordinate(20.2502, 37.5526));
        boundaryMarks3.add(new Coordinate(18.2802, 38.5126));
        boundaryMarks3.add(new Coordinate(17.2502, 39.5126));
        boundaryMarks3.add(new Coordinate(19.2502, 38.5126));
        boundaryMarks3.add(new Coordinate(19.2502, 37.5126));
        boundaryMarks3.add(new Coordinate(18.2502, 39.5126));
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

    @Test
    public void notContainsTest() {
        boolean doesContain = gps.isInside(new Coordinate(0, 0),
                Arrays.asList(
                        new Coordinate(100, 100),
                        new Coordinate(101, 101),
                        new Coordinate(102, 102),
                        new Coordinate(99, 99)));
        Assert.assertFalse(doesContain);
    }


    @Test
    public void containsTest() {
        boolean doesContain = gps.isInside(new Coordinate(32.3547,-89.3985),
                Arrays.asList(
                        new Coordinate(25.767368,-80.18930),
                        new Coordinate(34.088808,-118.40612),
                        new Coordinate(40.727093,-73.97864)));
        Assert.assertTrue(doesContain);
    }


    @Test
    /**
     * Check if the point is inside the course boundary case 1.
     */
    public void randomPoint1Test() {
        Coordinate randomPoint = gps.randomPoint(boundaryMarks1);

        boolean isInside = gps.isInside(randomPoint, boundaryMarks1);

        Assert.assertTrue(isInside);
    }


    @Test
    /**
     * Check if the point is inside the course boundary case 1.
     */
    public void randomPoint2Test() {
        Coordinate randomPoint = gps.randomPoint(boundaryMarks1);

        boolean isInside = gps.isInside(randomPoint, boundaryMarks1);

        Assert.assertTrue(isInside);
    }


    @Test
    /**
     * Check if the point is inside the course boundary case 2.
     */
    public void randomPoint3Test() {
        Coordinate randomPoint = gps.randomPoint(boundaryMarks2);

        boolean isInside = gps.isInside(randomPoint, boundaryMarks2);

        Assert.assertTrue(isInside);
    }


    @Test
    /**
     * Check if the point is inside the course boundary case 2.
     */
    public void randomPoint4Test() {
        Coordinate randomPoint = gps.randomPoint(boundaryMarks2);

        boolean isInside = gps.isInside(randomPoint, boundaryMarks2);

        Assert.assertTrue(isInside);
    }


    @Test
    /**
     * Check if the point is inside the course boundary case 3.
     */
    public void randomPoint5Test() {
        Coordinate randomPoint = gps.randomPoint(boundaryMarks3);

        boolean isInside = gps.isInside(randomPoint, boundaryMarks3);

        Assert.assertTrue(isInside);
    }


    @Test
    /**
     * Check if the point is inside the course boundary case 3.
     */
    public void randomPoint6Test() {
        Coordinate randomPoint = gps.randomPoint(boundaryMarks3);

        boolean isInside = gps.isInside(randomPoint, boundaryMarks3);

        Assert.assertTrue(isInside);
    }
}