package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.message.AcceptanceMessage;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

/**
 * Class to interpreting acceptance messages sent from the model to the visualiser
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
