package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import seng302.team18.visualiser.ClientRace;

/**
 * The class that manages the wind direction
 */
public class WindDisplay extends AnimationTimer {

    private ClientRace race;
    private Polygon arrow;
    private Label speedLabel;
    private double direction;
    private double scaleY = 40;
    private Pane pane;
    private Image windImageSmall = new Image("/images/race_view/wind_arrow_small.png");
    private Image windImageMedium = new Image("/images/race_view/wind_arrow_medium.png");
    private Image windImageBig = new Image("/images/race_view/wind_arrow_big.png");
    private ImageView windView = new ImageView(windImageSmall);
    private double offsetX = windImageSmall.getWidth() / 2;
    private double offsetY = windImageSmall.getHeight() / 2;
    private final int LIGHT_WIND = 20;
    private final int MODERATE_WIND = 25;


    public WindDisplay(ClientRace race, Polygon arrow, Label speedLabel, Pane pane) {
        this.race = race;
        this.arrow = arrow;
        this.speedLabel = speedLabel;
        this.direction = race.getCourse().getWindDirection();
        this.pane = pane;
        arrow.setVisible(false);
        speedLabel.setVisible(false);
        pane.getChildren().add(windView);
        windView.setLayoutX(offsetX);
        windView.setLayoutY(offsetY);
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
     * @param speed
     */
    private void setWindImage(double speed) {
        if (speed <= LIGHT_WIND) {
            windView.setImage(windImageSmall);
        } else if (speed <= MODERATE_WIND) {
            windView.setImage(windImageMedium);
        } else {
            windView.setImage(windImageBig);
        }
    }
}