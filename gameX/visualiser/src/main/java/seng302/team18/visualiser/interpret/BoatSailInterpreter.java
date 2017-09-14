package seng302.team18.visualiser.interpret;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35BoatLocationMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.ClientRace;

import java.util.Iterator;
import java.util.List;

/**
 * The BoatSailInterpreter that sets boat's sail status.
 */
public class BoatSailInterpreter extends MessageInterpreter {
    private ClientRace race;


    /**
     * Constructor for  BoatSailInterpreter.
     *
     * @param race Race, race
     */
    public BoatSailInterpreter(ClientRace race) {
        this.race = race;
    }


    /**
     * Interpret method for BoatSailInterpreter. Gets the boat's sail in status from the message.
     *
     * @param message to be interpreted
     */
    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35BoatLocationMessage) {
            AC35BoatLocationMessage actionMessage = (AC35BoatLocationMessage) message;
            List<Boat> boats = race.getStartingList();
            if (boats.size() > 0) {
                Iterator<Boat> boatIterator = boats.iterator();
                Boat boat = boatIterator.next();
                while (!boat.getId().equals(actionMessage.getSourceId()) && boatIterator.hasNext()) {
                    boat = boatIterator.next();
                }
                if (boat.getId().equals(actionMessage.getSourceId())) {
                    boat.setSailOut(actionMessage.getSailsOut());
                }
            }
        }
    }
}
