package seng302.team18.model.updaters;

import seng302.team18.message.PowerType;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

/**
 * Class to update boats position and powerup usages
 */
public class BoatUpdater implements Updater {

    @Override
    public void update(Race race, double time) {
        for (Boat boat : race.getStartingList()) {
            boat.update(time);
            if (boat.isPowerActive() && boat.getPowerUp().getType().equals(PowerType.SHARK)) {
                race.addProjectile(boat, boat.getPowerUp().getType());
                boat.setPowerActive(false);
                boat.setPowerUp(null);
            }
        }
    }
}
