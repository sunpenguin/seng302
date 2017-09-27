package seng302.team18.visualiser.display.ui;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import seng302.team18.visualiser.ClientRace;

import java.time.format.DateTimeFormatter;

/**
 * Class which contains all the labels on the pre race so that they can be adjusted accordingly.
 */
public class PreRaceTimes extends AnimationTimer {

    private Label startLabel;
    private Label currentTimeLabel;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private ClientRace race;


    /**
     * Constructor for the PreRaceTimes class.
     *
     * @param startLabel the label which will hold the start time text of a race.
     * @param currentTimeLabel the label which will hold the current time text of a race.
     * @param race the race to be observed.
     */
    public PreRaceTimes(Label startLabel, Label currentTimeLabel, ClientRace race) {
        this.startLabel = startLabel;
        this.currentTimeLabel = currentTimeLabel;
        this.race = race;
    }


    @Override
    public void handle(long now) {
        startLabel.setText(race.getStartTime().format(formatter));
        currentTimeLabel.setText(race.getCurrentTime().format(formatter));
    }
}
