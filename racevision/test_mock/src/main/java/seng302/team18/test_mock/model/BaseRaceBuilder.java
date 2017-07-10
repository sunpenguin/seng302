package seng302.team18.test_mock.model;

import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.Regatta;

/**
 * Created by afj19 on 10/07/17.
 */
public abstract class BaseRaceBuilder {

    public Race buildRace(Regatta regatta, Course course) {
        Race race = new Race();

        race.setRaceType(getRaceType());
        race.setId(getId());
        race.setRaceName(getRaceName());
        race.setCourse(course);
        race.setRegatta(regatta);

        return race;
    }

    protected abstract String getRaceName();

    protected abstract int getId();

    protected abstract Race.RaceType getRaceType();
}
