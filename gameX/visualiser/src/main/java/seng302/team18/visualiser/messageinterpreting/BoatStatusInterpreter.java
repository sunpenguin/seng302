package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35BoatStatusMessage;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.model.BoatStatus;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.display.DisplayRace;

import java.util.List;

/**
 * Interpreter to interpret AC35RaceStatusMessages for a boat status
 */
public class BoatStatusInterpreter extends MessageInterpreter {
    private DisplayRace race;

    public BoatStatusInterpreter(DisplayRace race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;

            List<AC35BoatStatusMessage> boatStates = statusMessage.getBoatStatus();
            for (AC35BoatStatusMessage boatStatus : boatStates) {
                race.getStartingList()
                        .stream()
                        .filter(boat -> boat.getId().equals(boatStatus.getBoatId()))
                        .forEach(boat -> {
                            if ((boat.getId() == race.getPlayerId()) && (boatStatus.getBoatStatus() == BoatStatus.DSQ) && (boat.getStatus() != BoatStatus.DSQ)) {
                                setChanged();
                                notifyObservers(boat);
                            }
                            boat.setStatus(boatStatus.getBoatStatus());
                            setLeg(boat, boatStatus.getLegNumber());
                        });
            }
        }
    }


    private void setLeg(Boat boat, int realLegNumber) {
        race.setNextLeg(boat, realLegNumber);
    }
}
