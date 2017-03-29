package seng302;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.AnchorPane;
import seng302.display.FPSReporter;
import seng302.display.RaceRenderer;
import seng302.model.Race;


/**
 * Created by dhl25 on 16/03/17.
 */
public class RaceLoop extends AnimationTimer {
    private long previousTime = 0;
    private double secondsElapsedSinceLastFpsUpdate = 0d;
    private int framesSinceLastFpsUpdate = 0;
    private Race race;
    private RaceRenderer renderer;
    private FPSReporter fpsReporter;
    private AnchorPane raceViewAnchorPane;
    private int frameCount = 0;

    /**
     * Constructor for the RaceLoop class.
     *
     * @param race the race to be updated
     * @param renderer the renderer that updates with the race
     */
    public RaceLoop(Race race, RaceRenderer renderer, FPSReporter fpsReporter, AnchorPane raceViewAnchorPane) {
        this.race = race;
        this.renderer = renderer;
        this.fpsReporter = fpsReporter;
        this.raceViewAnchorPane = raceViewAnchorPane;
    }

    @Override
    public void handle(long currentTime) {
        frameCount++;

        if (previousTime == 0) {
            previousTime = currentTime;
            return;
        }

        double secondsElapsed = (currentTime - previousTime) / 1e9f; // converting from nanoseconds to seconds
        previousTime = currentTime;
        race.updateBoats(secondsElapsed);
        renderer.renderBoats(false, frameCount);

        secondsElapsedSinceLastFpsUpdate += secondsElapsed;
        framesSinceLastFpsUpdate++;
        if (secondsElapsedSinceLastFpsUpdate >= 0.5d) {
            double fps = framesSinceLastFpsUpdate / secondsElapsedSinceLastFpsUpdate;
            fpsReporter.report(fps);
            secondsElapsedSinceLastFpsUpdate = 0;
            framesSinceLastFpsUpdate = 0;
        }

        if (frameCount >= 10) {
            frameCount = 0;
        }
    }


    @Override
    public void stop() {
        super.stop();
        previousTime = 0;
    }
}

