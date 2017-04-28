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
    private DateTimeFormatter formatter;
    private Long time;

    /**
     * Construct a new RaceClock.
     * @param timeLabel JavaFX Label to display the time on.
     */
    public RaceClock(Label timeLabel, DateTimeFormatter formatter) {
        this.timeLabel = timeLabel;
        this.formatter = formatter;
        time = 0L;
        timeLabel.setTextFill(Color.BLACK);
        timeLabel.setStyle("-fx-font-size: 2em;");
//        timeLabel.setText("");
    }




    public void setTime(Long time) { // seconds please
//        System.out.println("formatter null? " + formatter == null);
//        System.out.println("time null? " + time == null);
//        System.out.println("timelabel null? " + timeLabel == null);
        this.time = time;
    }


    @Override
    public void handle(long now) {
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
}

