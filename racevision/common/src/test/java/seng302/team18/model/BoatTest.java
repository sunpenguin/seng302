package seng302.team18.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A test class for the boat class
 */
public class BoatTest {

    @Test
    public void testBoatName() {
        Boat testboat = new Boat("Enterprise", "Starfleet", 10);
        assertEquals("Enterprise", testboat.getBoatName());
    }

}
