package seng302.team18.messageinterpreting;

import seng302.team18.data.AC35RaceStatusMessage;
import seng302.team18.data.MessageBody;
import seng302.team18.model.Race;

/**
 * Created by dhl25 on 27/04/17.
 */
public class WindDirectionInterpreter extends MessageInterpreter {

    private Race race;

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
