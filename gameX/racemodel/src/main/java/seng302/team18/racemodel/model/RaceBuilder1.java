package seng302.team18.racemodel.model;

import seng302.team18.model.Race;

/**
 * Builds a preset race.
 * <p>
 * Concrete implementation of AbstractRaceBuilder.
 *
 * @see seng302.team18.racemodel.model.AbstractRaceBuilder
 */
public class RaceBuilder1 extends AbstractRaceBuilder {

    @Override
    protected int getId() {
        return 11080703;
    }


    @Override
    protected Race.RaceType getRaceType() {
        return Race.RaceType.MATCH;
    }
}
