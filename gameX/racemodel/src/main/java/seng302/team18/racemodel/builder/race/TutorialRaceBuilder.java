package seng302.team18.racemodel.builder.race;

import seng302.team18.model.*;
import seng302.team18.model.updaters.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Builds a preset race for tutorial mode.
 * <p>
 * Concrete implementation of AbstractRaceBuilder.
 *
 * @see AbstractRaceBuilder
 */
public class TutorialRaceBuilder extends AbstractRaceBuilder {

    @Override
    protected int getId() {
        return 11080701;
    }


    @Override
    protected RaceType getRaceType() {
        return RaceType.MATCH;
    }


    @Override
    protected List<Updater> getUpdaters(){
        List<Updater> updaters = new ArrayList<>();
        updaters.add(new BoatsUpdater());
        updaters.add(new RegularStatusUpdater(ZonedDateTime.now(), 5, 1, 2));

        return updaters;
    }


    @Override
    protected RaceMode getRaceMode(){
        return RaceMode.CONTROLS_TUTORIAL;
    }


    @Override
    protected StartPositionSetter getPositionSetter() {
        return new CircularPositionSetter(10);
    }
}
