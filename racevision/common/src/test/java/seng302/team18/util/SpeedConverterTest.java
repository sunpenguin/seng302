package seng302.team18.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by dhl25 on 1/07/17.
 */
public class SpeedConverterTest {

    private SpeedConverter converter = new SpeedConverter();

    @Test
    public void mmsToKnotsTest() {
        double expected = 1.9438445;
        double actual = converter.mmsToKnots(1000);
        Assert.assertEquals(expected, actual, 0.01);
    }


    @Test
    public void knotsToMmsTest() {
        double expected = 514444d;
        double actual = converter.knotsToMms(1000);
        Assert.assertEquals(expected, actual, 0.01);
    }


    @Test
    public void knotsToMsTest() {
        double expected = 514.444;
        double actual = converter.knotsToMs(1000);
        Assert.assertEquals(expected, actual, 0.01);
    }

}
