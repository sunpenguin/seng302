package seng302.team18.racemodel.builder.race;

import seng302.team18.model.RaceMode;
import seng302.team18.model.RaceType;
import seng302.team18.model.RegularStartLineSetter;
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

    private StatusUpdater statusUpdater = null;

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
        updaters.add(new BoatCollisionUpdater(25));
        updaters.add(new MarkCollisionUpdater(25));
        updaters.add(new OutOfBoundsUpdater());
        updaters.add(new MarkRoundingUpdater());

        if (statusUpdater == null) {
            statusUpdater = new RegularStatusUpdater(ZonedDateTime.now(), 1, 1, 1);
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
        return new RegularStartLineSetter(20);
    }
}
