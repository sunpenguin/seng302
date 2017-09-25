package seng302.team18.visualiser.interpret.unique;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.message.PowerTakenMessage;
import seng302.team18.model.Boat;
import seng302.team18.model.PowerUp;
import seng302.team18.visualiser.ClientRace;

/**
 * Created by dhl25 on 4/09/17.
 */
public class PowerTakenInterpreter extends MessageInterpreter {

    private ClientRace race;


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
                    powerUp.setDuration(takenMessage.getDuration());
                }
            }

            race.removePickUp(takenMessage.getPowerId());
        }
    }

}
