package seng302.team18.racemodel.model;

import seng302.team18.model.Boat;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a preset participant pool with 20 participants.
 * <p>
 * Concrete implementation of AbstractParticipantsBuilder.
 *
 * @see seng302.team18.racemodel.model.AbstractParticipantsBuilder
 */
public class ParticipantsBuilderSize20 extends AbstractParticipantsBuilder {

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

        Boat boat6 = new Boat("Shining Day", "TEAM Baldr", 127, 14.019);
        boat6.setHullNumber("AC4507");
        boat6.setStoweName("BAL");
        boats.add(boat6);

        Boat boat7 = new Boat("Frenzy of the Wise", "TEAM Odin", 128, 14.019);
        boat7.setHullNumber("AC4508");
        boat7.setStoweName("ODN");
        boats.add(boat7);

        Boat boat8 = new Boat("Trickster", "TEAM Loki", 129, 14.019);
        boat8.setHullNumber("AC4509");
        boat8.setStoweName("LKI");
        boats.add(boat8);

        Boat boat9 = new Boat("Lightning", "TEAM Zeus", 130, 14.019);
        boat9.setHullNumber("AC4510");
        boat9.setStoweName("ZEU");
        boats.add(boat9);

        Boat boat10 = new Boat("Peacock", "TEAM Juno", 131, 14.019);
        boat10.setHullNumber("AC4511");
        boat10.setStoweName("JUN");
        boats.add(boat10);

        Boat boat11 = new Boat("Sea Chariot", "TEAM Poseidon", 132, 14.019);
        boat11.setHullNumber("AC4512");
        boat11.setStoweName("PSD");
        boats.add(boat11);

        Boat boat12 = new Boat("Harvest", "TEAM Ceres", 133, 14.019);
        boat12.setHullNumber("AC4513");
        boat12.setStoweName("CRS");
        boats.add(boat12);

        Boat boat13 = new Boat("Daybreak", "TEAM Apollo", 134, 14.019);
        boat13.setHullNumber("AC4514");
        boat13.setStoweName("APO");
        boats.add(boat13);

        Boat boat14 = new Boat("Moon Bow", "TEAM Tsukuyomi", 135, 14.019);
        boat14.setHullNumber("AC4515");
        boat14.setStoweName("ART");
        boats.add(boat14);

        Boat boat15 = new Boat("Sky Father", "TEAM Ranginui", 136, 14.019);
        boat15.setHullNumber("AC4516");
        boat15.setStoweName("SKY");
        boats.add(boat15);

        Boat boat16 = new Boat("Fish Father", "TEAM Ikatere", 137, 14.019);
        boat16.setHullNumber("AC4517");
        boat16.setStoweName("IKA");
        boats.add(boat16);

        Boat boat17 = new Boat("Chaos", "TEAM Set", 138, 14.019);
        boat17.setHullNumber("AC4518");
        boat17.setStoweName("CHS");
        boats.add(boat17);

        Boat boat18 = new Boat("Yeah Bouy", "TEAM Naughti Bouys", 139, 14.019);
        boat18.setHullNumber("AC4519");
        boat18.setStoweName("NTB");
        boats.add(boat18);

        Boat boat19 = new Boat("Patron of Duc", "TEAM Inanna", 140, 14.019);
        boat19.setHullNumber("AC4520");
        boat19.setStoweName("INN");
        boats.add(boat19);

        return boats;
    }
}
