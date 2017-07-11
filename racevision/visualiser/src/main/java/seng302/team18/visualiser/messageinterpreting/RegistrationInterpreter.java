package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.message.MessageBody;
import seng302.team18.message.RegistrationMessage;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

/**
 * Created by David-chan on 30/06/17.
 */
public class RegistrationInterpreter extends MessageInterpreter {

    private Race race;

    public RegistrationInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof RegistrationMessage) {
            int sourceId = ((RegistrationMessage) message).getSourceId();
            race.setPlayerId(sourceId);
        }
    }
}
