package seng302.team18.data;

import seng302.team18.model.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

/**
 * Created by david on 4/13/17.
 */
public class RaceMessageInterpreter implements MessageInterpreter {
    
    private Race race;

    public RaceMessageInterpreter(Race race) {
        this.race = race;
    }

    public void interpretMessage(MessageBody message) {
        switch (message.getType()) {
            case XML_RACE:
                updateXMLRace((AC35XMLRaceMessage) message, race);
                break;
            case XML_BOATS:
                updateXMLBoats((AC35XMLBoatMessage) message, race);
                break;
            case XML_REGATTA:
                updateXMLRegatta((AC35XMLRegattaMessage) message, race.getCourse());
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
        // What do I do here? Boats contain marks etc.
        race.setStartingList(message.getBoats());
//        System.out.println("Boat XML");
//        for (Boat boat: message.getBoats()) {
//            System.out.println("BoatName: " + boat.getBoatName());
//            System.out.println("Boat Short Name: " + boat.getShortName());
//            System.out.println("Boat ID: " + boat.getId());
//            System.out.println();
//        }

    }

    private void updateBoatLocation(AC35BoatLocationMessage message, List<Boat> boats) {
//        System.out.println("boat location");
//        System.out.println("speed = " + message.getSpeed());
//        System.out.println("heading = " + message.getHeading());
//        System.out.println("coordinate = " + message.getCoordinate().toString());
//        System.out.println();
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


    private void updateXMLRace(AC35XMLRaceMessage message, Race race) {
//        System.out.println("race xml");
//        System.out.println("time = " + message.getRaceStartTime());
//        System.out.println("participant ids = " + message.getParticipantIDs());
//        System.out.println("boundaries = " + message.getBoundaryMarks());
//        System.out.println("marks = " + message.getCompoundMarks());
//        System.out.println("roundings = " + message.getMarkRoundings());
//        System.out.println();
        LocalDateTime startTime = LocalDateTime.parse(message.getRaceStartTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        race.setStartTime(startTime);
        race.setParticipantIds(message.getParticipantIDs());

        Course course = race.getCourse();
        course.setMarkRoundings(message.getMarkRoundings());
        course.setCompoundMarks(message.getCompoundMarks());
        course.setBoundaries(message.getBoundaryMarks());
    }


    private void updateXMLRegatta(AC35XMLRegattaMessage message, Course course) {
//        System.out.println("regatta xml");
//        System.out.println("UTC offset = " + message.getUtcOffset());
//        System.out.println("cent Lat = " + message.getCentralLat());
//        System.out.println("cent Long = " + message.getCentralLong());
//        System.out.println();
        String utcOffset = message.getUtcOffset();
        if (utcOffset.startsWith("+") || utcOffset.startsWith("-")) {
            course.setTimeZone(ZoneId.of("UTC" + utcOffset));
        } else {
            course.setTimeZone(ZoneId.of("UTC+" + utcOffset));
        }
        course.setCentralCoordinate(new Coordinate(message.getCentralLat(), message.getCentralLong()));
    }


}
