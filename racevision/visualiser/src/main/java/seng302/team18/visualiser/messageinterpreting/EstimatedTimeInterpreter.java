package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.messageparsing.AC35RaceStatusMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.List;
import java.util.Map;

/**
 * Created by dhl25 on 27/04/17.
 */
public class EstimatedTimeInterpreter extends MessageInterpreter {

    private Race race;

    public EstimatedTimeInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            Map<Integer, List> boatStatus = statusMessage.getBoatStatus();
            for (Boat boat : race.getStartingList()) {
                if (boatStatus.containsKey(boat.getId())) {
                    double timeTilNextMark = ((long) boatStatus.get(boat.getId())
                            .get(statusMessage.getEstimatedTimePosition()) - statusMessage.getCurrentTime()) / 1000d;
                    boat.setTimeTilNextMark((long) timeTilNextMark);
                }
            }
        }
    }
}
