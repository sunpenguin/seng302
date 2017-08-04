package seng302.team18.racemodel.interpret;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The class used to interpret BoatActionMessage.
 */
public class BoatActionInterpreter extends MessageInterpreter {

    private Race race;
    private int id;
    private List<Boat> boats;
    private BoatRotater boatRotater;

    /**
     * Constructor for BoatActionInterpreter.
     *
     * @param race   to modify
     * @param boatId of the controlled boat
     */
    public BoatActionInterpreter(Race race, int boatId) {
        this.race = race;
        this.id = boatId;
        this.boats = race.getStartingList()
                .stream()
                .filter(boat -> boat.getId().equals(id))
                .collect(Collectors.toList());
        this.boatRotater = new BoatRotater(boats, 3d);
    }


    @Override
    public void interpret(MessageBody message) {
        if (message != null && message instanceof BoatActionMessage) {
            BoatActionMessage actionMessage = (BoatActionMessage) message;
            for (Boat boat : boats) {
                applyActions(boat, actionMessage);
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
        final double DEAD_ZONE = 10d;
        if (actions.isDownwind()) {
            boatRotater.rotateDownwind(race.getCourse().getWindDirection(), race.getCourse().getWindSpeed());
        } else if (actions.isUpwind()) {
            boatRotater.rotateUpwind(race.getCourse().getWindDirection(), race.getCourse().getWindSpeed());
        } else if (actions.isAutopilot()) {
            boatRotater.setVMG(race.getCourse().getWindDirection(), race.getCourse().getWindSpeed(), DEAD_ZONE);
        } else if (actions.isSailsIn()) {
            boat.setSailOut(false);
        } else if (!actions.isSailsIn()) {
            boat.setSailOut(true);
        }
    }


}
