package seng302.team18.visualiser.interpret.americascup;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.RaceStatus;
import seng302.team18.visualiser.ClientRace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


/**
 * Interpreter to check if a race has been completed.
 */
public class RaceStatusInterpreter extends MessageInterpreter {

    private final ClientRace race;
    private final Map<RaceStatus, List<Consumer<Boolean>>> callbacks = new HashMap<>();

    /**
     * Constructor for a RaceStatusInterpreter.
     *
     * @param race the race
     */
    public RaceStatusInterpreter(ClientRace race) {
        this.race = race;
    }


    /**
     * Adds a callback to be called when the race status changes to the given status
     *
     * @param status   the target status
     * @param callback the callback
     */
    public void addCallback(RaceStatus status, Consumer<Boolean> callback) {
        callbacks.computeIfAbsent(status, key -> new ArrayList<>()).add(callback);
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            RaceStatus statusCode = RaceStatus.from(statusMessage.getRaceStatus());

            boolean isChangedStatus = !race.getStatus().equals(statusCode);
            race.setStatus(statusCode);
            if (isChangedStatus && callbacks.containsKey(statusCode)) {
                callbacks.get(statusCode).forEach(callback -> callback.accept(true));
            }
        }
    }
}
