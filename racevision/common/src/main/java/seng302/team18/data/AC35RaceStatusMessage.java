package seng302.team18.data;

import java.util.List;
import java.util.Map;

/**
 * Created by jds112 on 19/04/17.
 */
public class AC35RaceStatusMessage implements MessageBody {

    private long currentTime;
    private long startTime;
    private double windDirection;
    private Map <Integer, List> boatStatus;

    private int BOAT_STATUS_POSITION = 0;
    private int LEG_POSITION = 1;
    private int ESTIMATED_TIME_POSITION = 2;


    public AC35RaceStatusMessage(long currentTime, long startTime, double windDirection, Map <Integer, List> boatStatus) {
        this.currentTime = currentTime;
        this.startTime = startTime;
        this.windDirection = windDirection;
        this.boatStatus = boatStatus;
    }

    @Override
    public int getType() {
        return AC35MessageType.RACE_STATUS.getCode();
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public Map<Integer, List> getBoatStatus() {
        return boatStatus;
    }

    public int getBoatStatusPosition() {
        return BOAT_STATUS_POSITION;
    }

    public int getLegPosition() {
        return LEG_POSITION;
    }

    public int getEstimatedTimePosition() {
        return ESTIMATED_TIME_POSITION;
    }
}
