package seng302.team18.model.updaters;

import seng302.team18.model.*;
import seng302.team18.util.GPSCalculator;

import java.util.Arrays;

/**
 * Class to update boats on power ups
 */
public class PowerUpUpdater implements Updater {

    /**
     * Updates all boats' power-ups
     *
     * @param race
     */
    @Override
    public void update(Race race) {
        if (race.getStatus() == RaceStatus.PRESTART) {
            addPowerUps(0, race);//TODO: get working
        }
        for (Boat boat : race.getStartingList()) {
            powerUpStuff(boat);
        }
    }

    /**
     * Randomly places power ups.
     *
     * @param powerUps number of power ups to place
     */
    public void addPowerUps(int powerUps, Race race) {
        GPSCalculator calculator = new GPSCalculator();
        for (int i = 0; i < powerUps; i++) {
            Coordinate randomPoint = calculator.randomPoint(race.getCourse().getCourseLimits());
            race.getCourse().getCompoundMarks().add(new CompoundMark("a", Arrays.asList(new Mark(i * 3 + 2, randomPoint)), i * 2 + 3));
        }
    }

    /**
     * Determines if a boat picked up a power up.
     *
     * @param boat
     */
    private void powerUpStuff(Boat boat) {

    }
}
