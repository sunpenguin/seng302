package seng302.team18.data;

/**
 * Created by jds112 on 19/04/17.
 */
public class AC35RaceStatusMessage implements MessageBody {

    private long currentTime;
    private long startTime;

    public AC35RaceStatusMessage(long currentTime, long startTime) {
        this.currentTime = currentTime;
        this.startTime = startTime;
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
}
