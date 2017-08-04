package seng302.team18.racemodel.interpret;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.model.Boat;

import java.util.Collections;

/**
 * Created by dhl25 on 23/07/17.
 */
public class BoatRotaterTest {

    private Boat boat;
    private BoatRotater rotater;
    private final double rotation = 3.0d;
    private final double deadZone = 10.0d;
    private final double windDirection = 0.0d;
    private final double windSpeed = 10.0d;

    @Before
    public void setUp() {
        boat = new Boat("name", "nm", 10, 14.019);
        boat.setHeading(45.0d);
        rotater = new BoatRotater(Collections.singletonList(boat), rotation);
    }


    @Test
    public void setVMGPassTest() {
        double expectedHeading = 43.0d;
        double expectedSpeed = 10.0d;
        rotater.setVMG(windDirection, windSpeed, deadZone);
        Assert.assertEquals(expectedHeading, boat.getHeading(), 0.1);
        Assert.assertEquals(expectedSpeed, boat.getSpeed(), 0.1);
    }


    @Test
    public void setVMGFailTest() {
        double expectedHeading = 91.0d;
        double expectedSpeed = 0.0d;
        boat.setHeading(91.d);
        rotater.setVMG(windDirection, windSpeed, deadZone);
        Assert.assertEquals(expectedHeading, boat.getHeading(), 0.1);
        Assert.assertEquals(expectedSpeed, boat.getSpeed(), 0.1);
    }


    @Test
    public void rotateUpwindTest() {
        double expectedHeading = 42.0d;
        double expectedSpeed = 11.95d;
        rotater.rotateUpwind(windDirection, windSpeed);
        Assert.assertEquals(expectedHeading, boat.getHeading(), 0.1);
        Assert.assertEquals(expectedSpeed, boat.getSpeed(), 0.1);
    }


    @Test
    public void rotateDownwindTest() {
        double expectedHeading = 48.0d;
        double expectedSpeed = 12.58d;
        rotater.rotateDownwind(windDirection, windSpeed);
        Assert.assertEquals(expectedHeading, boat.getHeading(), 0.1);
        Assert.assertEquals(expectedSpeed, boat.getSpeed(), 0.1);
    }

}
