package seng302.team18.messageinterpreting;

import seng302.team18.messageparsing.AC35BoatLocationMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.Iterator;
import java.util.List;

/**
 * Created by dhl25 on 27/04/17.
 */
public class BoatLocationInterpreter extends MessageInterpreter {

    private Race race;

    public BoatLocationInterpreter(Race race) {
        this.race = race;
    }

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
//                System.out.println("Boat Heading: " + message.getHeading());
                    boat.setHeading(locationMessage.getHeading());
                    boat.setCoordinate(locationMessage.getCoordinate());
                }
            }
        }
    }
}
