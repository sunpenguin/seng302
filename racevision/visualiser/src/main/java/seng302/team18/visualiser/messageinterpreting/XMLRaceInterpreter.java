package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.messageparsing.AC35XMLRaceMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Course;
import seng302.team18.model.Race;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by dhl25 on 27/04/17.
 */
public class XMLRaceInterpreter extends MessageInterpreter {

    private Race race;

    public XMLRaceInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35XMLRaceMessage) {
            AC35XMLRaceMessage raceMessage = (AC35XMLRaceMessage) message;
            ZonedDateTime startTime;
            try {
                startTime = ZonedDateTime.parse(raceMessage.getRaceStartTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            } catch (Exception e) {
                startTime = ZonedDateTime.now();
            }
            race.setStartTime(startTime);
            race.setParticipantIds(raceMessage.getParticipantIDs());

            Course course = race.getCourse();
            course.setMarkRoundings(raceMessage.getMarkRoundings());
            course.setCompoundMarks(raceMessage.getCompoundMarks());
            course.setBoundaries(raceMessage.getBoundaryMarks());
        }
    }
}
