package seng302.team18.model.updaters;

import seng302.team18.model.Projectile;
import seng302.team18.model.Race;

/**
 * Class to update projectiles
 */
public class ProjectileUpdater implements Updater  {

    @Override
    public void update(Race race, double time) {
        for(Projectile projectile : race.getProjectiles()) {
            projectile.update(time);
        }
    }
}
