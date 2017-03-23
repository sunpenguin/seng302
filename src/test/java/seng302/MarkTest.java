package seng302;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the Mark class
 */
public class MarkTest {

    @Test
    public void nameTest() {
        String expected = "HAHAhOHo";
        Mark actual = new Mark("HAHAhOHo", new Coordinate(1, 1));
        assertEquals(expected, actual.getName());

        expected = "Lol123";
        actual.setName("Lol123");
        assertEquals(expected, actual.getName());
    }

    @Test
    public void coordinateTest() {
        Coordinate expected = new Coordinate(1, 1);
        Mark actual = new Mark("HAHAhOHo", new Coordinate(1, 1));
        assertEquals(expected, actual.getCoordinates());

        expected = new Coordinate(2.5, 4);
        actual.setCoordinates(new Coordinate(2.5, 4));
        assertEquals(expected, actual.getCoordinates());
    }


}
