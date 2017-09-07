package seng302.team18.model.updaters;

import seng302.team18.model.*;
import seng302.team18.util.GPSCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle collisions
 */
public class BoatCollisionUpdater implements Updater {

    @Override
    public void update(Race race, double time) {
        for (Boat boat : race.getStartingList()) {
            detectCollision(boat, race);
        }
    }


    /**
     * Detects if there has been a collision between the boat and another boat
     *
     * @param boat to be collision stuffed
     */
    private void detectCollision(Boat boat, Race race) {
        List<BodyMass> objects = new ArrayList<>();
        for (Boat b : race.getStartingList()) {
            if (!b.getId().equals(boat.getId()) &&
                    !(b.getStatus() == BoatStatus.FINISHED || boat.getStatus() == BoatStatus.FINISHED)) {
                objects.add(b.getBodyMass());
            }
        }

        for (BodyMass object : objects) {
            if (boat.hasCollided(object)) {
                handleCollision(boat.getBodyMass(), object);
            }
        }
    }


    /**
     * Handles the collision when one is detected
     * NOTE: Bumper car edition currently in play
     *
     * @param object   boat collision was detected from
     * @param obstacle obstacle the boat collided with
     */
    private void handleCollision(BodyMass object, BodyMass obstacle) {
        final double totalPushBack = 25; // meters
        GPSCalculator calculator = new GPSCalculator();
        double bearingOfCollision = calculator.getBearing(obstacle.getLocation(), object.getLocation());
        double ratio = object.getWeight() + obstacle.getWeight();
        double obstacleBackUpDistance = totalPushBack * (object.getWeight() / ratio);
        double objectBackUpDistance = totalPushBack * (obstacle.getWeight() / ratio);
        Coordinate newBoatCoordinate = calculator.toCoordinate(object.getLocation(), bearingOfCollision, objectBackUpDistance);
        object.setLocation(newBoatCoordinate);
        Coordinate newObstacleCoordinate = calculator.toCoordinate(obstacle.getLocation(), bearingOfCollision, -obstacleBackUpDistance);
        obstacle.setLocation(newObstacleCoordinate);
    }

}
