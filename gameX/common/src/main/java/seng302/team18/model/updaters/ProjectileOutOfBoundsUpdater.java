package seng302.team18.model.updaters;

import seng302.team18.model.Coordinate;
import seng302.team18.model.Projectile;
import seng302.team18.model.Race;
import seng302.team18.util.GPSCalculator;

import java.util.List;

/**
 * Updater to remove any projctiles that have left the course limits
 */
public class ProjectileOutOfBoundsUpdater implements Updater {
    @Override
    public void update(Race race, double time) {
        for (Projectile projectile : race.getProjectiles()){
            GPSCalculator calculator = new GPSCalculator();
            List<Coordinate> boundaries = race.getCourse().getLimits();

            if (!calculator.isInside(projectile.getLocation(), boundaries)) {
                race.removeProjectile(projectile.getId());
                break;
            }
        }
    }
}
