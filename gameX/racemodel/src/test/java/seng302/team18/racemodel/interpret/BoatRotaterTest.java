package seng302.team18.racemodel.interpret;

import cucumber.api.java.cs.A;
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

    @Test
    public void isUpwindTest() {
        Assert.assertTrue(rotater.isUpwind(boat, windDirection));
    }

    @Test
    public void isDownwindTest() {
        boat.setHeading(135);
        Assert.assertFalse(rotater.isUpwind(boat, windDirection));
    }

    @Test
    public void checkEastTestOne() {
        boat.setHeading(245);
        Assert.assertTrue(rotater.checkEast(boat.getHeading(), windDirection));
    }


    @Test
    public void checkEastTestThree() {
        boat.setHeading(315);
        Assert.assertTrue(rotater.checkEast(boat.getHeading(), 45));
    }

    @Test
    public void checkEastTestFour() {
        boat.setHeading(90);
        Assert.assertTrue(rotater.checkEast(boat.getHeading(), 225));
    }

    @Test
    public void checkWestTestOne() {
        boat.setHeading(135);
        Assert.assertFalse(rotater.checkEast(boat.getHeading(), windDirection));
    }

    @Test
    public void checkWestTestTwo() {
        Assert.assertFalse(rotater.checkEast(boat.getHeading(), windDirection));
    }

    @Test
    public void checkTackTestOne() {
        rotater.setTackGybe(windDirection, boat);
        Assert.assertEquals(315, boat.getHeading(), 0.1);
    }

    @Test
    public void checkTackTestTwo() {
        boat.setHeading(315);
        rotater.setTackGybe(windDirection, boat);
        Assert.assertEquals(45, boat.getHeading(),0.1);
    }


    /**
     * One to Four are Downwind East Gybe Tests.
     */
    @Test
    public void checkGybeTestOne() {
        boat.setHeading(235);
        rotater.setTackGybe(windDirection, boat);
        Assert.assertEquals(125, boat.getHeading(), 0.1);
    }

    @Test
    public void checkGybeTestTwo() {
        rotater.setTackGybe(135, boat);
        Assert.assertEquals(225, boat.getHeading(), 0.1);
    }

    @Test
    public void checkGybeTestThree() {
        boat.setHeading(90);
        rotater.setTackGybe(225, boat);
        Assert.assertEquals(0, boat.getHeading(), 0.1);
    }

    @Test
    public void checkGybeTestFour() {
        boat.setHeading(45);
        rotater.setTackGybe(180, boat);
        Assert.assertEquals(315, boat.getHeading(), 0.1);
    }

    /**
     * The rest are downwind West Gybe Tests.
     */
    @Test
    public void checkGybeTestFive() {
        boat.setHeading(125);
        rotater.setTackGybe(windDirection, boat);
        Assert.assertEquals(235, boat.getHeading(), 0.1);
    }

    @Test
    public void checkGybeTestSix() {
        boat.setHeading(0);
        rotater.setTackGybe(225, boat);
        Assert.assertEquals(90, boat.getHeading(), 0.1);
    }

    @Test
    public void checkGybeTestSeven() {
        boat.setHeading(60);
        rotater.setTackGybe(315, boat);
        Assert.assertEquals(210, boat.getHeading(), 0.1);
    }
}
