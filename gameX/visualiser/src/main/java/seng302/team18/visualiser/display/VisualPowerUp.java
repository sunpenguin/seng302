package seng302.team18.visualiser.display;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import seng302.team18.model.Boat;

/**
 * Created by spe76 on 11/09/17.
 */
public class VisualPowerUp {

    private Image powerImage;
    private Image sharkImage1;
    private HBox powerBox;
    private ImageView speedView;
    private ImageView sharkView1;
    private Boat boat;


    /**
     * Construct a VisualHealth for displaying hearts to represent player health in certain game modes.
     *
     * @param pane Pane to draw hearts on.
     * @param boat Player's boat to represent lives for.
     */
    public VisualPowerUp(Pane pane, Boat boat) {
        this.boat = boat;
        powerImage = new Image("/images/race_view/Arrow2.gif");
        sharkImage1 = new Image("/images/race_view/heart.png"); // TODO Change to proper shark image hqi19 11/09/17
        powerBox = new HBox();
        powerBox.setLayoutX(135);
        powerBox.setLayoutY(80);
        speedView = new ImageView(powerImage);
        sharkView1 = new ImageView(sharkImage1);
        pane.getChildren().add(powerBox);
    }


    /**
     * Display a number of hearts according to how many lives the player has.
     * Only re-draws the hearts if the current number displayed does not match the player's lives.
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
