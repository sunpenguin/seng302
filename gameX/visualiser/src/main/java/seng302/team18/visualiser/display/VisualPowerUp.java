package seng302.team18.visualiser.display;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import seng302.team18.model.Boat;

/**
 * Class for displaying an image for the currently held power up
 */
public class VisualPowerUp {

    private Image powerImage;
    private Image sharkImage1;
    private HBox powerBox;
    private ImageView speedView;
    private ImageView sharkView1;
    private Boat boat;


    /**
     * Construct a VisualPowerUp for displaying the currently held or currently activated PowerUp.
     *
     * @param pane to place visual representation of power up on.
     * @param boat associated with the power
     */
    public VisualPowerUp(Pane pane, Boat boat) {
        this.boat = boat;
        powerImage = new Image("/images/race_view/Arrow2.gif");
        sharkImage1 = new Image("/images/race_view/reefShark.gif");
        powerBox = new HBox();
        powerBox.setLayoutX(135);
        powerBox.setLayoutY(80);
        speedView = new ImageView(powerImage);
        sharkView1 = new ImageView(sharkImage1);
        pane.getChildren().add(powerBox);
    }


    /**
     * Display the current power up.
     */
    public void display() {
        boat.expirePowerUp();
        powerBox.getChildren().clear();
        if (boat.getPowerUp() != null) {
            try {
                switch (boat.getPowerUp().getType()) {
                    case SPEED:
                        powerBox.getChildren().add(speedView);
                        break;
                    case SHARK:
                        powerBox.getChildren().add(sharkView1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
