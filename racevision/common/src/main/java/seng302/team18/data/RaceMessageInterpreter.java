package seng302.team18.data;

import seng302.team18.model.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static seng302.team18.data.AC35MessageType.*;

/**
 * Created by david on 4/13/17.
 */
public class RaceMessageInterpreter implements MessageInterpreter {
    
    private Race race;

    public RaceMessageInterpreter(Race race) {
        this.race = race;
    }


    public void interpretMessage(MessageBody message) {
        if (message == null) {
            return;
        }
        AC35MessageType messageType = AC35MessageType.from(message.getType());
        switch (messageType) {
            case XML_RACE:
                updateXMLRace((AC35XMLRaceMessage) message, race);
                break;
            case XML_BOATS:
                updateXMLBoats((AC35XMLBoatMessage) message, race);
                break;
            case XML_REGATTA:
                updateXMLRegatta((AC35XMLRegattaMessage) message, race.getCourse());
                break;
            case RACE_STATUS:
                updateRaceTime((AC35RaceStatusMessage) message, race);
                updateEstimatedTime((AC35RaceStatusMessage) message, race.getStartingList());
                break;
            case BOAT_LOCATION:
                updateBoatLocation((AC35BoatLocationMessage) message, race.getStartingList());
                updateMarkLocation((AC35BoatLocationMessage) message, race.getCourse().getMarks());
                break;
            case YACHT_EVENT:
                break;
        }
    }


    private void updateXMLBoats(AC35XMLBoatMessage message, Race race) {
        race.setStartingList(message.getBoats());
    }

    private void updateEstimatedTime(AC35RaceStatusMessage message, List<Boat> boats) {
//        System.out.println(message.getBoatID());
//        System.out.println(message.getEstimatedTime());
        Map<Integer, Long> boatStatus = message.getBoatStatus();
        for (Boat boat : boats) {
            if (boatStatus.containsKey(boat.getId())) {
                boat.setEstimatedTimeNextMark(boatStatus.get(boat.getId()));
            }
        }
//        if (boats.size() > 0) {
//            Iterator<Boat> boatIterator = boats.iterator();
//            Boat boat = boatIterator.next();
//            while (!boatStatus.containsKey(boat.getId()) && boatIterator.hasNext()) {
//                boat = boatIterator.next();
//            }
//
////            if (boat.getId().equals(message.getBoatID())) {
////                boat.setEstimatedTimeNextMark(message.getEstimatedTime());
////            }
//            if (boat.getId() == 102) {
//                System.out.println("Boat ID 102: " + boat.getEstimatedTimeNextMark());
//            }
//        }
    }

    private void updateBoatLocation(AC35BoatLocationMessage message, List<Boat> boats) {
        if (boats.size() > 0) {
            Iterator<Boat> boatIterator = boats.iterator();
            Boat boat = boatIterator.next();
            while (!boat.getId().equals(message.getSourceId()) && boatIterator.hasNext()) {
                boat = boatIterator.next();
            }
            if (boat.getId().equals(message.getSourceId())) {
                boat.setSpeed(message.getSpeed());
                boat.setHeading(message.getHeading());
                boat.setCoordinate(message.getCoordinate());
            }
            if (boat.getId() == 102) {
//                System.out.println("Boat ID 102: " + boat.getHeading());
            }
        }
    }


    private void updateMarkLocation(AC35BoatLocationMessage message, List<Mark> marks) {
        if (marks.size() > 0) {
            Iterator<Mark> marksIterator = marks.iterator();
            Mark mark = marksIterator.next();
            while (!mark.getId().equals(message.getSourceId()) && marksIterator.hasNext()) {
                mark = marksIterator.next();
            }
            if (mark.getId().equals(message.getSourceId())) {
                mark.setCoordinate(message.getCoordinate());
            }
        }
    }


    private void updateRaceTime(AC35RaceStatusMessage message, Race race) {
        Instant startIn = Instant.ofEpochMilli(message.getStartTime());
        Instant currentIn = Instant.ofEpochMilli(message.getCurrentTime());
        ZonedDateTime startTime = ZonedDateTime.ofInstant(startIn, race.getCourse().getTimeZone());
        ZonedDateTime currentTime = ZonedDateTime.ofInstant(currentIn, race.getCourse().getTimeZone());

        race.setStartTime(startTime);
        race.setCurrentTime(currentTime);
    }


    private void updateXMLRace(AC35XMLRaceMessage message, Race race) {
        ZonedDateTime startTime = ZonedDateTime.parse(message.getRaceStartTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        race.setStartTime(startTime);
        race.setParticipantIds(message.getParticipantIDs());

        Course course = race.getCourse();
        course.setMarkRoundings(message.getMarkRoundings());
        course.setCompoundMarks(message.getCompoundMarks());
        course.setBoundaries(message.getBoundaryMarks());
    }


    private void updateXMLRegatta(AC35XMLRegattaMessage message, Course course) {
        String utcOffset = message.getUtcOffset();
        if (utcOffset.startsWith("+") || utcOffset.startsWith("-")) {
            course.setTimeZone(ZoneId.of("UTC" + utcOffset));
        } else {
            course.setTimeZone(ZoneId.of("UTC+" + utcOffset));
        }
        course.setCentralCoordinate(new Coordinate(message.getCentralLat(), message.getCentralLong()));
    }


}
