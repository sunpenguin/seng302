package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35BoatLocationMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.util.SpeedConverter;

import java.util.Iterator;
import java.util.List;

/**
 * The BoatLocationInterpreter that sets the boat speed, heading and coordinate.
 *
 * @see MessageInterpreter
 */
public class BoatLocationInterpreter extends MessageInterpreter {
    private Race race;

    /**
     * Constructor for BoatLocationInterpreter.
     *
     * @param race the race to be updated.
     */
    public BoatLocationInterpreter(Race race) {
        this.race = race;
    }

    /**
     * Interpret method for BoatLocationInterpreter. Gets the boat speed, heading and coordinate from the message.
     *
     * @param message to be interpreted. Of type AC35BoatLocationMessage.
     * @see AC35BoatLocationMessage
     */
    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35BoatLocationMessage) {
            AC35BoatLocationMessage locationMessage = (AC35BoatLocationMessage) message;
            List<Boat> boats = race.getStartingList();
            if (boats.size() > 0) {
                Iterator<Boat> boatIterator = boats.iterator();
                Boat boat = boatIterator.next();
                while (!boat.getId().equals(locationMessage.getSourceId()) && boatIterator.hasNext()) {
                    boat = boatIterator.next();
                }
                if (boat.getId().equals(locationMessage.getSourceId())) {
                    boat.setSpeed(locationMessage.getSpeed());
                    boat.setHeading(locationMessage.getHeading());
                    boat.setCoordinate(locationMessage.getCoordinate());
                }
            }
        }
    }
}
