package seng302.team18.model.updaters;

import seng302.team18.model.Boat;
import seng302.team18.model.BoatStatus;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Race;
import seng302.team18.util.GPSCalculator;

import java.util.List;

/**
 * Updates a players health when they leave the bounds of the course
 */
public class BumperBoatHealthUpdater implements Updater {

    @Override
    public void update(Race race) {
        for (Boat boat : race.getStartingList()) {
            if (isOutSide(boat, race)) {
                boat.loseLife();
                boat.setCoordinate(race.getStartPosition(boat, 3 * boat.getLength()));
                boat.setSailOut(true);

                if (boat.getLives() < 1) {
                    boat.setStatus(BoatStatus.DSQ);
                    race.setChanged();
                    race.notifyObservers(boat);
                }
            }
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
