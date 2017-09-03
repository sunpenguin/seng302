package seng302.team18.visualiser.interpret;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.message.PowerUpMessage;
import seng302.team18.model.Course;

/**
 * Created by dhl25 on 3/09/17.
 */
public class PowerUpInterpreter extends MessageInterpreter {


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof PowerUpMessage) {
            PowerUpMessage powerMessage = (PowerUpMessage) message;

        }
    }
}
