package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35YachtEventMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.YachtEventCode;
import seng302.team18.visualiser.ClientRace;

/**
 * Interprets YachtEvent messages.
 */
public class YachtEventInterpreter extends MessageInterpreter {
    private ClientRace race;


    /**
     * @param race the race to be updated.
     */
    public YachtEventInterpreter(ClientRace race) {
        this.race = race;
    }


    /**
     * @param message to be interpreted. Of type AC35YachtEventMessage.
     * @see seng302.team18.message.AC35YachtEventMessage
     */
    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35YachtEventMessage) {
            AC35YachtEventMessage eventMessage = (AC35YachtEventMessage) message;
            switch (YachtEventCode.ofCode((byte) eventMessage.getType())) {
                case BOAT_IN_COLLISION:
                case BOAT_COLLIDE_WITH_MARK:
                    race.getBoat(eventMessage.getBoatId()).setHasCollided(true);
            }
        }
    }
}
