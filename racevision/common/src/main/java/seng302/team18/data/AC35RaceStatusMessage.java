package seng302.team18.data;

import java.util.Map;

/**
 * Created by jds112 on 19/04/17.
 */
public class AC35RaceStatusMessage implements MessageBody {

    private long currentTime;
    private long startTime;
    private Map <Integer, Long> boatStatus;

    public AC35RaceStatusMessage(long currentTime, long startTime, Map <Integer, Long> boatStatus) {
        this.currentTime = currentTime;
        this.startTime = startTime;
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

    public Map<Integer, Long> getBoatStatus() {
        return boatStatus;
    }
}
