package seng302;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;


public class RaceClock {

    private Timeline timeline;
    private Label timerLabel;
    private double timeSeconds;
    private Duration time = Duration.ZERO;
    private Race race;
    private double timeScaleFactor;
    private final double KMPH_TO_MPS = 1000.0 / 3600.0;
    private String timeString;

    public RaceClock(Label timerLabel, Race race, double raceDuration) {
        this.timerLabel = timerLabel;
        this.race = race;
        this.timeScaleFactor = race.getCourse().getCourseDistance()
                        / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / raceDuration;

        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setStyle("-fx-font-size: 2em;");
        timerLabel.setText("-00:00");
    }

    public void start() {
        timeline = new Timeline(
                new KeyFrame(Duration.millis(100), t -> {
                    if (race.isFinished()) {
                        timeline.stop();
                    } else {
                        Duration duration = ((KeyFrame) t.getSource()).getTime();
                        time = time.add(duration.multiply(timeScaleFactor));
                        timeSeconds = time.toSeconds();
                        secondsToString(timeSeconds);
                        timerLabel.textProperty().set(timeString);
                    }
                }
                ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    public void stop() {
        timeline.stop();
    }

    private void secondsToString(double pTime) {
        pTime = (int) pTime;
        timeString = String.format(" %02.0f:%02.0f", (Math.floor(pTime / 60)), (pTime % 60));
    }

    public void setRaceDuration(double raceDuration) {
        this.timeScaleFactor = race.getCourse().getCourseDistance()
                / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / raceDuration;
    }
}

