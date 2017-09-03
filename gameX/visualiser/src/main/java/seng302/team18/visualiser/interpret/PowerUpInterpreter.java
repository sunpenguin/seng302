package seng302.team18.visualiser.interpret;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.message.PowerUpMessage;
import seng302.team18.model.*;
import seng302.team18.visualiser.ClientRace;

/**
 * Created by dhl25 on 3/09/17.
 */
public class PowerUpInterpreter extends MessageInterpreter {

    private ClientRace race;

    public PowerUpInterpreter(ClientRace race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof PowerUpMessage) {
            PowerUpMessage powerMessage = (PowerUpMessage) message;
            PickUp pickUp = makePickUp(powerMessage);
            PowerUp powerUp = makePowerUp(powerMessage);
            pickUp.setPower(powerUp);
            race.getCourse().addPickUp(pickUp);
        }
    }


    /**
     * Creates a pick up from a PowerUpMessage.
     *
     * @param message a PowerUpMessage
     * @return a pickup
     */
    private PickUp makePickUp(PowerUpMessage message) {
        PickUp pickUp = new PickUp(message.getId());
        pickUp.setTimeout(message.getTimeout());
        Coordinate location = new Coordinate(message.getLatitude(), message.getLongitude());
        BodyMass mass = new BodyMass(location, message.getRadius(), 0);
        pickUp.setBodyMass(mass);
        return pickUp;
    }


    /**
     * Creates a power up from a PowerUpMessage.
     *
     * @param message a PowerUpMessage
     * @return a power up
     */
    private PowerUp makePowerUp(PowerUpMessage message) {
        switch (message.getPower()) {
            case SPEED:
                PowerUp powerUp = new SpeedPowerUp();
                powerUp.setDuration(message.getDuration());
                return powerUp;
            default:
                System.out.println("PowerUpInterpreter::makePowerUp has gone horribly wrong (ask Sunguin for help)");
                return null;
        }
    }
}
