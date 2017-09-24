package seng302.team18.racemodel.builder.race;

import seng302.team18.model.RaceMode;
import seng302.team18.model.RaceType;
import seng302.team18.model.StartLineSetter;
import seng302.team18.model.StartPositionSetter;
import seng302.team18.model.updaters.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds a preset race.
 * <p>
 * Concrete implementation of AbstractRaceBuilder.
 *
 * @see AbstractRaceBuilder
 */
public class RegularRaceBuilder extends AbstractRaceBuilder {
    StatusUpdater statusUpdater = null;

    @Override
    protected int getId() {
        return 11080703;
    }


    @Override
    protected RaceType getRaceType() {
        return RaceType.MATCH;
    }


    @Override
    protected List<Updater> getUpdaters() {
        List<Updater> updaters = new ArrayList<>();
        updaters.add(new BoatsUpdater());
        updaters.add(new BoatCollisionUpdater());
        updaters.add(new MarkCollisionUpdater());
        updaters.add(new OutOfBoundsUpdater());
        updaters.add(new MarkRoundingUpdater());

        if (statusUpdater == null) {
            statusUpdater = new RegularStatusUpdater(ZonedDateTime.now(), 2, 1, 5);
        }

        updaters.add(statusUpdater);

        return updaters;
    }


    @Override
    protected RaceMode getRaceMode() {
        return RaceMode.RACE;
    }


    @Override
    protected StartPositionSetter getPositionSetter() {
        return new StartLineSetter(20);
    }
}
