package seng302.team18.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Smoke test to ensure that API-spec-dependant values are not changed
 */
public class MarkRoundingDirectionTest {
    private static final String PORT = "Port";
    private static final String STARBOARD = "Stbd";
    private static final String SP = "SP";
    private static final String PS = "PS";


    @Test
    public void fromValueTest() throws Exception {
        assertEquals("Enum value parsed from \"Port\" not as expected", MarkRounding.Direction.PORT, MarkRounding.Direction.fromValue("Port"));
        assertEquals("Enum value parsed from \"Stbd\" not as expected", MarkRounding.Direction.STARBOARD, MarkRounding.Direction.fromValue("Stbd"));
        assertEquals("Enum value parsed from \"SP\" not as expected", MarkRounding.Direction.SP, MarkRounding.Direction.fromValue("SP"));
        assertEquals("Enum value parsed from \"PS\" not as expected", MarkRounding.Direction.PS, MarkRounding.Direction.fromValue("PS"));
    }


    @Test
    public void toStringTest() throws Exception {
        assertEquals("String representation of MarkRounding.Direction.PORT not as expected", PORT, MarkRounding.Direction.PORT.toString());
        assertEquals("String representation of MarkRounding.Direction.STARBOARD not as expected", STARBOARD, MarkRounding.Direction.STARBOARD.toString());
        assertEquals("String representation of MarkRounding.Direction.SP not as expected", SP, MarkRounding.Direction.SP.toString());
        assertEquals("String representation of MarkRounding.Direction.PS not as expected", PS, MarkRounding.Direction.PS.toString());
    }
}