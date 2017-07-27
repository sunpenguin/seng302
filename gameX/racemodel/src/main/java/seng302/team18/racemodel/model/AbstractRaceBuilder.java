package seng302.team18.racemodel.model;

import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.Regatta;

/**
 * Base implementation class for race builders
 * <p>
 * This class provides a skeleton for classes that build a race.
 */
public abstract class AbstractRaceBuilder {

    /**
     * Builds a race, setting the regatta and course
     *
     * @param regatta the regatta the race belongs to
     * @param course  the course the race follows
     * @return the constructed race
     */
    public Race buildRace(Regatta regatta, Course course) {
        Race race = new Race();

        race.setCourse(course);
        race.setRegatta(regatta);
        race.setRaceType(getRaceType());
        race.setId(getId());

        return race;
    }


    /**
     * @return the ID to be used by the race
     */
    protected abstract int getId();


    /**
     * @return the type of the race
     */
    protected abstract Race.RaceType getRaceType();
}
