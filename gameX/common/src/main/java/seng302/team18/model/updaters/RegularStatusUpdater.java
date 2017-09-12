package seng302.team18.model.updaters;

import seng302.team18.model.Boat;
import seng302.team18.model.BoatStatus;
import seng302.team18.model.Race;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Handles switching the race into the correct status (warning, started, finished etc) at the appropriate times.
 */
public class RegularStatusUpdater extends StatusUpdater {


    /**
     * Construct a new RegularStatusUpdater
     * Used for updating a normal race. Race is deemed finished when ALL players are in a finished state.
     *
     * @param initialTime        time of construction
     * @param warningTimeSeconds the duration until we switch to {@link seng302.team18.model.RaceStatus#WARNING WARNING} (s)
     * @param prepTimeSeconds    the duration between {@link seng302.team18.model.RaceStatus#WARNING WARNING} and {@link seng302.team18.model.RaceStatus#PREPARATORY PREPARATORY} (s)
     * @param startTimeSeconds   the duration between {@link seng302.team18.model.RaceStatus#PREPARATORY PREPARATORY} and {@link seng302.team18.model.RaceStatus#STARTED STARTED} (s)
     */
    public RegularStatusUpdater(ZonedDateTime initialTime, long warningTimeSeconds, long prepTimeSeconds, long startTimeSeconds) {
        super(initialTime, warningTimeSeconds, prepTimeSeconds, startTimeSeconds);
    }


    protected boolean isFinished(Race race) {
        Collection<BoatStatus> finishedStatuses = Arrays.asList(BoatStatus.DNF, BoatStatus.DNS, BoatStatus.FINISHED, BoatStatus.DSQ);
        List<Boat> startingList = race.getStartingList();
        int numFinished = (int) startingList
                .stream()
                .filter(boat -> finishedStatuses.contains(boat.getStatus()))
                .count();
        return startingList.size() == numFinished && startingList.size() != 0;
    }
}
