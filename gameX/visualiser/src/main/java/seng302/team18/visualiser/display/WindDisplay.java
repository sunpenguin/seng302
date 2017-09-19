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

    int count = 0;
    int nwSpeed = 15;
    boolean countUp = true;

    @Override
    public void handle(long currentTime) {
        if (direction < 0) {
            return;
        }
        double newWindDirection = (race.getCourse().getWindDirection() + 180 + count) % 360;
        double newWindSpeed = race.getCourse().getWindSpeed();

        if (countUp) {
            if (nwSpeed > 30) {
                nwSpeed = 30;
                countUp = false;
            } else {
                nwSpeed++;
            }
        } else {
            if (nwSpeed < 15) {
                nwSpeed = 15;
                countUp = true;
            } else {
                nwSpeed--;
            }
        }

        newWindSpeed = nwSpeed;

        speedLabel.setText(String.format("%.2f", newWindSpeed) + " knots");

        setWindImage(newWindSpeed);

        updateWindDisplay(newWindDirection, newWindSpeed);

        count += 1;
    }


    private void updateWindDisplay(double newWindDirection, double newWindSpeed) {
        if (newWindSpeed / scaleY <= 0.3) {
            arrow.setScaleY(0.3);
        } else if (newWindSpeed / scaleY <= 0.75) {
            arrow.setScaleY(newWindSpeed / scaleY);
        } else {
            arrow.setScaleY(0.75);
        }
        windView.setRotate(newWindDirection);
    }


    private void setWindImage(double speed) {
        if (speed <= 20) {
            windView.setImage(windImageSmall);
        } else if (speed <= 25) {
            windView.setImage(windImageMedium);
        } else {
            windView.setImage(windImageBig);
        }
    }
}