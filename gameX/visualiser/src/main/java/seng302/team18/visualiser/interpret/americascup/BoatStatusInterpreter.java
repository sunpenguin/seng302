package seng302.team18.visualiser.interpret.americascup;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35BoatStatusMessage;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.BoatStatus;
import seng302.team18.visualiser.ClientRace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Interpreter to interpret AC35RaceStatusMessages for a boat status
 */
public class BoatStatusInterpreter extends MessageInterpreter {

    private ClientRace race;
    private final Map<BoatStatus, List<Consumer<Boolean>>> callbacks = new HashMap<>();


    public BoatStatusInterpreter(ClientRace race) {
        this.race = race;
    }


    /**
     * Adds a callback to be called when a boat changes to the specified status. The callback is called with the parameter
     * set to true if the boat is the player's boat, else it is false.
     *
     * @param status   the target status
     * @param callback the callback
     */
    public void addCallback(BoatStatus status, Consumer<Boolean> callback) {
        callbacks.computeIfAbsent(status, key -> new ArrayList<>()).add(callback);
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;

            List<AC35BoatStatusMessage> boatStates = statusMessage.getBoatStatus();
            for (AC35BoatStatusMessage boatStatus : boatStates) {
                race.getStartingList()
                        .stream()
                        .filter(boat -> boat.getId().equals(boatStatus.getBoatId()))
                        .forEach(boat -> {
                            processCallback(boat, boatStatus.getBoatStatus());
                            boat.setStatus(boatStatus.getBoatStatus());
                            setLeg(boat, boatStatus.getLegNumber());
                        });
            }
        }
    }


    private void processCallback(Boat boat, BoatStatus newStatus) {
        boolean isStatusChange = !newStatus.equals(boat.getStatus());
        boolean isPlayerBoat = boat.getId() == race.getPlayerId();

        if (isStatusChange && callbacks.containsKey(newStatus)) {
            callbacks.get(newStatus).forEach(callback -> callback.accept(isPlayerBoat));
        }
    }


    private void setLeg(Boat boat, int realLegNumber) {
        race.setNextLeg(boat, realLegNumber);
    }
}
