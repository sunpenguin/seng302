package seng302.team18.visualiser.display;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;


/**
 * Class to Display time on a JavaFX label in a stop-watch fashion.
 */
public class StopWatchClock extends Clock {

    public StopWatchClock(Label timeLabel) {
        super(timeLabel);
    }


    /**
     * Updates the label to match the time set in setTime.
     * @param now unused.
     */
    @Override
    public void handle(long now) {
        if (time < 0) {
            timeLabel.setTextFill(Color.RED);
        } else {
            timeLabel.setTextFill(Color.BLACK);
        }
        timeLabel.setText(secondsToString(time.doubleValue()));
    }


    /**
     * Given a double, set the string displayed on the label to in MM:SS format
     * @param seconds The time to display.
     */
    @Override
    protected String secondsToString(double seconds) {
        String timeString;
        if (seconds > 0) {
            timeString = String.format(" %02.0f:%02.0f", Math.floor(Math.abs(seconds / 60)), Math.abs(seconds) % 60);
        } else {
            timeString = String.format("-%02.0f:%02.0f", Math.floor(Math.abs(seconds / 60)), Math.abs(seconds) % 60);
        }
        return timeString;
    }
}

