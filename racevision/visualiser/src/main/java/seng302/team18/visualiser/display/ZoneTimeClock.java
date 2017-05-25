package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class to display time from a specific time-zone on a JavaFX Label.
 */
public class ZoneTimeClock extends AnimationTimer {

    private Label timerLabel;
    private ZonedDateTime zonedDateTime;
    private long previousTime = 0;
    private final DateTimeFormatter formatter;

    public ZoneTimeClock(Label timerLabel, DateTimeFormatter timeFormatter, ZonedDateTime currentTime) {
        this.timerLabel = timerLabel;
        this.formatter = timeFormatter;
        zonedDateTime = currentTime;
    }

    @Override
    public void handle(long currentTime) {
        if (previousTime == 0) {
            previousTime = currentTime;
            return;
        }

        double nanoSecondsElapsed = (currentTime - previousTime);// * timeScaleFactor;
        previousTime = currentTime;

        zonedDateTime = zonedDateTime.plusNanos((long) nanoSecondsElapsed);
        timerLabel.setText(zonedDateTime.format(formatter));
    }


}