package seng302.team18.racemodel.builder.race;

import seng302.team18.model.*;
import seng302.team18.model.updaters.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds a preset race for arcade mode game play
 * <p>
 * Concrete implementation of AbstractRaceBuilder.
 *
 * @see AbstractRaceBuilder
 */
public class ArcadeRaceBuilder extends AbstractRaceBuilder {
    private StatusUpdater statusUpdater  = null;


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
        updaters.add(new PowerUpUpdater(makePickUp(), 4));
        updaters.add(new ProjectileUpdater());
        updaters.add(new ProjectileHitUpdater());
        updaters.add(new ProjectileOutOfBoundsUpdater());


        if (statusUpdater == null) {
            statusUpdater = new RegularStatusUpdater(ZonedDateTime.now(), 5, 1, 5);
        }

        updaters.add(statusUpdater);

        return updaters;
    }


    private PickUp makePickUp() {
        BodyMass mass = new BodyMass();
        mass.setWeight(0);
        mass.setRadius(12);

        PickUp pickUp = new PickUp(-1);
        pickUp.setBodyMass(mass);
        return pickUp;
    }


    @Override
    protected RaceMode getRaceMode() {
        return RaceMode.ARCADE;
    }


    @Override
    protected StartPositionSetter getPositionSetter() {
        return new StartLineSetter(20);
    }
}