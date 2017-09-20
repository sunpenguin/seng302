package seng302.team18.visualiser.interpret.unique;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.message.PowerUpMessage;
import seng302.team18.model.*;
import seng302.team18.visualiser.ClientRace;

import java.util.List;
import java.util.stream.Collectors;

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
            if (isNew(powerMessage)) {
                PickUp pickUp = makePickUp(powerMessage);
                PowerUp powerUp = makePowerUp(powerMessage);
                pickUp.setPower(powerUp);
                race.getCourse().addPickUp(pickUp);
            } else {
                PickUp pickUp = race.getPickUp(powerMessage.getId());
                BodyMass bodyMass = new BodyMass();
                bodyMass.setLocation(powerMessage.getLocation());
                bodyMass.setRadius(powerMessage.getRadius());
                pickUp.setBodyMass(bodyMass);
                pickUp.setTimeout(powerMessage.getTimeout());
                pickUp.setPower(makePowerUp(powerMessage));
            }
        }
    }


    /**
     * Determines if a power up is new or old.
     *
     * @param message not null
     * @return the answer.
     */
    private boolean isNew(PowerUpMessage message) {
        List<Integer> ids = race.getPickUps()
                .stream()
                .map(PickUp::getId)
                .collect(Collectors.toList());
        return !ids.contains(message.getId());
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
        PowerUp powerUp = null;
        switch (message.getPower()) {
            case SPEED:
                powerUp = new SpeedPowerUp(3);
                powerUp.setDuration(message.getDuration());
                return powerUp;
            case SHARK:
                powerUp = new SharkPowerUp();
                powerUp.setDuration(message.getDuration());
                return powerUp;
            default:
                System.out.println("PowerUpInterpreter::makePowerUp has gone horribly wrong (ask Sunguin for help)");
                return null;
        }
    }
}
