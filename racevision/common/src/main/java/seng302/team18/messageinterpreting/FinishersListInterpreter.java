package seng302.team18.messageinterpreting;

import seng302.team18.messageparsing.AC35RaceStatusMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.List;
import java.util.Map;

/**
 * Created by dhl25 on 27/04/17.
 */
public class FinishersListInterpreter extends MessageInterpreter {

    private Race race;

    public FinishersListInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            List<Boat> finishedList = race.getFinishedList();
            Map<Integer, List> boatStatus = statusMessage.getBoatStatus();
            for (Boat boat : race.getStartingList()) {
                if (!finishedList.contains(boat) &&
                            (int) boatStatus.get(boat.getId()).get(statusMessage.getBoatStatusPosition()) == 3) {
                    finishedList.add(boat);
                }
            }
        }
    }
}
