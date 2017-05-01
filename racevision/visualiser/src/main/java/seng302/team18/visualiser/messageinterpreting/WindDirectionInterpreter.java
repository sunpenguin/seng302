package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.messageparsing.AC35RaceStatusMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Race;

/**
 * A MessageInterpreter that takes a AC35RaceStatusMessage and updates the wind direction of a Race.
 */
public class WindDirectionInterpreter extends MessageInterpreter {

    private Race race;

    /**
     * Constructor for WindDirectionInterpreter. Takes a Race as a parameter which it updates every time a
     * AC35RaceStatusMessage is interpreted.
     * @param race to be updated.
     */
    public WindDirectionInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            double newDirection = statusMessage.getWindDirection();
            race.getCourse().setWindDirection(newDirection);
        }
    }
}