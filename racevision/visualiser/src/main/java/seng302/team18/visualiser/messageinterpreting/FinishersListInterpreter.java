package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.message.AC35BoatStatusMessage;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.List;
import java.util.Map;

/**
 * The FinishersListInterpreter that adds a boat to the finishers list when it has finished the race.
 *
 * @see MessageInterpreter
 */
public class FinishersListInterpreter extends MessageInterpreter {
    private Race race;

    /**
     * Constructor for FinishersListInterpreter.
     *
     * @param race the race to be updated.
     */
    public FinishersListInterpreter(Race race) {
        this.race = race;
    }

    /**
     * Interpret method for FinishersListInterpreter. Gets the boat status from the message.
     *
     * @param message to be interpreted. Of type AC35RaceStatusMessage.
     * @see AC35RaceStatusMessage
     */
    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;
            List<AC35BoatStatusMessage> boatStates = statusMessage.getBoatStatus();
            List<Boat> finishedList = race.getFinishedList();
            for (AC35BoatStatusMessage boatStatus : boatStates) {
                race.getStartingList()
                        .stream()
                        .filter(boat -> boat.getId().equals(boatStatus.getBoatId()) && boatStatus.getBoatStatus() == 3)
                        .forEach(finishedList::add);
            }
//            Map<Integer, List> boatStatus = statusMessage.getBoatStatus();
//            for (Boat boat : race.getStartingList()) {
//                if (!finishedList.contains(boat) &&
//                            (int) boatStatus.get(boat.getId()).get(statusMessage.getBoatStatusPosition()) == 3) {
//                    finishedList.add(boat);
//                }
//            }
        }
    }
}
