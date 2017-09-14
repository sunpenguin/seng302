package seng302.team18.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PickUp
 */
public class PickUpTest {
    private PickUp pickUp0 = new PickUp(0);
    private PickUp pickUp1 = new PickUp(1);
    private PickUp pickUp2 = new PickUp(2);


    @Test
    public void hasExpiredTest1() throws Exception {
        pickUp0.setTimeout(System.currentTimeMillis() + 1000);
        Assert.assertFalse(pickUp0.hasExpired());
        Thread.sleep(1001);
        Assert.assertTrue(pickUp0.hasExpired());
    }


    @Test
    public void hasExpiredTest2() throws Exception {
        pickUp1.setTimeout(System.currentTimeMillis() + 100);
        Assert.assertFalse(pickUp1.hasExpired());
        Thread.sleep(101);
        Assert.assertTrue(pickUp1.hasExpired());
    }


    @Test
    public void hasExpiredTest3() throws Exception {
        pickUp2.setTimeout(System.currentTimeMillis() + 1);
        Assert.assertFalse(pickUp2.hasExpired());
        Thread.sleep(2);
        Assert.assertTrue(pickUp2.hasExpired());
    }
}
