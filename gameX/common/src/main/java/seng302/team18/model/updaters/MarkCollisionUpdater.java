package seng302.team18.model.updaters;

import seng302.team18.model.*;
import seng302.team18.util.GPSCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbe67 on 5/09/17.
 */
public class MarkCollisionUpdater implements Updater {

    @Override
    public void update(Race race) {
        for (Boat boat : race.getStartingList()) {
            detectCollision(boat, race);
        }
    }


    /**
     * Detects if there has been a collision between the boat and another abstract boat after updating the position
     *
     * @param boat to be collision stuffed
     */
    private void detectCollision(Boat boat, Race race) {
        List<BodyMass> objects = new ArrayList<>();

        for (Mark mark : race.getCourse().getMarks()) {
            objects.add(mark.getBodyMass());
        }
        for (BodyMass object : objects) {
            if (boat.hasCollided(object)) {
                handleCollision(boat.getBodyMass(), object);
            }
        }
    }


    /**
     * Handles the collision when one is detected by printing to the console
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
