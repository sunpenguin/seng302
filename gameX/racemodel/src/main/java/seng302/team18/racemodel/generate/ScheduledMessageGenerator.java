package seng302.team18.racemodel.generate;

/**
 * Abstract base class to generate messages to be sent on a regular schedule.
 */
public abstract class ScheduledMessageGenerator extends MessageGenerator {
    private long lastSent;
    private final int frequency;

    /**
     * Constructor used by subclasses.
     *
     * @param frequency the frequency at which the message is to be sent (Hz)
     * @param type      the message type
     */
    public ScheduledMessageGenerator(int frequency, int type) {
        super(type);
        this.frequency = frequency;
    }

    /**
     * Checks if it is time to encode the message, delegating this if it is
     *
     * @param currTime the current time
     * @return True if it is time to encode, otherwise False.
     */
    public boolean isTimeToSend(long currTime) {
        if ((currTime - lastSent) > (1000d / frequency)) {
            lastSent = currTime;
            return true;
        }
        return false;
    }

}
