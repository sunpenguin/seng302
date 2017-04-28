package seng302.team18.messageparsing;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jds112 on 26/04/17.
 */
public class AC35RaceStatusParserTest {

    private AC35RaceStatusParser parser;
    private AC35RaceStatusMessage message;

    @Before
    public void setUp() throws Exception {
        byte[] bytes = {2, 40, -123, 50, 121, 79, 1, -107, 37, -26, 0, 3, 64, 87, 38, 121, 79, 1, -94, -90, -112, 24, 6,
                2, 101, 0, 0, 0, 2, 6, 0, 0, 13, 116, 51, 121, 79, 1, -52, 59, 63, 121, 79, 1, 102, 0, 0, 0, 2, 6, 0, 0,
                87, 56, 52, 121, 79, 1, 22, 0, 64, 121, 79, 1, 103, 0, 0, 0, 2, 6, 0, 0, 37, 0, 52, 121, 79, 1, -28, -57,
                63, 121, 79, 1, 104, 0, 0, 0, 2, 5, 0, 0, 108, -2, 50, 121, 79, 1, 80, -89, 64, 121, 79, 1, 105, 0, 0, 0,
                2, 5, 0, 0, 87, -126, 51, 121, 79, 1, 59, 43, 65, 121, 79, 1, 106, 0, 0, 0, 2, 6, 0, 0, 9, -51, 51, 121,
                79, 1, -55, -108, 63, 121, 79, 1,};
        parser = new AC35RaceStatusParser();
        message = (AC35RaceStatusMessage) parser.parse(bytes);
    }

    @Test
    public void parse() throws Exception {
        long expectedCurrentTime = 1440847398184L;
        long expectedStartTime = 1440846600000L;
        long actualCurrentTime = message.getCurrentTime();
        long actualStartTime = message.getStartTime();
        assertEquals("The race status parser did not correctly read the current time.", expectedCurrentTime, actualCurrentTime);
        assertEquals("The race status parser did not correctly read the start time.", expectedStartTime, actualStartTime);

    }

}