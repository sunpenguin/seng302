package seng302.team18.message;

import java.util.List;
import java.util.Map;

/**
 * MessageBody that contains information about a races status.
 */
public class AC35RaceStatusMessage implements MessageBody {

    private long currentTime;
    private int raceStatus;
    private long startTime;
    private double windDirection;
    private List<AC35BoatStatusMessage> boatStatus;



    /**
     * Constructor for AC35RaceStatusMessage.
     * @param currentTime of the race in Epoch milliseconds.
     * @param raceStatus of the race in Epoch milliseconds.
     * @param startTime of the race.
     * @param windDirection of the race.
     * @param boatStatus a list of the AC35BoatStatusMessages.
     */
    public AC35RaceStatusMessage(long currentTime, int raceStatus, long startTime, double windDirection, List<AC35BoatStatusMessage> boatStatus) {
        this.currentTime = currentTime;
        this.raceStatus = raceStatus;
        this.startTime = startTime;
        this.windDirection = windDirection;
        this.boatStatus = boatStatus;
    }

    @Override
    public int getType() {
        return AC35MessageType.RACE_STATUS.getCode();
    }

    /**
     * Getter for the current time of the race.
     * @return the current time of the race in Epoch milliseconds.
     */
    public long getCurrentTime() {
        return currentTime;
    }

    /**
     * Getter for the start time of the race.
     * @return the start time of the race in Epoch milliseconds.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Getter for the wind direction of the race.
     * @return the wind direction.
     */
    public double getWindDirection() {
        return windDirection;
    }

    /**
     * Getter for the map of the boats id to the status, leg, and estimated time to next mark.
     * @return the map of the boats id to the status, leg, and estimated time to next mark.
     */
    public List<AC35BoatStatusMessage> getBoatStatus() {
        return boatStatus;
    }

    /**
     * Getter for the current status of the race.
     * @return the current status of the race.
     */
    public int getRaceStatus() {
        return raceStatus;
    }
}
