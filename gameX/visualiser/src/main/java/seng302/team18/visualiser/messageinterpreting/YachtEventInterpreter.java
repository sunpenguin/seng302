package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35YachtEventMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.*;

import java.util.List;

/**
 * Interprets a YachtEventMessage to set the boat status if a boat has been penalised..
 */
public class YachtEventInterpreter extends MessageInterpreter {

    private Race race;

    /**
     * Constructor for YachtEventInterpreter. Takes a Race as a parameter which it updates every time a
     * YachtEventMessage is interpreted.
     *
     * @param race to be updated.
     */
    public YachtEventInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof YachtEvent) {
            AC35YachtEventMessage eventMessage = (AC35YachtEventMessage) message;
            List<Boat> boats = race.getStartingList();
            for (Boat boat : boats) {
                if (boat.getId().equals(eventMessage.getBoatId())) {
                    if (eventMessage.getEventCode().equals(YachtEventCode.OVER_START_LINE_EARLY)) {
                        boat.setTimeAtLastMark(eventMessage.getTime());
                        boat.setStatus(BoatStatus.OCS);
                    } else if (eventMessage.getEventCode().equals(YachtEventCode.OCS_PENALTY_COMPLETE)) {
                        boat.setStatus(BoatStatus.RACING);
                    }
                }
            }
        }
    }
}
