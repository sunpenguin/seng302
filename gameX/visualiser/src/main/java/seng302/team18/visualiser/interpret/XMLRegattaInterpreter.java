package seng302.team18.visualiser.interpret;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.visualiser.ClientRace;

/**
 * A MessageInterpreter that takes a AC35XMLRegattaMessage and updates the central coordinate and timezone of a Race.
 */
public class XMLRegattaInterpreter extends MessageInterpreter {

    private ClientRace race;


    /**
     * Constructor for XMLRegattaInterpreter. Takes a Race as a parameter which it updates every time a
     * AC35XMLRegattaMessage is interpreted.
     *
     * @param race to be updated.
     */
    public XMLRegattaInterpreter(ClientRace race) {
        this.race = race;
    }


    /**
     * Interprets the XMLRegatta message
     *
     * @param message to be interpreted
     */
    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35XMLRegattaMessage) {
            AC35XMLRegattaMessage regattaMessage = (AC35XMLRegattaMessage) message;
            race.getRegatta().setRegattaName(regattaMessage.getRegattaName());
            Course course = race.getCourse();
            course.setCentralCoordinate(new Coordinate(regattaMessage.getCentralLat(), regattaMessage.getCentralLong()));
        }
    }
}
