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

//    @Test
//    // test to check that new boats added to a race cannot have the same name as any others
//    public void testAddBoat() {
//        Boat boat1 = new Boat("Oracle", "New Zealand");
//        Boat boat2 = new Boat("Oracle" , "USA");
//
//        Race raceTest = new Race();
//        raceTest.addBoat(boat1);
//        raceTest.addBoat(boat2);
//
//        assertEquals(raceTest.getStartingList().size(), 1);
//    }


}
