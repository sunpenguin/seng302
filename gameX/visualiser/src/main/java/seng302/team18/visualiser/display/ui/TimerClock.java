package seng302.team18.visualiser.display.ui;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 * Clock for counting down.
 */
public class TimerClock extends Clock {

    public TimerClock(Label timeLabel) {
        super(timeLabel);
    }


    /**
     * Updates the label to match the time set in setTime.
     * If time reached 0, calls timeUp() to stop the AnimationTimer.
     *
     * @param now unused.
     */
    @Override
    public void handle(long now) {
        if (super.time == 0) {
            timeLabel.setTextFill(Color.RED);
            timeUp();
        }
        timeLabel.setText(secondsToString(super.time.doubleValue()));
    }


    /**
     * Stop the animation timer.
     * To be called when timer reaches 0 seconds.
     */
    private void timeUp() {
        stop();
    }


    /**
     * Given a double, set the string displayed on the label to in MM:SS format
     *
     * @param seconds The time to display.
     */
    @Override
    protected String secondsToString(double seconds) {
        return String.format(" %02.0f:%02.0f", Math.floor(Math.abs(seconds / 60)), Math.abs(seconds) % 60);
    }
}
