package seng302.team18.visualiser.display;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Class to handle the health system client side.
 */
public class VisualHealth {

    private Pane pane;
    private Image heartImage;
    private HBox heartBox;
    private ImageView heartView1;
    private ImageView heartView2;
    private ImageView heartView3;

    public VisualHealth(Pane pane) {
        this.pane = pane;
        setup();
    }

    private void setup() {
        heartImage = new Image("/images/race_view/heart.png");
        heartView1 = new ImageView(heartImage);
        heartView2 = new ImageView(heartImage);
        heartView3 = new ImageView(heartImage);
        heartBox = new HBox(heartView1, heartView2, heartView3);

        pane.getChildren().add(heartBox);
        heartBox.setLayoutX(100);
        heartBox.setLayoutY(10);
    }
}
