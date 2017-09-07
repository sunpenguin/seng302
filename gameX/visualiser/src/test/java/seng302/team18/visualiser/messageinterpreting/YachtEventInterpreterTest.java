package seng302.team18.visualiser.messageinterpreting;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35YachtEventMessage;
import seng302.team18.model.Boat;
import seng302.team18.model.YachtEventCode;
import seng302.team18.visualiser.ClientRace;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class YachtEventInterpreterTest {

    private Boat boat100;
    private Boat boat101;
    private Boat boat102;
    private MessageInterpreter interpreter;


    @Before
    public void setUp() throws Exception {
        boat100 = new Boat("boat 100", "b100", 100, 5);
        boat101 = new Boat("boat 101", "b101", 101, 5);
        boat102 = new Boat("boat 102", "b102", 102, 5);

        ClientRace clientRace = new ClientRace();
        clientRace.setStartingList(Arrays.asList(boat100, boat101, boat102));
        interpreter = new YachtEventInterpreter(clientRace);
    }


    @Test
    public void boatInCollisionTest() throws Exception {
        Boat boat = boat100;
        assertFalse("boat incorrectly marked as in collision", boat.getHasCollided());

        AC35YachtEventMessage message = new AC35YachtEventMessage(System.currentTimeMillis(), boat.getId(), YachtEventCode.BOAT_IN_COLLISION);
        interpreter.interpret(message);
        assertTrue("boat not marked in collision when it has been", boat.getHasCollided());
    }


    @Test
    public void boatCollideWithMarkTest() throws Exception {
        Boat boat = boat100;
        assertFalse("boat incorrectly marked as in collision", boat.getHasCollided());

        AC35YachtEventMessage message = new AC35YachtEventMessage(System.currentTimeMillis(), boat.getId(), YachtEventCode.BOAT_COLLIDE_WITH_MARK);
        interpreter.interpret(message);
        assertTrue("boat not marked in collision when it has been", boat.getHasCollided());
    }


    @Test
    public void boatInCollisionTest_onlyBoat() throws Exception {
        AC35YachtEventMessage message = new AC35YachtEventMessage(System.currentTimeMillis(), boat100.getId(), YachtEventCode.BOAT_IN_COLLISION);
        interpreter.interpret(message);
        assertFalse("boat incorrectly marked as in collision", boat101.getHasCollided());
        assertFalse("boat incorrectly marked as in collision", boat102.getHasCollided());
    }


    @Test
    public void onlyCollisionCodesTest() throws Exception {
        interpreter.interpret(new AC35YachtEventMessage(System.currentTimeMillis(), boat100.getId(), YachtEventCode.OVER_START_LINE_EARLY));
        interpreter.interpret(new AC35YachtEventMessage(System.currentTimeMillis(), boat101.getId(), YachtEventCode.OCS_PENALTY_COMPLETE));
        interpreter.interpret(new AC35YachtEventMessage(System.currentTimeMillis(), boat102.getId(), YachtEventCode.CLEAR_BEHIND_START_AFTER_EARLY));

        assertFalse("boat incorrectly marked as in collision", boat100.getHasCollided());
        assertFalse("boat incorrectly marked as in collision", boat101.getHasCollided());
        assertFalse("boat incorrectly marked as in collision", boat102.getHasCollided());
    }
}