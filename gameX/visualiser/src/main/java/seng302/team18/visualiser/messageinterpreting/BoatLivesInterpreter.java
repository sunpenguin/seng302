package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35BoatLocationMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.ClientRace;

import java.util.Iterator;
import java.util.List;

/**
 * The BoatLivesInterpreter that sets the number of lives a boat has.
 *
 * @see MessageInterpreter
 */
public class BoatLivesInterpreter extends MessageInterpreter {
    private ClientRace race;

    /**
     * Constructor for BoatLivesInterpreter.
     *
     * @param race Race, race
     */
    public BoatLivesInterpreter(ClientRace race) {
        this.race = race;
    }

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
                    while(boat.getLives() > actionMessage.getLives()) {
                        boat.loseLife();
                    }
                }
            }
        }

    }
}
