package seng302.team18.model.updaters;

import seng302.team18.model.*;

import java.util.ArrayList;
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

        List<Projectile> projectilesToRemove = new ArrayList<>();
        for (Projectile projectile : race.getProjectiles()) {
            if (boat.hasCollided(projectile.getBodyMass())) {
                PowerUp newStun = new StunPowerUp();
                newStun.setDuration(5000);
                boat.setPowerUp(newStun);
                boat.activatePowerUp();
                projectilesToRemove.add(projectile);
                race.addYachtEvent(new YachtEvent(System.currentTimeMillis(), boat.getId(), YachtEventCode.BOAT_COLLIDE_WITH_MARK));
            }
        }
        for (Projectile projectile : projectilesToRemove) {
            race.removeProjectile(projectile.getId());
        }
    }
}
