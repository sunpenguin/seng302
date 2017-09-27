package seng302.team18.visualiser.interpret.unique;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.message.PowerTakenMessage;
import seng302.team18.model.Boat;
import seng302.team18.model.PowerUp;
import seng302.team18.visualiser.ClientRace;

import java.util.function.Consumer;

/**
 * Interpreter for {@link PowerTakenMessage PowerTakenMessages}
 */
public class PowerTakenInterpreter extends MessageInterpreter {

    private ClientRace race;
    private Consumer<Boolean> callback;


    public PowerTakenInterpreter(ClientRace race) {
        this.race = race;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof PowerTakenMessage) {
            PowerTakenMessage takenMessage = (PowerTakenMessage) message;

            for (Boat boat : race.getStartingList()) {
                if (boat.getId() == takenMessage.getBoatId()) {
                    boat.setPowerUp(race.getPowerUp(takenMessage.getPowerId()));
                    PowerUp powerUp = boat.getPowerUp();
                    if (null != powerUp) {
                        processCallbacks(boat.getId() == race.getPlayerId());
                        powerUp.setDuration(takenMessage.getDuration());
                    }
                }
            }
            race.removePickUp(takenMessage.getPowerId());
        }
    }


    /**
     * Callback to make when a boat picks up a power up.
     *
     * @param callback the callback. Passed true if it was the player that picked up a power-up, else false
     */
    public void setCallback(Consumer<Boolean> callback) {
        this.callback = callback;
    }


    /**
     * Trigger callbacks
     *
     * @param isFromPlayer true if triggered by the player, else false
     */
    private void processCallbacks(boolean isFromPlayer) {
        if (callback != null) {
            callback.accept(isFromPlayer);
        }
    }
}
