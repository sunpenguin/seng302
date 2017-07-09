package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AcceptanceMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Race;

/**
 * Created by David-chan on 30/06/17.
 */
public class AcceptanceInterpreter extends MessageInterpreter {

    private Race race;

    public AcceptanceInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AcceptanceMessage) {
            int sourceId = ((AcceptanceMessage) message).getSourceId();
            race.setPlayerId(sourceId);
        }
    }

}
