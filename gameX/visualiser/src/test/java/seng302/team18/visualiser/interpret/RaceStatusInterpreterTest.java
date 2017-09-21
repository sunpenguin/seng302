package seng302.team18.visualiser.interpret;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.model.RaceStatus;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.interpret.americascup.RaceStatusInterpreter;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Test class for EstimatedTimeInterpreter.
 */
public class RaceStatusInterpreterTest {

    private ClientRace race;
    private MessageInterpreter interpreter;
    private boolean didStart;
    private boolean didFinish;

    @Before
    public void setUp() {
        didStart = false;
        didFinish = false;
        race = new ClientRace();
        interpreter = new RaceStatusInterpreter(race, aBool -> didFinish = true, aBool -> didStart = true);
    }


    @Test
    public void raceStartTest() {
        race.setStatus(RaceStatus.PRESTART);
        interpreter.interpret(new AC35RaceStatusMessage(1, RaceStatus.STARTED.getCode(), 2, 0, 0, Collections.emptyList()));
        assertEquals(RaceStatus.STARTED, race.getStatus());
        assertTrue(didStart);
    }


    @Test
    public void raceAlreadyStarted() throws Exception {
        race.setStatus(RaceStatus.STARTED);
        interpreter.interpret(new AC35RaceStatusMessage(1, RaceStatus.STARTED.getCode(), 2, 0, 0, Collections.emptyList()));
        assertEquals(RaceStatus.STARTED, race.getStatus());
        assertFalse(didStart);
    }


    @Test
    public void raceNotStarted() throws Exception {
        race.setStatus(RaceStatus.PRESTART);
        interpreter.interpret(new AC35RaceStatusMessage(1, RaceStatus.PRESTART.getCode(), 2, 0, 0, Collections.emptyList()));
        assertEquals(RaceStatus.PRESTART, race.getStatus());
        assertFalse(didStart);
    }


    @Test
    public void raceFinishTest() {
        race.setStatus(RaceStatus.STARTED);
        interpreter.interpret(new AC35RaceStatusMessage(1, RaceStatus.FINISHED.getCode(), 2, 0, 0, Collections.emptyList()));
        assertEquals(RaceStatus.FINISHED, race.getStatus());
        assertTrue(didFinish);
    }

}
