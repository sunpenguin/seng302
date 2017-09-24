package seng302.team18.visualiser.interpret.americascup;


import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35YachtEventMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.YachtEventCode;
import seng302.team18.visualiser.ClientRace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Interprets YachtEvent messages.
 */
public class YachtEventInterpreter extends MessageInterpreter {

    private ClientRace race;
    private final Map<YachtEventCode, List<Consumer<Boolean>>> callbacks = new HashMap<>();


    /**
     * @param race the race to be updated.
     */
    public YachtEventInterpreter(ClientRace race) {
        this.race = race;
    }


    /**
     * Adds a callback to be called when the given yacht event occurs. The callback is passed a boolean parameter,
     * which is true if the event is for the player-controlled yacht, else false.
     *
     * @param eventCode the target event type
     * @param callback  the callback
     */
    public void addCallback(YachtEventCode eventCode, Consumer<Boolean> callback) {
        callbacks.computeIfAbsent(eventCode, key -> new ArrayList<>()).add(callback);
    }


    /**
     * @param message to be interpreted. Of type AC35YachtEventMessage.
     * @see seng302.team18.message.AC35YachtEventMessage
     */
    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35YachtEventMessage) {
            AC35YachtEventMessage eventMessage = (AC35YachtEventMessage) message;

            switch (eventMessage.getEventCode()) {
                case BOAT_IN_COLLISION:
                case BOAT_COLLIDE_WITH_MARK:
                    race.getBoat(eventMessage.getBoatId()).setHasCollided(true);
            }

            if (callbacks.containsKey(eventMessage.getEventCode())) {
                callbacks.get(eventMessage.getEventCode()).forEach(callback -> callback.accept(race.getPlayerId() == eventMessage.getBoatId()));
            }
        }
    }
}
