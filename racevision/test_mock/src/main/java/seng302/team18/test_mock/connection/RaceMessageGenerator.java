package seng302.team18.test_mock.connection;

import seng302.team18.model.Race;

import java.time.ZonedDateTime;

/**
 * Created by hqi19 on 21/04/17.
 */
public class RaceMessageGenerator {

    private Race race;

    public RaceMessageGenerator(Race race) {
        this.race = race;
    }

    public String getRaceMessage() {
        ZonedDateTime startTime = race.getStartTime();
        ZonedDateTime currentTime = race.getCurrentTime();
        return "";
    }
}
