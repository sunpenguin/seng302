package seng302.team18.model.updaters;

import seng302.team18.model.*;

/**
 * Class to check ing boats have rounded a mark
 */
public class MarkRoundingUpdater implements Updater {

    @Override
    public void update(Race race) {
        for (Boat boat : race.getStartingList()) {
            checkForRounding(boat, race);
        }
    }


    /**
     * Detects roundings and updates boat status.
     *
     * @param boat to update.
     */
    private void checkForRounding(Boat boat, Race race) {
        if (boat.getStatus().equals(BoatStatus.RACING) && race.getDetector().hasPassedDestination(boat, race.getCourse())) {
            setNextLeg(boat, boat.getLegNumber() + 1, race);
        } else if (boat.getStatus().equals(BoatStatus.PRE_START) && boat.getLegNumber() == 0
                && race.getDetector().hasPassedDestination(boat, race.getCourse())) {
            statusOSCPenalty(boat, race);
        } else if (boat.getStatus().equals(BoatStatus.OCS) && race.getCurrentTime().isAfter(race.getStartTime().plusSeconds(5))) {
            race.addYachtEvent(new YachtEvent(System.currentTimeMillis(), boat.getId(), YachtEventCode.OCS_PENALTY_COMPLETE));
            boat.setStatus(BoatStatus.RACING);
            boat.setSpeed(boat.getBoatTWS(race.getCourse().getWindSpeed(), race.getCourse().getWindDirection()));
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

        boat.setCoordinate(race.getStartPosition(boat, boat.getLength() * 6));

        boat.setSpeed(0);
        boat.setSailOut(true);
    }
}
