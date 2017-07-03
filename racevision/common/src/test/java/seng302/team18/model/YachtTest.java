package seng302.team18.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A test class for the boat class
 */
public class YachtTest {

    @Test
    public void testBoatName() {
        Yacht testboat = new Yacht("Enterprise", "Starfleet", 10);
        assertEquals("Enterprise", testboat.getName());
    }

}
