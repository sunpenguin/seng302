package seng302.team18.test_mock.interpret;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Race;

/**
 * Created by dhl25 on 6/07/17.
 */
public class BoatActionInterpreter extends MessageInterpreter {

    private Race race;
    private int id;

    public BoatActionInterpreter(Race race, int boatId) {
        this.race = race;
        this.id = boatId;
    }

    @Override
    public void interpret(MessageBody message) {
//        if (message instanceof BoatActionMessage) {
//            BoatActionMessage boatAction = (BoatActionMessage) message;
//
//        }
    }

}
