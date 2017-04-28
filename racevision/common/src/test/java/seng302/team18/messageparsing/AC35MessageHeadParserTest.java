package seng302.team18.messageparsing;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jds112 on 26/04/17.
 */
public class AC35MessageHeadParserTest {

    private AC35MessageHeadParser parser;
    private AC35MessageHead message;
    byte[] headerBytes;

    @Before
    public void setUp() throws Exception {
        byte[] bytes = {71, -125, 44, 113, 80, 107, 28, 83, 1, -54, 0, 0, 0, 63, 0}; //get from file?
        headerBytes = bytes;
    }

    @Test
    public void parse() throws Exception {
        parser = new AC35MessageHeadParser();
        message = (AC35MessageHead) parser.parse(headerBytes);
        int expectedType = 44;
        int actualType = message.getType();
        System.out.println(actualType);
        assertEquals("The header message parser did not read the type as predicted.", expectedType, actualType);
    }

}