package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.message.AC35BoatLocationMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Yacht;
import seng302.team18.model.Race;

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
            List<Yacht> yachts = race.getStartingList();
            if (yachts.size() > 0) {
                Iterator<Yacht> boatIterator = yachts.iterator();
                Yacht yacht = boatIterator.next();
                while (!yacht.getId().equals(locationMessage.getSourceId()) && boatIterator.hasNext()) {
                    yacht = boatIterator.next();
                }
                if (yacht.getId().equals(locationMessage.getSourceId())) {
                    yacht.setSpeed(locationMessage.getSpeed());
                    yacht.setKnotsSpeed(locationMessage.getSpeed() * 0.539957);
                    yacht.setHeading(locationMessage.getHeading());
                    yacht.setCoordinate(locationMessage.getCoordinate());
                }
            }
        }
    }
}
