package seng302.team18.model.updaters;

import seng302.team18.model.*;
import seng302.team18.util.GPSCalculator;

import java.util.List;

/**
 * Class to update boats on bound attributes
 */
public class OutOfBoundsUpdater implements Updater {

    @Override
    public void update(Race race) {
        for (Boat boat : race.getStartingList()) {
            checkForBoundaryDSQ(boat, race);
        }
        for (Projectile projectile : race.getProjectiles()){
            GPSCalculator calculator = new GPSCalculator();
            List<Coordinate> boundaries = race.getCourse().getCourseLimits();

            if (!calculator.isInside(projectile.getLocation(), boundaries)) {
                race.removeProjectile(projectile.getId());
                break;
            }
        }
    }


    /**
     * Disqualifies boat if outside boundary.
     *
     * @param boat to check
     */
    private void checkForBoundaryDSQ(Boat boat, Race race) {
        if (isOutSide(boat, race)) {
            boat.setStatus(BoatStatus.DSQ);
            race.setChanged();
            race.notifyObservers(boat);
        }
    }


    /**
     * Check if a boat is outside the boundary
     *
     * @param boat to check
     * @return if the boat is outside.
     */
    private boolean isOutSide(Boat boat, Race race) {
        GPSCalculator calculator = new GPSCalculator();
        List<Coordinate> boundaries = race.getCourse().getCourseLimits();

        return boundaries.size() > 2
                && !calculator.isInside(boat.getCoordinate(), boundaries)
                && !(boat.getStatus().equals(BoatStatus.DSQ));
    }
}
