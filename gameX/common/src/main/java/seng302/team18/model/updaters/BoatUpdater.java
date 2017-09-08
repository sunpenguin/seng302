package seng302.team18.model.updaters;

import seng302.team18.message.PowerType;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

/**
 * Class to update boats position & powerup usages
 */
public class BoatUpdater implements Updater {

    @Override
    public void update(Race race) {
        for (Boat boat : race.getStartingList()) {
            boat.update(race.getUpdateTime());
            if (boat.isPowerActive() == true && boat.getPowerUp().getType().equals(PowerType.SHARK)) {
                race.addProjectile(boat.getHeading(), boat.getCoordinate(), boat.getPowerUp().getType());
                boat.setPowerActive(false);
                boat.setPowerUp(null);
            }
        }
    }
}
