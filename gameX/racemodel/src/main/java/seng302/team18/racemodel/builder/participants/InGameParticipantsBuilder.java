package seng302.team18.racemodel.builder.participants;

import seng302.team18.model.Boat;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold participants with cooler names.
 */
public class InGameParticipantsBuilder extends AbstractParticipantsBuilder {

    @Override
    protected List<Boat> buildParticipantPool() {
        List<Boat> boats = new ArrayList<>();

        Boat boat0 = new Boat("Red Shoes Ronnie", "Ronnie", 121, 14.019);
        boat0.setHullNumber("HIG HC1");
        boat0.setStoweName("RON");
        boats.add(boat0);

        Boat boat1 = new Boat("Bixie Discuit Boy", "BixBoy", 122, 14.019);
        boat1.setHullNumber("HIGHC2");
        boat1.setStoweName("BIX");
        boats.add(boat1);

        Boat boat2 = new Boat("Johnny Hammer Sticks", "Hammer", 123, 14.019);
        boat2.setHullNumber("HIGHC3");
        boat2.setStoweName("JON");
        boats.add(boat2);

        Boat boat3 = new Boat("Jacky The Snow Plow", "JackP", 124, 14.019);
        boat3.setHullNumber("HIGHC4");
        boat3.setStoweName("JAK");
        boats.add(boat3);

        Boat boat4 = new Boat("Rocket Star Sally", "Sally", 125, 14.019);
        boat4.setHullNumber("HIGHC5");
        boat4.setStoweName("SAL");
        boats.add(boat4);

        Boat boat5 = new Boat("Dorrathy Dixon", "Dorrathy", 126, 14.019);
        boat5.setHullNumber("HIGHC6");
        boat5.setStoweName("DOR");
        boats.add(boat5);

        Boat boat6 = new Boat("Banshee Rangler Joseph", "BanRanJoe", 127, 14.019);
        boat6.setHullNumber("HIGHC7");
        boat6.setStoweName("JOE");
        boats.add(boat6);

        Boat boat7 = new Boat("Ricky Outlaw Thomas", "Rick", 128, 14.019);
        boat7.setHullNumber("HIGHC6");
        boat7.setStoweName("RIK");
        boats.add(boat7);

        return boats;
    }
}
