package seng302;

import org.junit.Test;
import seng302.model.Boat;

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
