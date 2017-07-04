package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Course;
import seng302.team18.model.Race;

import java.time.ZoneId;

/**
 * A MessageInterpreter that takes a AC35XMLRegattaMessage and updates the central coordinate and timezone of a Race.
 */
public class XMLRegattaInterpreter extends MessageInterpreter {

    private Race race;

    /**
     * Constructor for XMLRegattaInterpreter. Takes a Race as a parameter which it updates every time a
     * AC35XMLRegattaMessage is interpreted.
     * @param race to be updated.
     */
    public XMLRegattaInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35XMLRegattaMessage) {
            AC35XMLRegattaMessage regattaMessage = (AC35XMLRegattaMessage) message;
            race.setRaceName(regattaMessage.getName());
            String utcOffset = regattaMessage.getUtcOffset();
            Course course = race.getCourse();
            if (utcOffset.startsWith("+") || utcOffset.startsWith("-")) {
                course.setTimeZone(ZoneId.of("UTC" + utcOffset));
            } else {
                course.setTimeZone(ZoneId.of("UTC+" + utcOffset));
            }
            // course.setCentralCoordinate(new Coordinate(regattaMessage.getCentralLat(), regattaMessage.getCentralLong()));
        }
    }
}
