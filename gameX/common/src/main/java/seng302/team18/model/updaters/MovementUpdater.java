package seng302.team18.model.updaters;

import seng302.team18.model.Boat;
import seng302.team18.model.Race;

/**
 * Class to update boats position
 */
public class MovementUpdater implements Updater {

    @Override
    public void update(Race race) {
        for (Boat boat : race.getStartingList()){
            boat.update(race.getUpdateTime());
        }
    }
}
