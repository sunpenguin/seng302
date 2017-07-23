package seng302.team18.test_mock.interpret;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.IBoat;
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

    /**
     * Constructor for BoatActionInterpreter.
     *
     * @param race to modify
     * @param boatId of the controlled boat
     */
    public BoatActionInterpreter(Race race, int boatId) {
        this.race = race;
        this.id = boatId;
        this.boats = race.getStartingList()
                .stream()
                .filter(boat -> boat.getId().equals(id))
                .collect(Collectors.toList());
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
     * @param boat to be manipulated.
     * @param actions to be applied.
     */
    private void applyActions(Boat boat, BoatActionMessage actions) {
        final double headingChange = 3d;

        if (actions.isDownwind()) {
            double windDirection = race.getCourse().getWindDirection();
            double windSpeed = race.getCourse().getWindSpeed();
            double newHeading = headTowardsWind(boat.getHeading(), windDirection, headingChange);

            boat.setHeading(newHeading);
            boat.setSpeed(boat.getBoatTWS(windSpeed, windDirection));
        } else if (actions.isUpwind()) {
            double windSpeed = race.getCourse().getWindSpeed();
            double windDirection = race.getCourse().getWindDirection();
            double flippedWindDirection = (windDirection + 180) % 360; // flipping wind direction
            double newHeading = headTowardsWind(boat.getHeading(), flippedWindDirection, headingChange);

            boat.setHeading(newHeading);
            boat.setSpeed(boat.getBoatTWS(windSpeed, windDirection));
        } else if (actions.isSailsIn()) {
            boat.setSailOut(false);
        } else if (!actions.isSailsIn()) {
            boat.setSailOut(true);
        }

        boat.setSailOut(!actions.isSailsIn());
    }


    /**
     * Finds the new angle a boat should travel at to move towards the wind if headingChange is positive
     * or away from the wind if headingChange is negative.
     *
     * @param boatHeading the boats current heading.
     * @param windHeading heading of the wind.
     * @param headingChange how much the boat should head towards the boat.
     * @return double, the new angle.
     */
    private double headTowardsWind(double boatHeading, double windHeading, double headingChange) {
        double boatToWindClockwiseAngle = boatHeading - windHeading;

        if (boatToWindClockwiseAngle < 0) {
            boatToWindClockwiseAngle += 360;
        }

        if (boatToWindClockwiseAngle < 180) {
            return boatHeading + headingChange;
        }

        double newHeading = boatHeading - headingChange;

        if (newHeading < 0) {
            newHeading += 360;
        }

        return newHeading;
    }

}