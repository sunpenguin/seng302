package seng302.team18.visualiser.display;


import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import seng302.team18.model.Race;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Class to Display time on a JavaFX label in a stop-watch fashion.
 */
public class RaceClock extends AnimationTimer {

    private Label timeLabel;
    private Long time;

    /**
     * Construct a new RaceClock.
     * @param timeLabel JavaFX Label to display the time on.
     */
    public RaceClock(Label timeLabel) {
        this.timeLabel = timeLabel;
        time = 0L;
        //timeLabel.setTextFill(Color.BLACK);
        timeLabel.setStyle("-fx-font-size: 2em;");
        timeLabel.setText("");
    }


    /**
     * Sets the time of the clock in seconds.
     * @param time in seconds.
     */
    public void setTime(Long time) { // seconds please
        this.time = time;
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
            timeLabel.setTextFill(Color.LIGHTGREEN);
        }
        timeLabel.setText(secondsToString(time.doubleValue()));
    }


    /**
     * Given a double, set the string displayed on the label to in MM:SS format
     * @param seconds The time to display.
     */
    private String secondsToString(double seconds) {
        String timeString;
        if (seconds > 0) {
            timeString = String.format(" %02.0f:%02.0f", Math.floor(Math.abs(seconds / 60)), Math.abs(seconds) % 60);
        } else {
            timeString = String.format("-%02.0f:%02.0f", Math.floor(Math.abs(seconds / 60)), Math.abs(seconds) % 60);
        }
        return timeString;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RaceClock raceClock = (RaceClock) o;

        if (timeLabel != null ? !timeLabel.equals(raceClock.timeLabel) : raceClock.timeLabel != null) return false;
        return time != null ? time.equals(raceClock.time) : raceClock.time == null;
    }

    @Override
    public int hashCode() {
        int result = timeLabel != null ? timeLabel.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RaceClock{" +
                "timeLabel=" + timeLabel +
                ", time=" + time +
                '}';
    }
}

