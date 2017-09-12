package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import seng302.team18.visualiser.util.PixelMapper;


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
    private PixelMapper pixelMapper;
    private VisualHealth healthDisplay;
    private VisualPowerUp powerUpDisplay;

    /**
     * Constructor for the RaceLoop class.
     *
     * @param renderer thing that renders the boats
     * @param courseRenderer thing that renders the course
     * @param fpsReporter thing that updates fps
     */
    public RaceLoop(RaceRenderer renderer, CourseRenderer courseRenderer, FPSReporter fpsReporter,
                    PixelMapper pixelMapper, VisualHealth healthDisplay, VisualPowerUp powerUpDisplay) {
        this.renderer = renderer;
        this.fpsReporter = fpsReporter;
        this.courseRenderer = courseRenderer;
        this.pixelMapper = pixelMapper;
        this.healthDisplay = healthDisplay;
        this.powerUpDisplay = powerUpDisplay;
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
        pixelMapper.calculateMappingScale();
        renderer.renderBoats();
        courseRenderer.renderCourse();
        renderer.drawTrails();
        if (null != healthDisplay) {
            healthDisplay.display();
        }
        if (null != powerUpDisplay) {
            powerUpDisplay.display();
        }
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

