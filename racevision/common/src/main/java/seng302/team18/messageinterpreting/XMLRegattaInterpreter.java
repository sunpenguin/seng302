package seng302.team18.messageinterpreting;

import seng302.team18.messageparsing.AC35XMLRegattaMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.model.Race;

import java.time.ZoneId;

/**
 * Created by dhl25 on 27/04/17.
 */
public class XMLRegattaInterpreter extends MessageInterpreter {

    private Race race;

    public XMLRegattaInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35XMLRegattaMessage) {
            AC35XMLRegattaMessage regattaMessage = (AC35XMLRegattaMessage) message;
            String utcOffset = regattaMessage.getUtcOffset();
            Course course = race.getCourse();
            if (utcOffset.startsWith("+") || utcOffset.startsWith("-")) {
                course.setTimeZone(ZoneId.of("UTC" + utcOffset));
            } else {
                course.setTimeZone(ZoneId.of("UTC+" + utcOffset));
            }
            course.setCentralCoordinate(new Coordinate(regattaMessage.getCentralLat(), regattaMessage.getCentralLong()));
        }
    }
}
