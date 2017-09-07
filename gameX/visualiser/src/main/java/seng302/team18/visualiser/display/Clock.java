package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

/**
 * Abstract super class for a visual clock.
 */
public abstract class Clock extends AnimationTimer {

    protected Label timeLabel;
    protected Long time = Long.MIN_VALUE;

    /**
     * Construct a new Clock.
     * @param timeLabel JavaFX Label to display the time on.
     */
    public Clock(Label timeLabel) {
        this.timeLabel = timeLabel;
        timeLabel.setText("");
    }


    /**
     * Updates the label to match the time set in setTime.
     * @param now unused.
     */
    @Override
    public abstract void handle(long now);


    /**
     * Given a double, set the string displayed on the label to in MM:SS format
     * @param seconds The time to display.
     */
    protected abstract String secondsToString(double seconds);


    /**
     * Sets the time of the clock in seconds.
     * @param time in seconds.
     */
    public void setTime(Long time) { // seconds please
        this.time = time;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clock clock = (Clock) o;

        if (timeLabel != null ? !timeLabel.equals(clock.timeLabel) : clock.timeLabel != null) return false;
        return time != null ? time.equals(clock.time) : clock.time == null;
    }


    @Override
    public int hashCode() {
        int result = timeLabel != null ? timeLabel.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Clock{" +
                "timeLabel=" + timeLabel +
                ", time=" + time +
                '}';
    }
}
