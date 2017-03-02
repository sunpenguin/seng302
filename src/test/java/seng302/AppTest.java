package seng302;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * HI
 */
public class AppTest 
{

    @Test
    public void testBoatName() {
        Boat testboat = new Boat("Enterprise", "Starfleet");
        assertSame(testboat.getBoatName(), "Enterprise");
    }


}
