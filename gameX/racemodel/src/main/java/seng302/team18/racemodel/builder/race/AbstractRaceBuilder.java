package seng302.team18.racemodel.builder.race;

import seng302.team18.model.*;
import seng302.team18.model.updaters.Updater;

import java.util.List;

/**
 * Base implementation class for race builders
 * <p>
 * This class provides a skeleton for classes that builder a race.
 */
public abstract class AbstractRaceBuilder {

    /**
     * Builds a race, setting the regatta and course
     *
     * @param regatta the regatta the race belongs to
     * @param course  the course the race follows
     * @return the constructed race
     */
    public Race buildRace(Race race, Regatta regatta, Course course) {
        race.setCourse(course);
        race.setRegatta(regatta);
        race.setRaceType(getRaceType());
        race.setId(getId());
        race.setMode(getRaceMode());
        race.setUpdaters(getUpdaters());
        race.setPositionSetter(getPositionSetter());
        return race;
    }


    /**
     * @return the ID to be used by the race
     */
    protected abstract int getId();


    /**
     * @return the type of the race
     */
    protected abstract RaceType getRaceType();

    /**
     * @return the updaters for the race
     */
    protected abstract List<Updater> getUpdaters();


    /**
     * @return the mode for the race
     */
    protected abstract RaceMode getRaceMode();


    /**
     * @return the position setter.
     */
    protected abstract StartPositionSetter getPositionSetter();

}
