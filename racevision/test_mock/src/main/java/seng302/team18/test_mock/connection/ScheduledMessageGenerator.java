package seng302.team18.test_mock.connection;

/**
 * Abstract base class for messages to be sent on a regular schedule.
 */
public abstract class ScheduledMessageGenerator extends MessageGenerator {
    private long lastSent;
    private final int frequency;


    ScheduledMessageGenerator(int frequency, int type) {
        super(type);
        this.frequency = frequency;

    }

    /**
     * Checks if it is time to send the message, delegating this if it is
     *
     * @param currTime the current time
     */
    public boolean isTimeToSend(long currTime) {
        if ((currTime - lastSent) > (1000 / frequency)) {
            lastSent = currTime;
            return true;
        }
        return false;
    }

}
