package seng302.team18.test_mock.model;

import org.junit.Test;
import seng302.team18.model.Boat;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class AbstractParticipantsBuilderTest {
    private static final List<Boat> participants = setUpParticipants();


    @Test
    public void getParticipantPool() throws Exception {
        AbstractParticipantsBuilder participantsBuilder = new ConcreteParticipantsBuilder();

        for (int i = 0; i <= 8; i++) {
            assertEquals(new Integer(110 + i), participantsBuilder.getParticipantPool().get(i).getId());
        }
    }


    @Test
    public void getIdPool() throws Exception {
        AbstractParticipantsBuilder participantsBuilder = new ConcreteParticipantsBuilder();

        for (int i = 0; i <= 8; i++) {
            assertEquals(new Integer(110 + i), participantsBuilder.getIdPool().get(i));
        }
    }


    private static List<Boat> setUpParticipants() {
        List<Boat> boats = new ArrayList<>();

        boats.add(new Boat("Boat0", "b0", 110));
        boats.add(new Boat("Boat1", "b1", 111));
        boats.add(new Boat("Boat2", "b2", 112));
        boats.add(new Boat("Boat3", "b3", 113));
        boats.add(new Boat("Boat4", "b4", 114));
        boats.add(new Boat("Boat5", "b5", 115));
        boats.add(new Boat("Boat6", "b6", 116));
        boats.add(new Boat("Boat7", "b7", 117));
        boats.add(new Boat("Boat8", "b8", 118));

        return boats;
    }


    private class ConcreteParticipantsBuilder extends AbstractParticipantsBuilder {
        @Override
        protected List<Boat> buildParticipantPool() {
            return participants;
        }
    }
}