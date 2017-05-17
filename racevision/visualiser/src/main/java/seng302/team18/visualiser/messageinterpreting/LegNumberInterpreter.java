package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.message.AC35BoatStatusMessage;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Leg;
import seng302.team18.model.Race;

import java.util.List;

/**
 * Created by jth102 on 4/05/17.
 */
public class LegNumberInterpreter extends MessageInterpreter {
    private Race race;

    public LegNumberInterpreter(Race race) {
        this.race = race;
    }

    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;

            List<AC35BoatStatusMessage> boatStates = statusMessage.getBoatStatus();
            for (AC35BoatStatusMessage boatStatus : boatStates) {
                int realLegNumber = boatStatus.getLegNumber();
                race.getStartingList()
                        .stream()
                        .filter(boat -> boat.getId().equals(boatStatus.getBoatId()))
                        .forEach(boat -> {
                            boat.setStatus(boatStatus.getBoatStatus());
                            setLeg(boat, realLegNumber);
                        });
            }

//            Map<Integer, List> boats = statusMessage.getBoatStatus();
//            List<Boat> raceBoats = race.getStartingList();
//
//            for (Map.Entry<Integer, List> entry : boats.entrySet()) {
//                int realLegNumber = (int) entry.getValue().get(statusMessage.getLegPosition());
//
//                List<Boat> b = raceBoats.stream().filter(boat -> boat.getId().equals(entry.getKey())).collect(Collectors.toList());
//                Boat boat = b.get(0);
//                Leg currentLeg = boat.getLeg();
//
//                if (currentLeg == null) { // If: no leg has been set yet
//                    if (realLegNumber != 0) { // If: not in pre-start
//                        boat.setLeg(race.getCourse().getLegs().get(realLegNumber - 1));
//                    }
//                } else {
//                    Leg nextLeg = race.getCourse().getNextLeg(currentLeg);
//
//                    if (currentLeg.getLegNumber() != realLegNumber) {
//                        race.setNextLeg(boat, nextLeg);
//                    }
//                }
//            }
        }
    }


    private void setLeg(Boat boat, int realLegNumber) {
        Leg currentLeg = race.getCourse().getLeg(boat.getLegNumber());
        if (currentLeg == null) { // If: no leg has been set yet
            if (realLegNumber != 0) { // If: not in pre-start
                boat.setLegNumber((race.getCourse().getLegs().get(realLegNumber - 1)).getLegNumber());
            }
        } else {
            Leg nextLeg = race.getCourse().getNextLeg(currentLeg);
//            race.setNextLeg(boat, nextLeg);
            if (realLegNumber == race.getCourse().getLegs().size()) {
                boat.setLegNumber(realLegNumber);
            } else if (currentLeg.getLegNumber() != realLegNumber) {
                race.setNextLeg(boat, nextLeg);
            }
        }
    }
}
