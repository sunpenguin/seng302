package seng302.team18.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the Mark class
 */
public class MarkTest {

    @Test
    public void nameTest() {
        Integer expected = 100;
        Mark actual = new Mark(100, new Coordinate(1, 1));
        assertEquals(expected, actual.getId());
    }

    @Test
    public void coordinateTest() {
        Coordinate expected = new Coordinate(1, 1);
        Mark actual = new Mark(0, new Coordinate(1, 1));
        assertEquals(expected, actual.getCoordinate());

        expected = new Coordinate(2.5, 4);
        actual.setCoordinate(new Coordinate(2.5, 4));
        assertEquals(expected, actual.getCoordinate());
    }


}
