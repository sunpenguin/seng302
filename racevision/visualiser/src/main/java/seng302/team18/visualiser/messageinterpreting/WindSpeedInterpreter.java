package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Race;

/**
 * Created by csl62 on 28/06/17.
 */
public class WindSpeedInterpreter extends MessageInterpreter {

    private final double MMS_TO_KNOTS = 0.00194384;

    private Race race;

    public WindSpeedInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message){
        if (message instanceof AC35RaceStatusMessage){
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            double newSpeed = statusMessage.getWindSpeed() * MMS_TO_KNOTS;
            race.getCourse().setWindSpeed(newSpeed);
        }

    }

}
