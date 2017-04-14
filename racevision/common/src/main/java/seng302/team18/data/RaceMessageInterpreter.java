package seng302.team18.data;

import seng302.team18.model.Boat;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.model.Race;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by david on 4/13/17.
 */
public class RaceMessageInterpreter {
    
    private Race race;

    public RaceMessageInterpreter(Race race) {
        this.race = race;
    }

    public void interpretMessage(MessageBody message) {
        switch (message.getType()) {
            case XML_RACE:
                xmlRace((AC35XMLRaceMessage) message, race);
                break;
            case XML_BOATS:
                break;
            case XML_REGATTA:
                xmlRegatta((AC35XMLRegattaMessage) message, race.getCourse());
                break;
            case BOAT_LOCATION:
                boatLocation((AC35BoatLocationMessage) message, race.getStartingList());
                break;
            case YACHT_EVENT:
                break;
        }
    }

    private void boatLocation(AC35BoatLocationMessage message, List<Boat> boats) {
//        System.out.println("boat location");
//        System.out.println("speed = " + message.getSpeed());
//        System.out.println("heading = " + message.getHeading());
//        System.out.println("coordinate = " + message.getCoordinate().toString());
//        System.out.println();

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


    private void xmlRace(AC35XMLRaceMessage message, Race race) {
//        System.out.println("race xml");
//        System.out.println("time = " + message.getRaceStartTime());
//        System.out.println("participant ids = " + message.getParticipantIDs());
//        System.out.println("boundaries = " + message.getBoundaryMarks());
//        System.out.println("marks = " + message.getCompoundMarks());
//        System.out.println("roundings = " + message.getMarkRoundings());
//        System.out.println();
        LocalDateTime startTime = LocalDateTime.parse(message.getRaceStartTime());
        race.setStartTime(startTime);
        race.setParticipantIds(message.getParticipantIDs());

        Course course = race.getCourse();
        course.setMarkRoundings(message.getMarkRoundings());
        course.setCompoundMarks(message.getCompoundMarks());
        course.setBoundaries(message.getBoundaryMarks());
//        if (course == null) {}
    }


    private void xmlRegatta(AC35XMLRegattaMessage message, Course course) {
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
