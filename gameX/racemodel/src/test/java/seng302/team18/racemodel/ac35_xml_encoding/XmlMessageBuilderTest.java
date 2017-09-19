package seng302.team18.racemodel.ac35_xml_encoding;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.message.XmlMessage;
import seng302.team18.model.*;
import seng302.team18.racemodel.model.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class XmlMessageBuilderTest {

    private static final List<Boat> BOATS = Arrays.asList(
            new Boat("Boat 1", "b1", 1, 14.019),
            new Boat("Boat 2", "b2", 2, 14.019),
            new Boat("Boat 3", "b3", 3, 14.019)
    );

    private IBoatsXmlDefaults boatsXmlDefaults = new BoatXmlDefaults();
    private IRaceXmlDefaults raceXmlDefaults = new RaceXmlDefaults();

    private AC35XMLBoatMessage boatMessage;
    private AC35XMLRegattaMessage regattaMessage;
    private AC35XMLRaceMessage raceMessage;

    private Race race;


    @Before
    public void setUp() throws Exception {
        AbstractCourseBuilder courseBuilder = new CourseBuilder1();
        AbstractRaceBuilder raceBuilder = new RegularRaceBuilder();
        AbstractRegattaBuilder regattaBuilder = new RegattaBuilder1();
        race = raceBuilder.buildRace(new Race(), regattaBuilder.buildRegatta(), courseBuilder.buildCourse());
        BOATS.forEach(boat -> race.addParticipant(boat));

        XmlMessageBuilder messageBuilder = new XmlMessageBuilder(boatsXmlDefaults, raceXmlDefaults);
        boatMessage = messageBuilder.buildBoatsXmlMessage(race);
        regattaMessage = messageBuilder.buildRegattaMessage(race);
        raceMessage = messageBuilder.buildRaceXmlMessage(race);
    }


    @Test
    public void buildBoatsXmlMessage_boats() throws Exception {
        List<AbstractBoat> abstractBoats = boatMessage.getBoats();

        int numberExpectedBoats = race.getCourse().getMarks().size() + BOATS.size();
        assertEquals("wrong number of boats", numberExpectedBoats, abstractBoats.size());

        for (Boat boat : BOATS) {
            assertTrue("message does not contain boat: " + boat.getName(), abstractBoats.contains(boat));
        }

        for (Mark mark : race.getCourse().getMarks()) {
            assertTrue("message does not contain mark: " + mark.getName(), abstractBoats.contains(mark));
        }
    }


    @Test
    public void buildBoatsXmlMessage_version() throws Exception {
        assertEquals(boatsXmlDefaults.getVersion(), boatMessage.getVersion());
    }


    @Test
    public void buildBoatsXmlMessage_raceBoatType() throws Exception {
        assertEquals(boatsXmlDefaults.getRaceBoatType(), boatMessage.getRaceBoatType());
    }


    @Test
    public void buildBoatsXmlMessage_boatLength() throws Exception {
        assertEquals(boatsXmlDefaults.getBoatLength(), boatMessage.getBoatLength(), 1e-6);
    }


    @Test
    public void buildBoatsXmlMessage_hullLength() throws Exception {
        assertEquals(boatsXmlDefaults.getHullLength(), boatMessage.getHullLength(), 1e-6);
    }


    @Test
    public void buildBoatsXmlMessage_markZoneSize() throws Exception {
        assertEquals(boatsXmlDefaults.getMarkZoneSize(), boatMessage.getMarkZoneSize(), 1e-6);
    }


    @Test
    public void buildBoatsXmlMessage_courseZoneSize() throws Exception {
        assertEquals(boatsXmlDefaults.getCourseZoneSize(), boatMessage.getCourseZoneSize(), 1e-6);
    }


    @Test
    public void buildBoatsXmlMessage_limits() throws Exception {
        assertEquals("limit 1 is incorrect", boatsXmlDefaults.getLimit1(), boatMessage.getLimit1(), 1e-6);
        assertEquals("limit 2 is incorrect", boatsXmlDefaults.getLimit2(), boatMessage.getLimit2(), 1e-6);
        assertEquals("limit 3 is incorrect", boatsXmlDefaults.getLimit3(), boatMessage.getLimit3(), 1e-6);
        assertEquals("limit 4 is incorrect", boatsXmlDefaults.getLimit4(), boatMessage.getLimit4(), 1e-6);
        assertEquals("limit 5 is incorrect", boatsXmlDefaults.getLimit5(), boatMessage.getLimit5(), 1e-6);
    }


    @Test
    public void buildBoatsXmlMessage_gps() throws Exception {
        assertEquals("gps x is incorrect", boatsXmlDefaults.getGpsX(), boatMessage.getDefaultGpsX(), 1e-6);
        assertEquals("gps y is incorrect", boatsXmlDefaults.getGpsY(), boatMessage.getDefaultGpsY(), 1e-6);
        assertEquals("gps z is incorrect", boatsXmlDefaults.getGpsZ(), boatMessage.getDefaultGpsZ(), 1e-6);
    }


    @Test
    public void buildBoatsXmlMessage_flag() throws Exception {
        assertEquals("flag x is incorrect", boatsXmlDefaults.getFlagX(), boatMessage.getDefaultFlagX(), 1e-6);
        assertEquals("flag y is incorrect", boatsXmlDefaults.getFlagY(), boatMessage.getDefaultFlagY(), 1e-6);
        assertEquals("flag z is incorrect", boatsXmlDefaults.getFlagZ(), boatMessage.getDefaultFlagZ(), 1e-6);
    }


    @Test
    public void buildRaceXmlMessage_boundaries() throws Exception {
        assertEquals("wrong number of boundary marks", race.getCourse().getLimits().size(), raceMessage.getBoundaryMarks().size());

        for (int i = 0; i < race.getCourse().getLimits().size(); i++) {
            Coordinate boundaryMark = race.getCourse().getLimits().get(i);
            assertTrue("message does not contain boundary mark: " + i, raceMessage.getBoundaryMarks().contains(boundaryMark));
        }
    }


    @Test
    public void buildRaceXmlMessage_compoundMarks() throws Exception {
        assertEquals("wrong number of compound marks", race.getCourse().getCompoundMarks().size(), raceMessage.getCompoundMarks().size());

        for (CompoundMark compoundMark : race.getCourse().getCompoundMarks()) {
            assertTrue("message does not contain compound mark: " + compoundMark.getName(), raceMessage.getCompoundMarks().contains(compoundMark));
        }
    }


    @Test
    public void buildRaceXmlMessage_startTime() throws Exception {
        assertEquals(race.getStartTime().format(XmlMessage.DATE_TIME_FORMATTER), raceMessage.getStartTime());
    }


    @Test
    public void buildRaceXmlMessage_isPostponed() throws Exception {
        assertEquals(race.getStatus().equals(RaceStatus.POSTPONED), raceMessage.isStartPostponed());
    }


    @Test
    public void buildRaceXmlMessage_raceType() throws Exception {
        assertEquals(race.getRaceType(), raceMessage.getRaceType());
    }


    @Test
    public void buildRaceXmlMessage_raceId() throws Exception {
        assertEquals(race.getId(), raceMessage.getRaceID());
    }


    @Test
    public void buildRaceXmlMessage_participants() throws Exception {
        assertEquals("wrong number of participants", race.getStartingList().size(), raceMessage.getParticipants().size());

        for (Boat boat : race.getStartingList()) {
            assertTrue("message does not contain participant: " + boat.getId(), raceMessage.getParticipants().containsKey(boat.getId()));
            assertEquals("participant " + boat.getId() + " has incorrect entry direction", raceXmlDefaults.getParticipantEntryDirection(), raceMessage.getParticipants().get(boat.getId()));
        }
    }


    @Test
    public void buildRaceXmlMessage_markRounding() throws Exception {
        assertEquals("wrong number of mark roundings", race.getCourse().getMarkSequence().size(), raceMessage.getMarkRoundings().size());

        for (MarkRounding rounding : race.getCourse().getMarkSequence()) {
            assertTrue("message does not contain rounding: " + rounding.getSequenceNumber(), raceMessage.getMarkRoundings().contains(rounding));
        }
    }


    @Test
    public void buildRegattaMessage_id() throws Exception {
        assertEquals(race.getRegatta().getRegattaID(), regattaMessage.getId());
    }


    @Test
    public void buildRegattaMessage_name() throws Exception {
        assertEquals(race.getRegatta().getName(), regattaMessage.getRegattaName());
    }


    @Test
    public void buildRegattaMessage_courseName() throws Exception {
        assertEquals(race.getCourse().getName(), regattaMessage.getCourseName());
    }
}