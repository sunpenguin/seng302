package seng302.team18.util;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by csl62 on 24/07/17.
 */
public class XYPairTests {

    XYPair xy1 = new XYPair(10,5);
    XYPair xy2 = new XYPair(8, 10);
    XYPair xy3 = new XYPair(15, 12);
    XYPair xy4 = new XYPair(15, 6);


    @Test
    public void calculateDistanceX1GreaterThanX2Test() {
        double returned1 = xy1.calculateDistance(xy3);
        double returned2 = xy1.calculateDistance(xy4);
        double returned3 = xy2.calculateDistance(xy3);
        double returned4 = xy2.calculateDistance(xy4);

        double expected1 = 8.6;
        double expected2 = 5.099;
        double expected3 = 7.3;
        double expected4 = 8.06;

        assertEquals(expected1,returned1,0.05);
        assertEquals(expected2,returned2,0.05);
        assertEquals(expected3,returned3,0.05);
        assertEquals(expected4,returned4,0.05);
    }


    @Test
    public void calculateDistanceX1LessThanX2Test() {
        double returned1 = xy3.calculateDistance(xy1);
        double returned2 = xy3.calculateDistance(xy2);
        double returned3 = xy4.calculateDistance(xy1);
        double returned4 = xy4.calculateDistance(xy2);

        double expected1 = 8.6;
        double expected2 = 7.3;
        double expected3 = 5.099;
        double expected4 = 8.06;

        assertEquals(expected1,returned1,0.05);
        assertEquals(expected2,returned2,0.05);
        assertEquals(expected3,returned3,0.05);
        assertEquals(expected4,returned4,0.05);
    }





}
