package seng302.team18.visualiser.interpret;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.util.SpeedConverter;
import seng302.team18.visualiser.ClientRace;

/**
 * Created by csl62 on 28/06/17.
 */
public class WindSpeedInterpreter extends MessageInterpreter {

    private ClientRace race;


    public WindSpeedInterpreter(ClientRace race) {
        this.race = race;
    }


    /**
     * @param message to be interpreted
     */
    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            double newSpeed = new SpeedConverter().mmsToKnots(statusMessage.getWindSpeed());
            race.getCourse().setWindSpeed(newSpeed);
        }
    }

}
