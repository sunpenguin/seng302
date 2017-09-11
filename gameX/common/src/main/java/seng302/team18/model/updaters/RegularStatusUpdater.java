package seng302.team18.model.updaters;

import seng302.team18.model.Boat;
import seng302.team18.model.BoatStatus;
import seng302.team18.model.Race;
import seng302.team18.model.RaceStatus;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Class for handling of switching the race into the correct status (warning, started, finished etc) at the
 * appropriate times.
 */
public class RegularStatusUpdater extends StatusUpdater {



    /**
     * Construct a new RegularStatusUpdater.
     * Used for updating a normal race. Race is deemed finished when ALL players are in a finished state.
     *
     * @param initialTime Time of construction.
     * @param warningTimeSeconds until we switch to WARNING.
     * @param prepTimeSeconds until we switch from WARNING to PREPARATORY.
     * @param startTimeSeconds until we switch from PREPARATORY to STARTED.
     */
    public RegularStatusUpdater(ZonedDateTime initialTime, long warningTimeSeconds, long prepTimeSeconds, long startTimeSeconds) {
        super(initialTime, warningTimeSeconds, prepTimeSeconds, startTimeSeconds);
    }


    /**
     * Checks if a regular race is finished by checking the status of each boat.
     *
     * @param race to check
     * @return if the race is finished.
     */
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
