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
 * Created by Anton J on 10/07/2017.
 */
public class XmlMessageBuilder {
    private final IBoatsXmlDefaults boatsDefaults;
    private final IRaceXmlDefaults raceDefaults;

    public XmlMessageBuilder(IBoatsXmlDefaults boatsDefaults, IRaceXmlDefaults raceDefaults) {
        this.boatsDefaults = boatsDefaults;
        this.raceDefaults = raceDefaults;
    }

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


    public AC35XMLRaceMessage buildRaceXmlMessage(Race race) {

        AC35XMLRaceMessage message = new AC35XMLRaceMessage();

        message.setBoundaryMarks(race.getCourse().getBoundaries());

        message.setCompoundMarks(race.getCourse().getCompoundMarks());

        message.setStartTime(race.getStartTime().format(XmlMessage.DATE_TIME_FORMATTER));

        message.setStartPostponed(race.getStatus().equals(RaceStatus.POSTPONED));

        message.setRaceType(race.getRaceType());

        message.setRaceID(race.getId());

        Map<Integer, AC35XMLRaceMessage.EntryDirection> participants = new HashMap<>();
        for (Boat boat : race.getStartingList()) {
            participants.put(boat.getId(), raceDefaults.getParticipantEntryDirection());
        }
        message.setParticipants(participants);

        message.setMarkRoundings(race.getCourse().getMarkRoundings());

        return message;
    }


    public AC35XMLRegattaMessage buildRegattaMessage(Race race) {
        Regatta regatta = race.getRegatta();
        Coordinate centre = race.getCourse().getCentralCoordinate();
        ZoneOffset utcOffset = LocalDateTime.now().atZone(race.getCourse().getTimeZone()).getOffset();

        AC35XMLRegattaMessage message = new AC35XMLRegattaMessage(
                regatta.getRegattaID(),
                regatta.getRegattaName(),
                race.getCourse().getName(),
                centre.getLatitude(),
                centre.getLongitude(),
                utcOffset.toString()
        );

        return message;
    }
}
