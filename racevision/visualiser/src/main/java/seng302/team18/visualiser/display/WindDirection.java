package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import javafx.scene.shape.Polygon;
import seng302.team18.model.Race;

/**
 * The class that manages the wind direction
 */
public class WindDirection extends AnimationTimer {

    private Race race;
    private Polygon arrow;
    private double direction;

    public WindDirection(Race race, Polygon arrow, double direction) {
        this.race = race;
        this.arrow = arrow;
        this.direction = direction;
    }

    @Override
    public void handle(long currentTime) {
        if (direction < 0) {
            return;
        }
        double newWindDirection = race.getCourse().getWindDirection();
        updateWindDirection(newWindDirection);
    }

    private void updateWindDirection(double newWindDirection) {
        arrow.setRotate(newWindDirection);
    }
}
