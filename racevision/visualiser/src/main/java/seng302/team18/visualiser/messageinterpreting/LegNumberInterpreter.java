package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.messageparsing.AC35RaceStatusMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Leg;
import seng302.team18.model.Race;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            Map<Integer, List> boats = statusMessage.getBoatStatus();
            List<Boat> raceBoats = race.getStartingList();

            for (Map.Entry<Integer, List> entry : boats.entrySet()) {
                int realLegNumber = (int) entry.getValue().get(statusMessage.getLegPosition());

                List<Boat> b = raceBoats.stream().filter(boat -> boat.getId().equals(entry.getKey())).collect(Collectors.toList());
                Boat boat = b.get(0);
                Leg currentLeg = race.getCourse().getLegFromLefNumber(boat.getBoatLegNumber());

                if (currentLeg == null) { // If: no leg has been set yet
                    if (realLegNumber != 0) { // If: not in pre-start
                        boat.setBoatLegNumber((race.getCourse().getLegs().get(realLegNumber - 1)).getLegNumber());
                    }
                } else {
                    Leg nextLeg = race.getCourse().getNextLeg(currentLeg);

                    if (currentLeg.getLegNumber() != realLegNumber) {
                        race.setNextLeg(boat, nextLeg);
                    }
                }

            }
        }
    }
}
