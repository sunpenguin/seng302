package seng302.team18.model.updaters;

import seng302.team18.model.*;

import java.util.Iterator;
import java.util.List;

/**
 * Class to detect if a projectile has hit a boat
 */
public class ProjectileHitUpdater implements Updater {

    @Override
    public void update(Race race, double time) {
        for(Boat boat : race.getStartingList()) {
            detectHit(boat, race);
        }
    }

    private void detectHit(Boat boat, Race race) {
        if (boat.getStatus().equals(BoatStatus.FINISHED)) return;
        for (Iterator<Projectile> it = race.getProjectiles().iterator(); it.hasNext();) {
            Projectile projectile = it.next();
            if (boat.hasCollided(projectile.getBodyMass())) {
                it.remove();
                PowerUp newStun = new StunPowerUp();
                newStun.setDuration(5000);
                boat.setPowerUp(newStun);
                boat.activatePowerUp();
            }
        }


    }


}
