package seng302.team18.test_mock.model;

import seng302.team18.model.Race;

/**
 * Created by Anton J on 11/07/2017.
 */
public class RaceBuilder1 extends AbstractRaceBuilder {
    @Override
    protected String getRaceName() {
        return "North Head";
    }

    @Override
    protected int getId() {
        return 11080703;
    }

    @Override
    protected Race.RaceType getRaceType() {
        return Race.RaceType.MATCH;
    }
}
