package seng302.display;


import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import seng302.model.Race;


/**
 * Class to Display time on a JavaFX label in a stop-watch fashion.
 */
public class RaceClock extends AnimationTimer{

    private Label timerLabel;
    private Race race;
    private double timeScaleFactor;
    private final double KMPH_TO_MPS = 1000.0 / 3600.0;
    private String timeString;

    private double previousTime;
    private double totalTime;


    /**
     * Construct a new RaceClock.
     * @param timerLabel JavaFX Label to display the time on.
     * @param race The associated race for getting the required time scaling.
     * @param startTime The initial time to set the clock to.
     */
    public RaceClock(Label timerLabel, Race race, double startTime) {
        this.timerLabel = timerLabel;
        this.race = race;
        this.timeScaleFactor = race.getCourse().getCourseDistance()
                / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / race.getDuration();
        totalTime = startTime;

        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setStyle("-fx-font-size: 2em;");

        secondsToString(totalTime);
        timerLabel.setText(timeString);
    }


    /**
     * Overridden handle method.
     * Each frame, update the label by the time since the last frame multiplied by the timeScaleFactor
     * @param currentTime The timestamp of the current frame given in nanoseconds.
     */
    @Override
    public void handle(long currentTime) {
        if (previousTime == 0) {
            previousTime = currentTime;
            return;
        }

        if (race.isFinished()) {
            stop();
        } else {
            double secondsElapsed = ((currentTime - previousTime) / 1e9f) * timeScaleFactor; // converting from nanoseconds to seconds
            previousTime = currentTime;
            totalTime += secondsElapsed;
            secondsToString(totalTime);
            timerLabel.setText(timeString);
        }
    }

    @Override
    public void stop() {
        super.stop();
        previousTime = 0;
    }


    /**
     * Given a double, set the string displayed on the label to in MM:SS format
     * @param pTime The time to display.
     */
    private void secondsToString(double pTime) {
        pTime = (int) pTime;
        timeString = String.format(" %02.0f:%02.0f", Math.floor(Math.abs(pTime / 60)), Math.abs(pTime) % 60);
    }
}

