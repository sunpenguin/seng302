package seng302.team18.racemodel.model;

import seng302.team18.model.RaceMode;
import seng302.team18.model.RaceType;
import seng302.team18.model.updaters.*;

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
        updaters.add(new CollisionUpdater());
        updaters.add(new OutOfBoundsUpdater());
        updaters.add(new MarkRoundingUpdater());

        return updaters;
    }


    @Override
    protected RaceMode getRaceMode() {
        return RaceMode.RACE;
    }
}
