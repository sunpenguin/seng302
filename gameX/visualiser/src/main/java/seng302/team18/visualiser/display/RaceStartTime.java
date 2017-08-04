package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import seng302.team18.model.Race;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class to display a fixed time of a specific time-zone on a JavaFX Label.
 * This is currently only used for the start time label.
 */
public class RaceStartTime extends AnimationTimer {

    private Label timerLabel;
    private final DateTimeFormatter formatter;
    private Race race;


    /**
     * Constructor for a RaceStartTime.
     *
     * @param timerLabel the label to set the text for.
     * @param timeFormatter the format to display the time.
     * @param race the race to get the time from.
     */
    public RaceStartTime(Label timerLabel, DateTimeFormatter timeFormatter, Race race) {
        this.timerLabel = timerLabel;
        this.formatter = timeFormatter;
        this.race = race;
    }

    @Override
    public void handle(long currentTime) {
        timerLabel.setText(race.getStartTime().format(formatter));
    }


}