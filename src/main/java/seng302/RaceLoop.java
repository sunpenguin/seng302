package seng302;

import javafx.animation.AnimationTimer;


/**
 * Created by dhl25 on 16/03/17.
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
        double secondsElapsed = (currentTime - previousTime) / 1e9f; //converting from nanoseconds to seconds
        System.out.println("seconds elapsed = " + secondsElapsed);
        previousTime = currentTime;
//        System.out.println(1);
        race.updateBoats(secondsElapsed);
//        System.out.println(2);
        renderer.renderBoats();
//        System.out.println(3);
    }
}

