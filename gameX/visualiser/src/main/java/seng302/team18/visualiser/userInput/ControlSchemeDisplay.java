package seng302.team18.visualiser.userInput;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;


/**
 * Class to handle displaying an image showing the user the game's control scheme when a key is pressed.
 */
public class ControlSchemeDisplay {
    private Pane pane;
    private ImageView imageView;
    private Image image;
    private boolean controlsVisible;


    /**
     * Constructs a new ControlSchemeDisplay.
     * Loads the image for the control scheme and initializes the handler for pressing the appropriate key.
     *
     * @param pane The Pane to display the control scheme image on.
     */
    public ControlSchemeDisplay(Pane pane) {
        this.pane = pane;
        loadControlScheme();
        loadKeyHandler();
    }


    /**
     * Loads the control scheme image, fitted to the width of the raceViewPane.
     */
    private void loadControlScheme() {
        String path = "/images/controlScheme.png";
        image = new Image(path);
        imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        pane.getChildren().add(imageView);
        imageView.setVisible(false);
    }


    /**
     *  Shows the imageView holding the control scheme by adding it to the raceViewPane.
     */
    private void showControlScheme() {
        imageView.setLayoutX((pane.getWidth() / 2) - (image.getWidth() / 2));
        imageView.setVisible(true);
        controlsVisible = true;
    }


    /**
     *  Hides the imageView holding the control scheme by removing it from the raceViewPane.
     */
    private void hideControlScheme() {
        imageView.setVisible(false);
        controlsVisible = false;
    }


    /**
     * Creates a KeyEvent EventHandler for acting on input "C" from keyboard.
     * If the control scheme is visible, hide the control scheme. Otherwise, show it.
     */
    private void loadKeyHandler() {
        final EventHandler<KeyEvent> keyEventHandler =
            keyEvent -> {
                if (keyEvent.getCode() != null) {
                    if (keyEvent.getCode() == KeyCode.C) {
                        if (controlsVisible) {
                            hideControlScheme();
                        } else {
                            showControlScheme();
                        }
                    }
                }
            };

        pane.addEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
    }
}
