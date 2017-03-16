package seng302;

import javafx.animation.AnimationTimer;


/**
 * Created by dhl25 on 16/03/17.
 */
public class RaceLoop extends AnimationTimer {
    private long previousTime = 0;
    private Race race;
    private RaceRenderer renderer;

    public RaceLoop(Race race, RaceRenderer renderer) {
        this.race = race;
        this.renderer = renderer;
    }

    @Override
    public void handle(long currentTime)
    {
        if (previousTime == 0) {
            previousTime = currentTime;
            return;
        }

        float milliSecondsElapsed = (currentTime - previousTime) / 1e6f; //converting from nanoseconds to milllseconds
        previousTime = currentTime;

        race.updateBoats(milliSecondsElapsed);
        renderer.renderBoats();
        }
    }
