package seng302.team18.model.updaters;

import seng302.team18.model.*;

import java.util.ArrayList;
import java.util.List;

public class BumperBoatsHitUpdater implements Updater {


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
                boat.loseLife();
                boat.setCoordinate(race.getCenter());
                boat.setSailOut(true);
                
                projectilesToRemove.add(projectile);
                race.addYachtEvent(new YachtEvent(System.currentTimeMillis(), boat.getId(), YachtEventCode.BOAT_COLLIDE_WITH_MARK));
            }
        }
        for (Projectile projectile : projectilesToRemove) {
            race.removeProjectile(projectile.getId());
        }
    }
}
