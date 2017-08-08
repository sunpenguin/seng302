package seng302.team18.visualiser.display;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

/**
 * Class to handle loading and displaying the texture for the race background.
 */
public class RaceBackground {

    private TilePane backgroundPane = new TilePane();
    private Pane raceViewPane;
    private Image image;


    /**
     * Creates a new RaceBackground for displaying the image in the given path.
     * TilePane holding the tiled images placed at back of the raceViewPane.
     *
     * @param raceViewPane Pane for displaying the race on.
     * @param path Path to an image to display behind the race.
     */
    public RaceBackground(Pane raceViewPane, String path) {
        this.raceViewPane = raceViewPane;
        this.image = new Image(path);
        raceViewPane.getChildren().add(backgroundPane);
        backgroundPane.toBack();
    }


    /**
     * Render the tiled imaged by setting enough tiles on the TilePane to cover the whole screen and adding an
     * ImageView with the associated image in each one.
     * Provided this method is called whenever screen is re-sized, the TilePane will dynamically adjust to fill the screen.
     */
    public void renderBackground() {
        backgroundPane.getChildren().clear();
        backgroundPane.setPrefColumns((int) (raceViewPane.getWidth() / image.getWidth()) + 1);
        backgroundPane.setPrefRows((int) (raceViewPane.getHeight() / image.getHeight()) + 1);

        for (int i = 0; i < (backgroundPane.getPrefColumns() * backgroundPane.getPrefRows()); i++) {
            backgroundPane.getChildren().add(new ImageView(image));
        }
    }
}
