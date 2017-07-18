package seng302.team18.test_mock.model;

import seng302.team18.model.Boat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton J on 11/07/2017.
 */
public class ParticipantsBuilder1 extends BaseParticipantsBuilder {

    @Override
    public List<Boat> buildParticipantPool() {
        List<Boat> boats = new ArrayList<>();

        Boat boat0 = new Boat("Emirates Team New Zealand", "TEAM New Zealand", 121, 14.019);
        boat0.setHullNumber("AC4501");
        boat0.setStoweName("NZL");
        boats.add(boat0);

        Boat boat1 = new Boat("Oracle Team USA", "TEAM USA", 122, 14.019);
        boat1.setHullNumber("AC4502");
        boat1.setStoweName("USA");
        boats.add(boat1);

        Boat boat2 = new Boat("Artemis Racing", "TEAM SWE", 123, 14.019);
        boat2.setHullNumber("AC4503");
        boat2.setStoweName("SWE");
        boats.add(boat2);

        Boat boat3 = new Boat("Groupama Team France", "TEAM France", 124, 14.019);
        boat3.setHullNumber("AC4504");
        boat3.setStoweName("FRA");
        boats.add(boat3);

        Boat boat4 = new Boat("Land Rover BAR", "TEAM Britain", 125, 14.019);
        boat4.setHullNumber("AC4505");
        boat4.setStoweName("GBR");
        boats.add(boat4);

        Boat boat5 = new Boat("Softbank Team Japan", "TEAM Japan", 126, 14.019);
        boat5.setHullNumber("AC4506");
        boat5.setStoweName("JPN");
        boats.add(boat5);

        return boats;
    }
}
