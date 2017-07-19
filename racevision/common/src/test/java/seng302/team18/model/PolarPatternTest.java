package seng302.team18.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhl25 on 19/07/17.
 */
public class PolarPatternTest {


    private PolarPattern optimalAnglePattern;
    private PolarPattern acPattern;

    @Before
    public void setUp() {
        acPattern = new AC35PolarPattern();
        optimalAnglePattern = new PolarPattern() {

            @Override
            public double getSpeedForBoat(double boatTWA, double windSpeed) {
                return windSpeed;
            }

            @Override
            protected Map<Double, Polar> createMap() {
                return new HashMap<>();
            }
        };
    }


    /**
     * This test will still work if getSpeedForBoat method is broken.
     */
    @Test
    public void optimalAngleTest() {
        double actual = optimalAnglePattern.optimalAngle(new Coordinate(0, 0), new Coordinate(10, 10), 10);
        double expected = 140.0d; // angle from 0, 0 to 10, 10
        Assert.assertEquals(expected, actual, 0.1);
    }

    /**
     * This test will not work if AC35PolarPattern::getSpeedForBoat method is broken.
     */
    @Test
    public void acOptimalAngleTest() {
        double actual = acPattern.optimalAngle(new Coordinate(0, 0), new Coordinate(2, 2), 10);
        double expected = 65.4d; // angle from 0, 0 to 10, 10
        Assert.assertEquals(expected, actual, 0.1);
    }

}
