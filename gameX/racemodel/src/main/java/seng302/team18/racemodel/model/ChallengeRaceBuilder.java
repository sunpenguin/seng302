package seng302.team18.racemodel.model;

import seng302.team18.model.BodyMass;
import seng302.team18.model.PickUp;
import seng302.team18.model.RaceMode;
import seng302.team18.model.RaceType;
import seng302.team18.model.updaters.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhl25 on 7/09/17.
 */
public class ChallengeRaceBuilder extends AbstractRaceBuilder {

    @Override
    protected int getId() {
        return 11082705;
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
        updaters.add(new SpeedUpdater(10 * 1000, 1));
        return updaters;
    }


    @Override
    protected RaceMode getRaceMode() {
            return RaceMode.CHALLENGE_MODE;
        }


}
