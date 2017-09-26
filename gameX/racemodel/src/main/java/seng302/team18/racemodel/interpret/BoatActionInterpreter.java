package seng302.team18.racemodel.interpret;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.BoatActionStatus;
import seng302.team18.message.MessageBody;
import seng302.team18.message.PowerType;
import seng302.team18.model.*;
import seng302.team18.util.VMGAngles;

import java.util.Arrays;
import java.util.Collection;

/**
 * The class used to interpret BoatActionMessage.
 */
public class BoatActionInterpreter extends MessageInterpreter {

    private Race race;
    private int id;
    private Boat boat;
    private BoatRotater boatRotater;
    private final static Collection<BoatStatus> UNCONTROLLABLE_STATUSES = Arrays.asList(BoatStatus.OCS, BoatStatus.FINISHED);

    /**
     * Constructor for BoatActionInterpreter.
     *
     * @param race   to modify
     * @param boatId of the controlled boat
     */
    public BoatActionInterpreter(Race race, int boatId) {
        this.race = race;
        this.id = boatId;
        this.boat = race.getBoat(boatId);
        this.boatRotater = new BoatRotater(boat);
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof BoatActionMessage) {
            BoatActionMessage actionMessage = (BoatActionMessage) message;
            boat = race.getBoat(id);
            boatRotater.setBoat(boat);
            if (!UNCONTROLLABLE_STATUSES.contains(boat.getStatus()) && actionMessage.getId() == id) {
                applyActions(boat, actionMessage);
            } else {
                boat.setSailOut(true);
            }
        }
    }


    /**
     * Applies actions within the message to the specified boat.
     *
     * @param boat    to be manipulated.
     * @param actions to be applied.
     */
    private void applyActions(Boat boat, BoatActionMessage actions) {
        byte action = actions.getAction();
        if (action == BoatActionStatus.DOWNWIND.action()) {
            boatRotater.rotateDownwind(race.getCourse().getWindDirection(), race.getCourse().getWindSpeed());
        } else if (action == BoatActionStatus.UPWIND.action()) {
            boatRotater.rotateUpwind(race.getCourse().getWindDirection(), race.getCourse().getWindSpeed());
        } else if (action == BoatActionStatus.AUTOPILOT.action()) {
            boatRotater.setVMG(race.getCourse().getWindDirection(), race.getCourse().getWindSpeed(), VMGAngles.DEAD_ZONE.getValue());
        } else if (action == BoatActionStatus.SAIL_IN.action()) {
            boat.setSailOut(false);
        } else if (action == BoatActionStatus.SAIL_OUT.action()) {
            boat.setSailOut(true);
        } else if (action == BoatActionStatus.TACK_GYBE.action()) {
            boatRotater.setTackGybe(race.getCourse().getWindDirection(), boat);
        } else if (action == BoatActionStatus.POWER_UP.action()) {
            if (boat.canActivatePower()) {
                boat.activatePowerUp();
                if (boat.getPowerUp().getType().equals(PowerType.SPEED)) {
                    race.addYachtEvent(new YachtEvent(System.currentTimeMillis(), boat.getId(), YachtEventCode.ACTIVATED_SPEED_BOOST));
                }
            }

        }
    }


}
