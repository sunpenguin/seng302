package seng302.team18.test_mock.config;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import seng302.team18.test_mock.InvalidPlayerNumberException;
import seng302.team18.test_mock.MockDataStream;

import java.io.IOException;

/**
 * Test class for verifying readConfig file is being read correctly
 */
public class ConfigTest {

    private int START_WAIT_TIME;
    private int WARNING_WAIT_TIME;
    private int PREP_WAIT_TIME;
    private int MAX_PLAYERS;


    @Before
    public void setup() {
        try {
            MockDataStream.readConfig("/configTest.txt");
            START_WAIT_TIME = MockDataStream.getStartWaitTime();
            WARNING_WAIT_TIME = MockDataStream.getWarningWaitTime();
            PREP_WAIT_TIME = MockDataStream.getPrepWaitTime();
            MAX_PLAYERS = MockDataStream.getMaxPlayers();
        } catch (Exception e) {
        }
    }


    @Test
    public void verifyStartWaitTime() {
        assertEquals(START_WAIT_TIME, 20);
    }


    @Test
    public void verifyWarningWaitTime() {
        assertEquals(WARNING_WAIT_TIME, 10);
    }


    @Test
    public void verifyPrepWaitTime() {
        assertEquals(PREP_WAIT_TIME, 5);
    }


    @Test
    public void verifyMaxPlayers() {
        assertEquals(MAX_PLAYERS, 2);
    }


    /**
     * Test file contains an invalid MAX_PLAYERS field.
     * Test is to ensure the InvalidPlayerNumberException is caught.
     */
    @Test(expected = InvalidPlayerNumberException.class)
    public void invalidPlayerNumberTest() throws IOException, InvalidPlayerNumberException {
        MockDataStream.readConfig("/configTest_Invalid_MAX_PLAYERS.txt");
    }
}
