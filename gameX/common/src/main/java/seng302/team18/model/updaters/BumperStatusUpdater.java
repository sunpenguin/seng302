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
 * Created by dhl25 on 9/09/17.
 */
public class BumperStatusUpdater extends StatusUpdater {


    public BumperStatusUpdater(ZonedDateTime initialTime, long warningTimeSeconds, long prepTimeSeconds, long startTimeSeconds) {
        super(initialTime, warningTimeSeconds, prepTimeSeconds, startTimeSeconds);
    }


    /**
     * Checks if a regular race is finished by checking the status of each boat.
     *
     * @param race to check
     * @return if the race is finished.
     */
    protected boolean isFinished(Race race) {
//        System.out.println("BumperStatusUpdater::isFinished");
        Collection<BoatStatus> finishedStatuses = Arrays.asList(BoatStatus.DNF, BoatStatus.DNS, BoatStatus.FINISHED, BoatStatus.DSQ);
        List<Boat> startingList = race.getStartingList();
        int numFinished = (int) startingList
                .stream()
                .filter(boat -> finishedStatuses.contains(boat.getStatus()))
                .count();
        return startingList.size() - 1 == numFinished && startingList.size() != 0;
    }

}
