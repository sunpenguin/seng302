package seng302.team18.data;

import seng302.team18.model.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
//        System.out.println(messageType);
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
                updateEstimatedTime((AC35RaceStatusMessage) message, race);
                break;
            case BOAT_LOCATION:
                updateBoatLocation((AC35BoatLocationMessage) message, race.getStartingList());
                updateMarkLocation((AC35BoatLocationMessage) message, race.getCourse().getMarks());
                break;
            case YACHT_EVENT:
                break;
            case MARK_ROUNDING:
                updateMarkRounding((AC35MarkRoundingMessage) message, race.getStartingList());
                break;
        }
    }


    private void updateXMLBoats(AC35XMLBoatMessage message, Race race) {
        race.setStartingList(message.getBoats());
    }

    private void updateEstimatedTime(AC35RaceStatusMessage message, Race race) {
        Map<Integer, Long> boatStatus = message.getEstimatedMarkTimes();
        for (Boat boat : race.getStartingList()) {
            if (boatStatus.containsKey(boat.getId())) {
                double timeTilNextMark = (boatStatus.get(boat.getId()) - message.getCurrentTime()) / 1000d;
                boat.setTimeTilNextMark((long) timeTilNextMark);
            }
        }
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
        }
    }


    private void updateMarkLocation(AC35BoatLocationMessage message, List<Mark> marks) {
        List<Mark> filteredMarks = race.getCourse().getMarks().stream()
                .filter(mark -> mark.getId().equals(message.getSourceId()))
                .collect(Collectors.toList());
        for (Mark mark : filteredMarks) {
            mark.setCoordinate(message.getCoordinate());
        }
    }


    private void updateRaceTime(AC35RaceStatusMessage message, Race race) {
        Instant startIn = Instant.ofEpochMilli(message.getStartTime());
        Instant currentIn = Instant.ofEpochMilli(message.getCurrentTime());
        ZonedDateTime startTime = ZonedDateTime.ofInstant(startIn, race.getCourse().getTimeZone());
        ZonedDateTime currentTime = ZonedDateTime.ofInstant(currentIn, race.getCourse().getTimeZone());

        race.setStartTime(startTime);
        race.setCurrentTime(currentTime);

        updateBoatMarkTimes(race.getStartingList(), message.getCurrentTime());
    }

    private void updateBoatMarkTimes(Iterable<Boat> boats, long time) {
        for (Boat boat : boats) {
            if (!boat.getTimeAtLastMark().equals(0L)) {
                boat.setTimeSinceLastMark((time - boat.getTimeAtLastMark()) / 1000);
            }
        }
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

    private void updateMarkRounding(AC35MarkRoundingMessage message, Iterable<Boat> boats) {
        for (Boat boat : boats) {
            if (boat.getId().equals(message.getBoatId())) {
                boat.setTimeAtLastMark(message.getTime());
            }
        }
    }


}
