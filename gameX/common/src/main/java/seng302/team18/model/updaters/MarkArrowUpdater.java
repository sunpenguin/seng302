package seng302.team18.model.updaters;

import seng302.team18.model.Boat;
import seng302.team18.model.Race;

/**
 * Class for rounding arrow for mark.
 */
public class MarkArrowUpdater implements Updater{
    @Override
    public void update(Race race, double time) {
        for (Boat boat : race.getStartingList()) {
            updateMarkArrow(boat, race);
        }
    }

    /**
     * Update the rounding arrow for the target mark for a boat.
     *
     * @param boat current boat
     * @param race current race
     */
    private void updateMarkArrow(Boat boat, Race race) {
        boolean hasPassed = race.getDetector().hasPassedDestination(boat, race.getCourse());
        System.out.println(hasPassed);

        switch (race.getMode()) {
            case RACE:
                if (hasPassed) {
                    boat.setPassedDestination(true);
                } else {
                    boat.setPassedDestination(false);
                }
                break;
            case ARCADE:
                if (hasPassed) {
                    boat.setPassedDestination(true);
                } else {
                    boat.setPassedDestination(false);
                }
        }
    }
}
