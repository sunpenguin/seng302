package seng302.team18.racemodel.generate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by dhl25 on 5/09/17.
 */
public class PowerTakenGeneratorTest {

    private byte[] expected;
    private MessageGenerator generator;

    @Before
    public void setUp() {
//        PickUp pickUp = new PickUp(1) {
//            @Override
//            public PowerType getType() {
//                return PowerType.SPEED;
//            }
//
//            @Override
//            public double getPowerDuration() {
//                return 6;
//            }
//        };
//        pickUp.setBodyMass(new BodyMass(new Coordinate(1, 2), 3, 4));
//        pickUp.setTimeout(5);
//        generator = new PowerUpMessageGenerator(pickUp);
//        expected = new byte[] { 1, 0, 0, 0, 96, 11, -74, 0, -63, 22, 108, 1, -72, 11, 5, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0 };
        generator = new PowerTakenGenerator(1, 2, 3);
        expected = new byte[] { 1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0 };
    }



    @Test
    public void payloadTest() throws IOException {
        byte[] actual = generator.getPayload();
        Assert.assertArrayEquals(expected, actual);
    }

}
