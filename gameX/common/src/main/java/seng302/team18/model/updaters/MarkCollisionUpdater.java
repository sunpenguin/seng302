package seng302.team18.model.updaters;

import seng302.team18.model.AbstractBoat;
import seng302.team18.model.Race;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sbe67 on 5/09/17.
 */
public class MarkCollisionUpdater extends CollisionUpdater {


    @Override
    protected List<AbstractBoat> getObstacles(AbstractBoat boat, Race race) {
        return race.getCourse().getMarks()
                .stream()
                .map(mark -> ((AbstractBoat) mark))
                .collect(Collectors.toList());
    }
}
