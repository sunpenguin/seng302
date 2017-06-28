package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import seng302.team18.model.Race;

/**
 * The class that manages the wind direction
 */
public class WindDisplay extends AnimationTimer {

    private Race race;
    private Polygon arrow;
    private Label speedLabel;
    private double direction;
    private double speed;
    private double scaleY = 20;

    public WindDisplay(Race race, Polygon arrow, Label speedLabel) {
        this.race = race;
        this.arrow = arrow;
        this.speedLabel = speedLabel;
        this.direction = race.getCourse().getWindDirection();
        this.speed = race.getCourse().getWindSpeed();
    }

    @Override
    public void handle(long currentTime) {
        if (direction < 0) {
            return;
        }
        double newWindDirection = race.getCourse().getWindDirection();
        double newWindSpeed = race.getCourse().getWindSpeed();
        speedLabel.setText(String.format("%.4f", newWindSpeed)+ " knots");
        updateWindDisplay(newWindDirection, newWindSpeed);
    }

    private void updateWindDisplay(double newWindDirection, double newWindSpeed) {
        if (newWindSpeed / scaleY <= 0.3){
            arrow.setScaleY(0.3);
        } else if (newWindSpeed / scaleY <= 0.75) {
            arrow.setScaleY(newWindSpeed / scaleY);
        }
        arrow.setRotate(newWindDirection);
    }
}
