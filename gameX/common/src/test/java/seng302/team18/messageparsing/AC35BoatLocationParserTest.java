package seng302.team18.messageparsing;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.AC35BoatLocationMessage;
import seng302.team18.model.Coordinate;


import static org.junit.Assert.*;

/**
 * Created by jds112 on 25/04/17.
 */
public class AC35BoatLocationParserTest {

    private AC35BoatLocationParser parser;
    private AC35BoatLocationMessage message;

    @Before
    public void setUp() throws Exception {
        byte[] bytes = {1, -123, 96, 47, 121, 79, 1, 105, 0, 0, 0, -117, -82, 0, 0, 1, -64, -93, 2, 41, 62, -45, 107, 8, 27, 0, 0, 0, 106, 91, 2, 0, -112, 2, 0, 0, 1, 91, 46, 34, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0};
        parser = new AC35BoatLocationParser();
        message = (AC35BoatLocationMessage) parser.parse(bytes);
    }

    @Test
    public void parse() throws Exception {
        int expectedID = 105;
        double expectedHead = 128.551025390625;
        double expectedSpeed = 17.0;
        int expectedLives = 2;
        boolean expectedSails = true;

        int actualID = message.getSourceId();
        double actualHead = message.getHeading();
        double actualSpeed = message.getSpeed();
        boolean actualSails = message.getSailsOut();
        int actualLives = message.getLives();

        assertEquals("The ID was not read correctly by the boat location parser.", expectedID, actualID);
        assertEquals("The heading was not read correctly by the boat location parser.", expectedHead, actualHead, 0.01);
        assertEquals("The speed was not read correctly by the boat location parser.", expectedSpeed, actualSpeed, 0.01);
        assertEquals("The lives were not read correctly by the boat location parser.", expectedLives, actualLives);
        assertEquals("The sails were not read correctly by the boat location parser.", expectedSails, actualSails);
    }

}