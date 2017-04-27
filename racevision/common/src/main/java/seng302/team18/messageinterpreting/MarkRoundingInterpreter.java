package seng302.team18.messageinterpreting;

import seng302.team18.data.AC35BoatLocationMessage;
import seng302.team18.data.AC35MarkRoundingMessage;
import seng302.team18.data.AC35MarkRoundingParser;
import seng302.team18.data.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.List;

/**
 * Created by dhl25 on 27/04/17.
 */
public class MarkRoundingInterpreter extends MessageInterpreter {

    private Race race;

    public MarkRoundingInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35MarkRoundingMessage) {
            AC35MarkRoundingMessage roundingMessage = (AC35MarkRoundingMessage) message;
            List<Boat> boats = race.getStartingList();
            for (Boat boat : boats) {
                if (boat.getId().equals(roundingMessage.getBoatId())) {
                    boat.setTimeAtLastMark(roundingMessage.getTime());
                }
            }
        }
    }
}
