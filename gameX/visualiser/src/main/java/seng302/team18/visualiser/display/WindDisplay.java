package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import seng302.team18.visualiser.ClientRace;

/**
 * The class that manages the wind direction
 */
public class WindDisplay extends AnimationTimer {

    private final ClientRace race;
    private final Label speedLabel;
    private final double direction;

    private static final Image WIND_IMAGE_SMALL = new Image("/images/race_view/wind_arrow_small.png");
    private static final Image WIND_IMAGE_MEDIUM = new Image("/images/race_view/wind_arrow_medium.png");
    private static final Image WIND_IMAGE_BIG = new Image("/images/race_view/wind_arrow_big.png");
    private final ImageView windView = new ImageView(WIND_IMAGE_SMALL);

    private static final double OFFSET_X = WIND_IMAGE_SMALL.getWidth() / 4;
    private static final double OFFSET_Y = WIND_IMAGE_SMALL.getHeight() / 4;
    private static final int LIGHT_WIND = 20;
    private static final int MODERATE_WIND = 25;


    public WindDisplay(ClientRace race, Label speedLabel, Pane pane) {
        this.race = race;
        this.speedLabel = speedLabel;
        this.direction = race.getCourse().getWindDirection();
        pane.getChildren().add(windView);

        windView.setLayoutX(OFFSET_X);
        windView.setLayoutY(OFFSET_Y);

        speedLabel.setLayoutX(OFFSET_X);
        speedLabel.setLayoutY(OFFSET_Y * 1.5 + WIND_IMAGE_SMALL.getHeight());
        speedLabel.getStyleClass().add("windLabel");
    }


    @Override
    public void handle(long currentTime) {
        if (direction < 0) {
            return;
        }
        double newWindDirection = (race.getCourse().getWindDirection() + 180) % 360;
        double newWindSpeed = race.getCourse().getWindSpeed();

        speedLabel.setText(String.format("%.2f", newWindSpeed) + " knots");

        setWindImage(newWindSpeed);
        updateWindDisplay(newWindDirection);
    }


    /**
     * Sets the rotation of the ImageView containing the wind
     *
     * @param newWindDirection angle to set the ImageView to
     */
    private void updateWindDisplay(double newWindDirection) {
        windView.setRotate(newWindDirection);
    }


    /**
     * Sets the Image in the ImageView according to 3 different zones of wind speed.
     *
     * @param speed the speed in knots
     */
    private void setWindImage(double speed) {
        if (speed <= LIGHT_WIND) {
            windView.setImage(WIND_IMAGE_SMALL);
        } else if (speed <= MODERATE_WIND) {
            windView.setImage(WIND_IMAGE_MEDIUM);
        } else {
            windView.setImage(WIND_IMAGE_BIG);
        }
    }
}