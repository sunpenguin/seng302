package seng302.team18.test_mock.model;

import seng302.team18.model.Race;

/**
 * Created by afj19 on 10/07/17.
 */
public abstract class RaceBuilder {

    public Race buildRace() {
        Race race = new Race();

        race.setRaceType(getRaceType());
        race.setId(getId());
        race.setRaceName(getRaceName());

        return race;
    }

    protected abstract String getRaceName();

    protected abstract int getId();

    protected abstract Race.RaceType getRaceType();
}
