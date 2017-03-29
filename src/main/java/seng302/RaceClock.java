//package seng302;
//
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.scene.control.Label;
//import javafx.scene.paint.Color;
//import javafx.util.Duration;
//
////import java.time.ZonedDateTime;
////import java.time.format.DateTimeFormatter;
//
//
//public class RaceClock {
//
//    private Timeline timeline;
//    private Label timerLabel;
//    private double timeSeconds;
//    private Duration time;
//    private Race race;
//    private double timeScaleFactor;
//    private final double KMPH_TO_MPS = 1000.0 / 3600.0;
//    private String timeString;
//
//    public RaceClock(Label timerLabel, Race race) {
//        this.timerLabel = timerLabel;
//        this.race = race;
//        this.timeScaleFactor = race.getCourse().getCourseDistance()
//                        / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / race.getDuration();
//        time = Duration.ZERO;
//        timerLabel.setTextFill(Color.BLACK);
//        timerLabel.setStyle("-fx-font-size: 2em;");
//        timerLabel.setText("-00:00");
//    }
//
////    public RaceClock(Label timerLabel, Race race, ZonedDateTime zonedDateTime) {
////        this.timerLabel = timerLabel;
////        this.race = race;
////        this.timeScaleFactor = race.getCourse().getCourseDistance()
////                / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / race.getDuration();
////
////        timerLabel.setTextFill(Color.BLACK);
////        timerLabel.setStyle("-fx-font-size: 2em;");
////        timerLabel.setText("-00:00");
////
////        time = Duration.parse(zonedDateTime.format(DateTimeFormatter.ofPattern("HH'Hmm'Mss'S")));
////    }
//
//    public void start() {
//        timeline = new Timeline(
//                new KeyFrame(Duration.millis(100), t -> {
//                    if (race.isFinished()) {
//                        timeline.stop();
//                    } else {
//                        Duration duration = ((KeyFrame) t.getSource()).getTime();
//                        time = time.add(duration.multiply(timeScaleFactor));
//                        timeSeconds = time.toSeconds();
//                        secondsToString(timeSeconds);
//                        timerLabel.textProperty().set(timeString);
//                    }
//                }
//                ));
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();
//    }
//
//
//    public void stop() {
//        timeline.stop();
//    }
//
//    private void secondsToString(double pTime) {
//        pTime = (int) pTime;
//        timeString = String.format(" %02.0f:%02.0f", (Math.floor(pTime / 60)), (pTime % 60));
//    }
//
//    public void setRaceDuration(double raceDuration) {
//        this.timeScaleFactor = race.getCourse().getCourseDistance()
//                / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / raceDuration;
//    }
//}


package seng302;


import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;


public class RaceClock extends AnimationTimer{

    private Label timerLabel;
    private Duration time;
    private Race race;
    private double timeScaleFactor;
    private final double KMPH_TO_MPS = 1000.0 / 3600.0;
    private String timeString;

    private double previousTime;
    private double totalTime = -10;

    public RaceClock(Label timerLabel, Race race) {
        this.timerLabel = timerLabel;
        this.race = race;
        this.timeScaleFactor = race.getCourse().getCourseDistance()
                / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / race.getDuration();
        time = Duration.ZERO;
        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setStyle("-fx-font-size: 2em;");
        timerLabel.setText("-00:00");
    }

    @Override
    public void handle(long currentTime) {
        if (previousTime == 0) {
            previousTime = currentTime;
            return;
        }

        double secondsElapsed = ((currentTime - previousTime) / 1e9f) * timeScaleFactor; // converting from nanoseconds to seconds
        previousTime = currentTime;
        totalTime += secondsElapsed;

        secondsToString(totalTime);
        timerLabel.setText(timeString);
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

