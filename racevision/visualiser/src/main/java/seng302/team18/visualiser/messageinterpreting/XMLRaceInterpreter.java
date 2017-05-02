package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.messageparsing.AC35XMLRaceMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Course;
import seng302.team18.model.Race;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A MessageInterpreter that takes a AC35XMLRaceMessage and updates the start time, participants, and course of a Race.
 */
public class XMLRaceInterpreter extends MessageInterpreter {

    private Race race;

    /**
     * Constructor for XMLRaceInterpreter. Takes a Race as a parameter which it updates every time a
     * AC35XMLBoatMessage is interpreted.
     * @param race to be updated.
     */
    public XMLRaceInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35XMLRaceMessage) {
            AC35XMLRaceMessage raceMessage = (AC35XMLRaceMessage) message;
            ZonedDateTime startTime = ZonedDateTime.parse(raceMessage.getRaceStartTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            race.setStartTime(startTime);
            race.setParticipantIds(raceMessage.getParticipantIDs());

            Course course = race.getCourse();
            //course.setMarkRoundings(raceMessage.getMarkRoundings());
            course.setCompoundMarks(raceMessage.getCompoundMarks());
            course.setBoundaries(raceMessage.getBoundaryMarks());
        }
    }
}
