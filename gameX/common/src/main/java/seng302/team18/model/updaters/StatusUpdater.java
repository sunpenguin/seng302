package seng302.team18.model.updaters;

import seng302.team18.model.Boat;
import seng302.team18.model.BoatStatus;
import seng302.team18.model.Race;
import seng302.team18.model.RaceStatus;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;

/**
 * Abstract base class for classes that update the status of a race
 */
public abstract class StatusUpdater implements Updater {


    private ZonedDateTime warningTime;
    private ZonedDateTime prepTime;
    private ZonedDateTime startTime;


    /**
     * @param initialTime        time of construction
     * @param warningTimeSeconds the duration until we switch to {@link seng302.team18.model.RaceStatus#WARNING WARNING} (s)
     * @param prepTimeSeconds    the duration between {@link seng302.team18.model.RaceStatus#WARNING WARNING} and {@link seng302.team18.model.RaceStatus#PREPARATORY PREPARATORY} (s)
     * @param startTimeSeconds   the duration between {@link seng302.team18.model.RaceStatus#PREPARATORY PREPARATORY} and {@link seng302.team18.model.RaceStatus#STARTED STARTED} (s)
     */
    public StatusUpdater(ZonedDateTime initialTime, long warningTimeSeconds, long prepTimeSeconds, long startTimeSeconds) {
        this.warningTime = initialTime.plusSeconds(warningTimeSeconds);
        this.prepTime = initialTime.plusSeconds(warningTimeSeconds + prepTimeSeconds);
        this.startTime = initialTime.plusSeconds(warningTimeSeconds + prepTimeSeconds + startTimeSeconds);
    }


    @Override
    public void update(Race race, double time) {
        race.setStartTime(startTime);
        Collection<BoatStatus> finishedStatuses = Arrays.asList(BoatStatus.DNF, BoatStatus.DNS, BoatStatus.FINISHED, BoatStatus.DSQ);

        if (ZonedDateTime.now().isAfter(startTime) && isFinished(race)) {
            race.setStatus(RaceStatus.FINISHED);
            for (Boat boat : race.getStartingList()) {
                if (!finishedStatuses.contains(boat.getStatus())) {
                    boat.setStatus(BoatStatus.FINISHED);
                }
            }
            return;
        }

        if (!race.getStatus().equals(RaceStatus.FINISHED)) {
            if (ZonedDateTime.now().isAfter(race.getStartTime())) {
                race.setStatus(RaceStatus.STARTED);
                race.getStartingList().stream()
                        .filter(boat -> boat.getStatus().equals(BoatStatus.PRE_START))
                        .forEach(boat -> boat.setStatus(BoatStatus.RACING));
            } else if (ZonedDateTime.now().isAfter(prepTime)) {
                race.setStatus(RaceStatus.PREPARATORY);
            } else if (ZonedDateTime.now().isAfter(warningTime)) {
                race.setStatus(RaceStatus.WARNING);
            } else {
                race.setStatus(RaceStatus.PRESTART);
            }
        }
    }


    /**
     * Checks if a regular race is finished by checking the status of each boat.
     *
     * @param race to check
     * @return if the race is finished.
     */
    protected abstract boolean isFinished(Race race);

}
