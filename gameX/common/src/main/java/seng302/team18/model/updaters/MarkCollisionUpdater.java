package seng302.team18.model.updaters;

import seng302.team18.model.AbstractBoat;
import seng302.team18.model.Race;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Used to check if boats collide with marks
 */
public class MarkCollisionUpdater extends CollisionUpdater {


    public MarkCollisionUpdater(double totalPushBack) {
        super(totalPushBack);
    }


    @Override
    protected List<AbstractBoat> getObstacles(AbstractBoat boat, Race race) {
        return race.getCourse().getMarks()
                .stream()
                .map(mark -> ((AbstractBoat) mark))
                .collect(Collectors.toList());
    }
}
