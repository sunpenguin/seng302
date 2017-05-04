package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.messageparsing.AC35BoatLocationMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Mark;
import seng302.team18.model.Race;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The MarkLocationInterpreter that sets the mark location.
 *
 * @see MessageInterpreter
 */
public class MarkLocationInterpreter extends MessageInterpreter {
    private Race race;

    /**
     * Constructor for MarkLocationInterpreter.
     *
     * @param race the race to be updated.
     */
    public MarkLocationInterpreter(Race race) {
        this.race = race;
    }

    /**
     * Interpret method for MarkLocationInterpreter. Gets the mark coordinates from the message.
     *
     * @param message to be interpreted. Of type AC35BoatLocationMessage.
     * @see AC35BoatLocationMessage
     */
    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35BoatLocationMessage) {
            AC35BoatLocationMessage locationMessage = (AC35BoatLocationMessage) message;
            List<Mark> filteredMarks = race.getCourse().getMarks().stream()
                    .filter(mark -> mark.getId().equals(locationMessage.getSourceId()))
                    .collect(Collectors.toList());
            for (Mark mark : filteredMarks) {
                mark.setCoordinate(locationMessage.getCoordinate());
            }
        }
    }
}
