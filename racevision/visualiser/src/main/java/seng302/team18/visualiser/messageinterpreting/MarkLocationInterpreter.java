package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.messageparsing.AC35BoatLocationMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Mark;
import seng302.team18.model.Race;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dhl25 on 27/04/17.
 */
public class MarkLocationInterpreter extends MessageInterpreter {

    private Race race;

    public MarkLocationInterpreter(Race race) {
        this.race = race;
    }

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
