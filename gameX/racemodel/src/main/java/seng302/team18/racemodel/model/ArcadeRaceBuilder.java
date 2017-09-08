package seng302.team18.racemodel.model;

import seng302.team18.model.*;
import seng302.team18.model.updaters.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a preset race for arcade mode game play
 * <p>
 * Concrete implementation of AbstractRaceBuilder.
 *
 * @see seng302.team18.racemodel.model.AbstractRaceBuilder
 */
public class ArcadeRaceBuilder extends AbstractRaceBuilder {

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
        updaters.add(new BoatUpdater());
        updaters.add(new BoatCollisionUpdater());
        updaters.add(new MarkCollisionUpdater());
        updaters.add(new OutOfBoundsUpdater());
        updaters.add(new MarkRoundingUpdater());
        updaters.add(new PowerUpUpdater(makePickUp(), 4));
        updaters.add(new ProjectileUpdater());

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
}