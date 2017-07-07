package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import seng302.team18.messageparsing.*;
import seng302.team18.visualiser.messageinterpreting.MessageInterpreter;
import seng302.team18.model.Race;
import seng302.team18.visualiser.display.CourseRenderer;
import seng302.team18.visualiser.display.FPSReporter;
import seng302.team18.visualiser.display.RaceRenderer;
import sun.print.BackgroundLookupListener;


/**
 * An AnimationTimer to update the view of the race
 */
public class RaceLoop extends AnimationTimer {
    private long previousTime = 0;
    private double secondsElapsedSinceLastFpsUpdate = 0d;
    private int framesSinceLastFpsUpdate = 0;
    private RaceRenderer renderer;
    private CourseRenderer courseRenderer;
    private FPSReporter fpsReporter;

    /**
     * Constructor for the RaceLoop class.
     *
     * @param renderer thing that renders the boats
     * @param courseRenderer thing that renders the course
     * @param fpsReporter thing that updates fps
     */
    public RaceLoop(RaceRenderer renderer, CourseRenderer courseRenderer, FPSReporter fpsReporter) {
        this.renderer = renderer;
        this.fpsReporter = fpsReporter;
        this.courseRenderer = courseRenderer;
    }

    @Override
    public void handle(long currentTime) {
        if (previousTime == 0) {
            previousTime = currentTime;
            return;
        }
        double secondsElapsed = (currentTime - previousTime) / 1e9f; // converting from nanoseconds to seconds
        previousTime = currentTime;
        updateFps(secondsElapsed);
        updateView();
    }

    /**
     * Call each renderer and update the display of the race.
     */
    private void updateView() {
        renderer.renderBoats();
        courseRenderer.renderCourse();
        renderer.drawTrails();
    }

    /**
     * Update the FPS label showing the current frame rate.
     * @param secondsElapsed The seconds elapsed since the last update.
     */
    private void updateFps(double secondsElapsed) {
        secondsElapsedSinceLastFpsUpdate += secondsElapsed;
        framesSinceLastFpsUpdate++;
        if (secondsElapsedSinceLastFpsUpdate >= 0.5d) {
            double fps = framesSinceLastFpsUpdate / secondsElapsedSinceLastFpsUpdate;
            fpsReporter.report(fps);
            secondsElapsedSinceLastFpsUpdate = 0;
            framesSinceLastFpsUpdate = 0;
        }
    }


    @Override
    public void stop() {
        super.stop();
        previousTime = 0;
    }
}

