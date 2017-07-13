package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Race;
import seng302.team18.util.SpeedConverter;

/**
 * Created by csl62 on 28/06/17.
 */
public class WindSpeedInterpreter extends MessageInterpreter {

    private Race race;

    public WindSpeedInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message){
        if (message instanceof AC35RaceStatusMessage){
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            double newSpeed = new SpeedConverter().mmsToKnots(statusMessage.getWindSpeed());
            race.getCourse().setWindSpeed(newSpeed);
        }
    }

}
