package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.List;

/**
 * The BoatActionInterpreter that sets boat's sail status.
 */
public class BoatActionInterpreter extends MessageInterpreter {
    private Race race;

    /**
     * Constructor for  BoatActionInterpreter.
     * @param race Race, race
     */
    public BoatActionInterpreter(Race race) {
        this.race = race;
    }


    /**
     * Interpret method for BoatActionInterpreter. Gets the boat's sail in status from the message.
     *
     * @param message to be interpreted
     */
    @Override
    public void interpret(MessageBody message) {
        if (message instanceof BoatActionMessage) {
            BoatActionMessage actionMessage = (BoatActionMessage) message;
            List<Boat> boats = race.getStartingList();
            for (Boat boat : boats) {
                if (actionMessage.isSailsIn())
                boat.setSailOut(false);
            }
        }
    }
}
