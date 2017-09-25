package seng302.team18.visualiser.interpret.unique;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35BoatLocationMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.ClientRace;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * The BoatLivesInterpreter that sets the number of lives a boat has.
 *
 * @see MessageInterpreter
 */
public class BoatLivesInterpreter extends MessageInterpreter {

    private ClientRace race;
    private Consumer<Boolean> callback;

    /**
     * Constructor for BoatLivesInterpreter.
     *
     * @param race     Race, race
     * @param callback procedure to run when a boat loses a life
     */
    public BoatLivesInterpreter(ClientRace race, Consumer<Boolean> callback) {
        this.race = race;
        this.callback = callback;
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
                    boolean isPlayer = race.getPlayerId() == boat.getId();
                    while (boat.getLives() > actionMessage.getLives()) {
                        boat.loseLife();
                        callback.accept(isPlayer);
                    }
                }
            }
        }

    }
}
