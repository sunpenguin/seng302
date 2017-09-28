package seng302.team18.model.updaters;

import seng302.team18.model.AbstractBoat;
import seng302.team18.model.Race;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to handle collisions
 */
public class BoatCollisionUpdater extends CollisionUpdater {


    public BoatCollisionUpdater(double totalPushBack) {
        super(totalPushBack);
    }


    @Override
    protected List<AbstractBoat> getObstacles(AbstractBoat boat, Race race) {
        return race.getCompetitors().stream()
                .filter(b -> !b.getId().equals(boat.getId()))
                .collect(Collectors.toList());
    }
}
