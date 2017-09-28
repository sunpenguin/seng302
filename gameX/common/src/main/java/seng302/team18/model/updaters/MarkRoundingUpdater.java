package seng302.team18.model.updaters;

import seng302.team18.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to check if boats have rounded a mark
 */
public class MarkRoundingUpdater implements Updater {

    @Override
    public void update(Race race, double time) {
        List<BoatStatus> racingCodes = Arrays.asList(BoatStatus.PRE_START, BoatStatus.RACING, BoatStatus.OCS);
        List<Boat> boatsToUpdate = new ArrayList<>(race.getStartingList()
                .stream()
                .filter(boat -> racingCodes.contains(boat.getStatus()))
                .collect(Collectors.toList()));
        for (Boat boat : boatsToUpdate) {
            checkForRounding(boat, race);
        }
    }


    /**
     * Detects roundings and updates boat status.
     *
     * @param boat to update.
     */
    private void checkForRounding(Boat boat, Race race) {
        boolean hasPassed = race.getDetector().hasPassedDestination(boat, race.getCourse());

        switch (boat.getStatus()) {
            case RACING:
                if (hasPassed) {
                    setNextLeg(boat, boat.getLegNumber() + 1, race);
                }
                break;
            case PRE_START:
                if (boat.getLegNumber() == 0 && hasPassed) {
                    statusOSCPenalty(boat, race);
                }
                break;
            case OCS:
                if (race.getCurrentTime().isAfter(race.getStartTime().plusSeconds(5))) {
                    race.addYachtEvent(new YachtEvent(System.currentTimeMillis(), boat.getId(), YachtEventCode.OCS_PENALTY_COMPLETE));
                    boat.setStatus(BoatStatus.RACING);
                    boat.setSpeed(boat.getBoatTWS(race.getCourse().getWindSpeed(), race.getCourse().getWindDirection()));
                }
        }
    }


    /**
     * Sets the next Leg of the boat, updates the mark to show the boat has passed it,
     * and sets the destination to the next marks coordinates.
     *
     * @param boat    the boat
     * @param nextLeg the next leg
     */
    private void setNextLeg(Boat boat, int nextLeg, Race race) {
        int currentLeg = boat.getLegNumber();

        if (currentLeg == nextLeg) return;

        race.addMarkRoundingEvent(new MarkRoundingEvent(System.currentTimeMillis(), boat, race.getMarkRounding(currentLeg)));

        if (nextLeg == race.getCourse().getMarkSequence().size()) {
            boat.setStatus(BoatStatus.FINISHED);
            boat.setSpeed(0);
            boat.setSailOut(true);
        }

        boat.setLegNumber(nextLeg);
    }


    /**
     * Sets the boat to appropriate conditions when a boat has an OCS status.
     *
     * @param boat the boat which has the OCS status
     */
    private void statusOSCPenalty(Boat boat, Race race) {
        race.addYachtEvent(new YachtEvent(System.currentTimeMillis(), boat.getId(), YachtEventCode.OVER_START_LINE_EARLY));
        boat.setStatus(BoatStatus.OCS);

        StartPositionSetter positionSetter = race.getPositionSetter();
        boat.setCoordinate(positionSetter.getBoatPosition(boat, race.getCourse(), race.getStartingList().size()));

        boat.setSpeed(0);
        boat.setSailOut(true);
    }

}
