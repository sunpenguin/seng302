package seng302;

import javafx.animation.AnimationTimer;


/**
 * Updates a Race and Renders it
 */
public class RaceLoop extends AnimationTimer {
    private long previousTime = 0;
    private Race race;
    private RaceRenderer renderer;

    /**
     * Constructor for the RaceLoop class.
     *
     * @param race the race to be updated
     * @param renderer the renderer that updates with the race
     */
    public RaceLoop(Race race, RaceRenderer renderer) {
        this.race = race;
        this.renderer = renderer;
    }

    @Override
    public void handle(long currentTime) {
        if (previousTime == 0) {
            previousTime = currentTime;
            return;
        }
        float milliSecondsElapsed = (currentTime - previousTime) / 1e6f; //converting from nanoseconds to milliseconds
        previousTime = currentTime;
        race.updateBoats(milliSecondsElapsed);
        renderer.renderBoats();
    }
}

