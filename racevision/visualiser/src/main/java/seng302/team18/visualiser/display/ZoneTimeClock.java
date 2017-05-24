package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class to display time from a specific time-zone on a JavaFX Label.
 */
public class ZoneTimeClock extends AnimationTimer {

    private Label timeLabel;
    private ZonedDateTime time;
    private DateTimeFormatter formatter;
    private ZoneId raceZone;

    /**
     * Construct a new ZoneTimeClock.
     * @param timeLabel JavaFX Label to display the time on.
     * @param formatter formatter for time when displaying on label.
     */
    public ZoneTimeClock(Label timeLabel, DateTimeFormatter formatter, ZoneId raceZone) {
        this.timeLabel = timeLabel;
        this.formatter = formatter;
        this.raceZone = raceZone;
        timeLabel.setTextFill(Color.BLACK);
        timeLabel.setStyle("-fx-font-size: 2em;");
        timeLabel.setText("");
    }


    /**
     * Sets the time of the clock in seconds.
     * @param time current time.
     */
    public void setTime(ZonedDateTime time) { // seconds please
        this.time = time;
    }


    /**
     * Updates the label to match the time set in setTime.
     * @param now unused.
     */
    @Override
    public void handle(long now) {
        ZonedDateTime displayTime = (time != null) ? time : ZonedDateTime.now(raceZone);
        timeLabel.setText(displayTime.format(formatter));
    }


}
