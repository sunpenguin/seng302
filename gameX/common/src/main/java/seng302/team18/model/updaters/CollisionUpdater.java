package seng302.team18.model.updaters;

import seng302.team18.model.*;
import seng302.team18.util.GPSCalculator;

import java.util.List;

public abstract class CollisionUpdater implements Updater {
    private double totalPushBack;


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
        if (boat.getStatus().equals(BoatStatus.FINISHED)) return;

        for (AbstractBoat object : getObstacles(boat, race)) {
            if (boat.hasCollided(object.getBodyMass())) {

                if (race.getMode() == RaceMode.BUMPER_BOATS) {
                    totalPushBack = 100; // meters
                } else {
                    totalPushBack = 25; // meters
                }

                handleCollision(boat.getBodyMass(), object.getBodyMass());
                notifyCollision(race, boat, object);
            }
        }
    }


    /**
     * @param boat the AbstractBoat which is the instigating party
     * @param race the race the collision occurs in
     * @return a list of obstacles
     */
    abstract protected List<AbstractBoat> getObstacles(AbstractBoat boat, Race race);


    /**
     * Handles the collision when one is detected
     * NOTE: Bumper car edition currently in play
     *
     * @param object   boat collision was detected from
     * @param obstacle obstacle the boat collided with
     */
    private void handleCollision(BodyMass object, BodyMass obstacle) {
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
