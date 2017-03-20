package seng302;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class RaceTimer {

    private Timeline timeline;
    private Label timerLabel;
    private DoubleProperty timeSeconds = new SimpleDoubleProperty();
    private Duration time = Duration.ZERO;
    private Group group;

    public RaceTimer(Group group, Label timerLabel) {
        this.group = group;
        this.timerLabel = timerLabel;
    }

    public void start() {
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setStyle("-fx-font-size: 2em;");

        timeline = new Timeline(
                new KeyFrame(Duration.millis(100), t -> {
                    Duration duration = ((KeyFrame)t.getSource()).getTime();
                    time = time.add(duration);
                    timeSeconds.set(time.toSeconds());
                }
                ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}

