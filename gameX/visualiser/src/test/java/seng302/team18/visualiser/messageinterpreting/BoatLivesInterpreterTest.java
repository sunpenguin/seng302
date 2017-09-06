package seng302.team18.visualiser.messageinterpreting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.AC35BoatLocationMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Coordinate;
import seng302.team18.visualiser.ClientRace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to test boatLivesInterpreter
 */
public class BoatLivesInterpreterTest {

    private ClientRace race;
    private List<Boat> startingList;
    private BoatLivesInterpreter interpreter;
    private MessageBody message;

    private Double delta = 0.001;

    @Before
    public void setUp() {
        race = new ClientRace();
        Boat boat1 = new Boat("Big Boat", "BB", 420, 1);
        Boat boat2 = new Boat("Medium Boat", "MB", 100, 1);
        Boat boat3 = new Boat("Small Boat", "SB", 077, 1);
        startingList = new ArrayList<>(Arrays.asList(boat1, boat2, boat3));
        race.setParticipantIds(Arrays.asList(420, 100, 077));
        race.setStartingList(startingList);
        interpreter = new BoatLivesInterpreter(race);
    }

    @Test
    public void interpreteBoatLivesTest1(){
        message = new AC35BoatLocationMessage(420, new Coordinate(20, 20), 100, 10, true, 3);
        interpreter.interpret(message);
        Boat boatToCheck = race.getStartingList().get(0);
        Assert.assertEquals(3, boatToCheck.getLives(), delta);
    }

    @Test
    public void interpreteBoatLivesTest2(){
        message = new AC35BoatLocationMessage(100, new Coordinate(20, 20), 100, 10, true, 2);
        interpreter.interpret(message);
        Boat boatToCheck = race.getStartingList().get(1);
        Assert.assertEquals(2, boatToCheck.getLives(), delta);
    }

    @Test
    public void interpreteBoatLivesTest3(){
        message = new AC35BoatLocationMessage(077, new Coordinate(20, 20), 100, 10, true, 0);
        interpreter.interpret(message);
        Boat boatToCheck = race.getStartingList().get(2);
        Assert.assertEquals(0, boatToCheck.getLives(), delta);
    }


}
