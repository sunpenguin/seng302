package seng302.team18.test_mock.model;

import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.message.XmlMessage;
import seng302.team18.model.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds Xml message classes on demand, when supplied with model classes.
 * Uses a set of defaults supplied at construction to populate fields not available
 * from the model classes.
 *
 * @see seng302.team18.message.AC35XMLBoatMessage
 * @see seng302.team18.message.AC35XMLRegattaMessage
 * @see seng302.team18.message.AC35XMLRaceMessage
 */
public class XmlMessageBuilder {

    /**
     * The defaults for AC35XMLBoatMessage
     */
    private final IBoatsXmlDefaults boatsDefaults;
    /**
     * The defaults for AC35XMLRaceMessage
     */
    private final IRaceXmlDefaults raceDefaults;


    /**
     * Constructs a XmlMessage builder, and sets the default values to use
     *
     * @param boatsDefaults the supplier for the defaults for Boats.xml
     * @param raceDefaults  the supplier for defaults for Race.xml
     */
    public XmlMessageBuilder(IBoatsXmlDefaults boatsDefaults, IRaceXmlDefaults raceDefaults) {
        this.boatsDefaults = boatsDefaults;
        this.raceDefaults = raceDefaults;
    }


    /**
     * Uses the given race to build a AC35XMLBoatMessage
     *
     * @param race the race to build a AC35XMLBoatMessage for
     * @return the generated message class
     */
    public AC35XMLBoatMessage buildBoatsXmlMessage(Race race) {

        List<AbstractBoat> boats = new ArrayList<>(race.getStartingList());
        boats.addAll(race.getCourse().getMarks());

        AC35XMLBoatMessage message = new AC35XMLBoatMessage(boats);

        message.setVersion(boatsDefaults.getVersion());
        message.setRaceBoatType(boatsDefaults.getRaceBoatType());
        message.setBoatLength(boatsDefaults.getBoatLength());
        message.setHullLength(boatsDefaults.getHullLength());
        message.setMarkZoneSize(boatsDefaults.getMarkZoneSize());
        message.setCourseZoneSize(boatsDefaults.getCourseZoneSize());

        message.setLimit1(boatsDefaults.getLimit1());
        message.setLimit2(boatsDefaults.getLimit2());
        message.setLimit3(boatsDefaults.getLimit3());
        message.setLimit4(boatsDefaults.getLimit4());
        message.setLimit5(boatsDefaults.getLimit5());

        message.setDefaultGpsX(boatsDefaults.getGpsX());
        message.setDefaultGpsY(boatsDefaults.getGpsY());
        message.setDefaultGpsZ(boatsDefaults.getGpsZ());


        message.setDefaultFlagX(boatsDefaults.getFlagX());
        message.setDefaultFlagY(boatsDefaults.getFlagY());
        message.setDefaultFlagZ(boatsDefaults.getFlagZ());

        return message;
    }


    /**
     * Uses the given race to build a  AC35XMLRaceMessage
     *
     * @param race the race to build a AC35XMLRaceMessage for
     * @return the generated message class
     */
    public AC35XMLRaceMessage buildRaceXmlMessage(Race race) {
        AC35XMLRaceMessage message = new AC35XMLRaceMessage();

        Map<Integer, AC35XMLRaceMessage.EntryDirection> participants = new HashMap<>();
        for (Boat boat : race.getStartingList()) {
            participants.put(boat.getId(), raceDefaults.getParticipantEntryDirection());
        }

        message.setBoundaryMarks(race.getCourse().getBoundaries());
        message.setCompoundMarks(race.getCourse().getCompoundMarks());
        message.setStartTime(race.getStartTime().format(XmlMessage.DATE_TIME_FORMATTER));
        message.setStartPostponed(race.getStatus().equals(RaceStatus.POSTPONED));
        message.setRaceType(race.getRaceType());
        message.setRaceID(race.getId());
        message.setParticipants(participants);
        message.setMarkRoundings(race.getCourse().getMarkRoundings());

        return message;
    }

    /**
     * Uses the given race to build a  AC35XMLRegattaMessage
     *
     * @param race the race to build a AC35XMLRegattaMessage for
     * @return the generated message class
     */
    public AC35XMLRegattaMessage buildRegattaMessage(Race race) {
        Regatta regatta = race.getRegatta();
        Coordinate centre = race.getCourse().getCentralCoordinate();
        ZoneOffset utcOffset = LocalDateTime.now().atZone(race.getCourse().getTimeZone()).getOffset();

        return new AC35XMLRegattaMessage(
                regatta.getRegattaID(),
                regatta.getRegattaName(),
                race.getCourse().getName(),
                centre.getLatitude(),
                centre.getLongitude(),
                utcOffset.toString()
        );
    }

}
