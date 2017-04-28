package seng302.team18.messageparsing;

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
    private Map <Integer, List> boatStatus;

    private int BOAT_STATUS_POSITION = 0;
    private int LEG_POSITION = 1;
    private int ESTIMATED_TIME_POSITION = 2;


    /**
     * Constructor for AC35RaceStatusMessage.
     * @param currentTime of the race.
     * @param raceStatus of the race.
     * @param startTime of the race.
     * @param windDirection of the race.
     * @param boatStatus a map of the boats id to the status, leg, and estimated time to next mark.
     */
    public AC35RaceStatusMessage(long currentTime, int raceStatus, long startTime, double windDirection, Map <Integer, List> boatStatus) {
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
     * @return the current time of the race.
     */
    public long getCurrentTime() {
        return currentTime;
    }

    /**
     * Getter for the start time of the race.
     * @return the start time of the race.
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
    public Map<Integer, List> getBoatStatus() {
        return boatStatus;
    }

    /**
     * Getter for the index of the boat status.
     * @return the index of the boat status.
     */
    public int getBoatStatusPosition() {
        return BOAT_STATUS_POSITION;
    }

    /**
     * Getter for the index of the leg number.
     * @return the index of the leg number.
     */
    public int getLegPosition() {
        return LEG_POSITION;
    }

    /**
     * Getter for the index of the estimated time at next mark.
     * @return the index of the estimated time at next mark.
     */
    public int getEstimatedTimePosition() {
        return ESTIMATED_TIME_POSITION;
    }

    /**
     * Getter for the current status of the race.
     * @return the current status of the race.
     */
    public int getRaceStatus() {
        return raceStatus;
    }
}
