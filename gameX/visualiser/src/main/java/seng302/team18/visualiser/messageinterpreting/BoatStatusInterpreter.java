package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35BoatStatusMessage;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.BoatStatus;
import seng302.team18.model.Race;

import java.util.List;

/**
 * Created by jth102 on 4/05/17.
 */
public class BoatStatusInterpreter extends MessageInterpreter {
    private Race race;

    public BoatStatusInterpreter(Race race) {
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
                            boat.setStatus(BoatStatus.from(boatStatus.getBoatStatus()));
                            setLeg(boat, boatStatus.getLegNumber());
                        });
            }
        }
    }


    private void setLeg(Boat boat, int realLegNumber) {
        boat.setLegNumber(realLegNumber);
    }
}
