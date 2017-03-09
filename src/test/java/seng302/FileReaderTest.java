package seng302;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * A test class for the FileReader class
 */
public class FileReaderTest {

    @Test
    public void testReadBoatListFile() {
        FileReader testFileReader = new FileReader();
        ArrayList<Boat> testList = new ArrayList<>(testFileReader.readBoatListFile("/testAc35.txt"));

        Boat testBoat = testList.get(0);

        assertEquals(testBoat.getBoatName(), "Emirates"); // Assert the file reader creates a boat with the correct name
        assertEquals(testBoat.getTeamName(), "New Zealand"); // Assert the file reader creates a boat with the correct team name
        assertEquals(testList.size(), 1); // Assert the file reader did not add a second boat with the same name
    }
}
