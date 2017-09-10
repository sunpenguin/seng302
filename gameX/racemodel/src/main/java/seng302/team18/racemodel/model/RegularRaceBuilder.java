package seng302.team18.racemodel.model;

import seng302.team18.model.RaceMode;
import seng302.team18.model.RaceType;
import seng302.team18.model.updaters.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds a preset race.
 * <p>
 * Concrete implementation of AbstractRaceBuilder.
 *
 * @see seng302.team18.racemodel.model.AbstractRaceBuilder
 */
public class RegularRaceBuilder extends AbstractRaceBuilder {
    RegularStatusUpdater regularStatusUpdater = null;

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
        updaters.add(new MovementUpdater());
        updaters.add(new BoatCollisionUpdater());
        updaters.add(new MarkCollisionUpdater());
        updaters.add(new OutOfBoundsUpdater());
        updaters.add(new MarkRoundingUpdater());

        if (regularStatusUpdater == null) {
            regularStatusUpdater = new RegularStatusUpdater(ZonedDateTime.now(), 8, 2, 5);
        }

        updaters.add(regularStatusUpdater);

        return updaters;
    }


    @Override
    protected RaceMode getRaceMode() {
        return RaceMode.RACE;
    }
}
