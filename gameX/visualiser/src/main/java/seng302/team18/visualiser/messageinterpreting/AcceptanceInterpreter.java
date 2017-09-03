package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AcceptanceMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestType;
import seng302.team18.model.Race;

/**
 * The MarkLocationInterpreter that sets the client's player id
 *
 * @see MessageInterpreter
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
            RequestType requestType =  ((AcceptanceMessage) message).getRequestType();
            if (requestType.getCode() != race.getMode().getCode()) {
                //TODO Return user to title screen sbe67 15/8/2017
            }
            race.setPlayerId(sourceId);

        }
    }

}