package seng302;

import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * A test class for the boat class
 */
public class BoatTest {

    @Test
    public void testBoatName() {
        Boat testboat = new Boat("Enterprise", "Starfleet");
        assertSame(testboat.getBoatName(), "Enterprise");
    }

}
