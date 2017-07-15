package seng302.team18.visualiser.messageinterpreting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35BoatLocationMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Test class for MarkLocationInterpreter
 */
public class MarkLocationInterpreterTest {
    private Race race;
    private MessageInterpreter interpreter;
    private MessageBody message;
    private Double delta = 0.001;

    @Before
    public void setUp() throws Exception {
        race = new Race();
        List<Mark> compoundMarks1 = new ArrayList<>(Arrays.asList(
                new Mark(1, new Coordinate(10, 10)),
                new Mark(2, new Coordinate(-10, -10))
        ));
        List<Mark> compoundMarks2 = new ArrayList<>(Arrays.asList(
                new Mark(3, new Coordinate(100, 100))
        ));
        List<Mark> compoundMarks3 = new ArrayList<>(Arrays.asList(
                new Mark(4, new Coordinate(0, 0)),
                new Mark(5, new Coordinate(0, 0))
        ));
        race.getCourse().setCompoundMarks(Arrays.asList(
                new CompoundMark("CM1", compoundMarks1, 1),
                new CompoundMark("CM2", compoundMarks2, 2),
                new CompoundMark("CM3", compoundMarks3, 3)
        ));
        interpreter = new BoatLocationInterpreter(race);
    }

    @Test
    public void interpretMarkRoundings1() {
        message = new AC35BoatLocationMessage(1, new Coordinate(20, 20), 10, 10, true);
        interpreter.interpret(message);
        AC35BoatLocationMessage lMessage = (AC35BoatLocationMessage) message;
        List<Mark> filteredMarks = race.getCourse().getMarks().stream()
                .filter(mark -> mark.getId().equals(lMessage.getSourceId()))
                .collect(Collectors.toList());
        for (Mark mark : filteredMarks) {
            mark.setCoordinate(lMessage.getCoordinate());
        }
        Assert.assertEquals(20, race.getCourse().getMarks().get(0).getCoordinate().getLatitude(), delta);
        Assert.assertEquals(20, race.getCourse().getMarks().get(0).getCoordinate().getLongitude(), delta);
    }

    @Test
    public void interpretMarkRoundings2() {
        message = new AC35BoatLocationMessage(3, new Coordinate(-20, -20), 10, 10, true);
        interpreter.interpret(message);
        AC35BoatLocationMessage lMessage = (AC35BoatLocationMessage) message;
        List<Mark> filteredMarks = race.getCourse().getMarks().stream()
                .filter(mark -> mark.getId().equals(lMessage.getSourceId()))
                .collect(Collectors.toList());
        for (Mark mark : filteredMarks) {
            mark.setCoordinate(lMessage.getCoordinate());
        }
        Assert.assertEquals(-20, race.getCourse().getMarks().get(2).getCoordinate().getLatitude(), delta);
        Assert.assertEquals(-20, race.getCourse().getMarks().get(2).getCoordinate().getLongitude(), delta);
    }

    @Test
    public void interpretMarkRoundings3() {
        message = new AC35BoatLocationMessage(5, new Coordinate(89, -100), 10, 10, true);
        interpreter.interpret(message);
        AC35BoatLocationMessage lMessage = (AC35BoatLocationMessage) message;
        List<Mark> filteredMarks = race.getCourse().getMarks().stream()
                .filter(mark -> mark.getId().equals(lMessage.getSourceId()))
                .collect(Collectors.toList());
        for (Mark mark : filteredMarks) {
            mark.setCoordinate(lMessage.getCoordinate());
        }
        Assert.assertEquals(89, race.getCourse().getMarks().get(4).getCoordinate().getLatitude(), delta);
        Assert.assertEquals(-100, race.getCourse().getMarks().get(4).getCoordinate().getLongitude(), delta);
    }
}