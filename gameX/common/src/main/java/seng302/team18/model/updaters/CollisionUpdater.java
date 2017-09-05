package seng302.team18.model.updaters;

import seng302.team18.model.*;
import seng302.team18.util.GPSCalculator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to handle collisions
 */
public class CollisionUpdater implements Updater {

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
        if (boat.getStatus().equals(BoatStatus.FINISHED)) return;

        List<AbstractBoat> objects = race.getStartingList().stream()
                .filter(b -> !b.getId().equals(boat.getId()))
                .filter(b -> !b.getStatus().equals(BoatStatus.FINISHED))
                .collect(Collectors.toList());
        objects.addAll(race.getCourse().getMarks());

        for (AbstractBoat object : objects) {
            if (boat.hasCollided(object.getBodyMass())) {
                handleCollision(boat.getBodyMass(), object.getBodyMass());
                notifyCollision(race, boat, object);
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


    /**
     * Registers a yacht event for the collision with the race.
     *
     * @param race   the race in which the collision occurred
     * @param party1 the first party in the collision
     * @param party2 the second party in the collision
     */
    private void notifyCollision(Race race, AbstractBoat party1, AbstractBoat party2) {
        long time = System.currentTimeMillis();

        if (party2.getType().equals(BoatType.YACHT)) {
            race.addYachtEvent(new YachtEvent(time, party1.getId(), YachtEventCode.BOAT_IN_COLLISION));
            race.addYachtEvent(new YachtEvent(time, party2.getId(), YachtEventCode.BOAT_IN_COLLISION));
        } else if (party2.getType().equals(BoatType.MARK)) {
            race.addYachtEvent(new YachtEvent(time, party1.getId(), YachtEventCode.BOAT_COLLIDE_WITH_MARK));
        }
    }

}
