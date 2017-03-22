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

    private String timeString;

    public RaceClock(Label timerLabel) {
        this.timerLabel = timerLabel;

        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setStyle("-fx-font-size: 2em;");
        timerLabel.setText("-00:00");
    }

    public void start() {
        timeline = new Timeline(
                new KeyFrame(Duration.millis(100), t -> {
                    Duration duration = ((KeyFrame)t.getSource()).getTime();
                    time = time.add(duration);
                    timeSeconds = time.toSeconds();
                    secondsToString(timeSeconds);
                    timerLabel.textProperty().set(timeString);
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


    public Duration getTime() {
        return time;
    }
}

