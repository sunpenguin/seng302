package seng302;

import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * HI
 */
public class AppTest 
{
    @Test
    public void testApp()
    {
        assertTrue(true);
    }

    @Test
    public void testBoatName() {
        Boat testboat = new Boat("Enterprise");
        assertSame(testboat.getName(), "Enterprise");
    }


}
