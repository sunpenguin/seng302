package seng302.team18.model.updaters;

import seng302.team18.model.AbstractBoat;
import seng302.team18.model.BoatStatus;
import seng302.team18.model.Race;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to handle collisions
 */
public class BoatCollisionUpdater extends CollisionUpdater {


    @Override
    protected List<AbstractBoat> getObstacles(AbstractBoat boat, Race race) {
        return race.getStartingList().stream()
                .filter(b -> !b.getId().equals(boat.getId()))
                .filter(b -> !b.getStatus().equals(BoatStatus.FINISHED))
                .collect(Collectors.toList());
    }
}
