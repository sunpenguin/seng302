package seng302;

import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;


/**
 * A test class for the App class
 */
public class AppTest 
{

    @Test
    // test to check that new boats added to a race cannot have the same name as any others
    public void testAddBoat() {
        Boat boat1 = new Boat("Oracle", "New Zealand");
        Boat boat2 = new Boat("Oracle" , "USA");

        ArrayList<Boat> boatsToRace = new ArrayList<>();

        Race raceTest = new Race(boatsToRace, new Course("/testCourse.txt"));

        raceTest.addBoat(boat1);
        raceTest.addBoat(boat2);

        assertEquals(raceTest.getStartingList().size(), 1);
    }
}
