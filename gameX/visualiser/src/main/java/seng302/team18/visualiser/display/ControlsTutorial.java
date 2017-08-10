package seng302.team18.visualiser.display;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to handle running the controls tutorial
 */
public class ControlsTutorial {

    // TODO jth102 9/08: Make class for each animation? Store all this + frame time, repetitions, size scale
    private final int COLUMNS  =   7;
    private final int COUNT    =  44;
    private final int OFFSET_X =  0;
    private final int OFFSET_Y =  0;
    private final int WIDTH    = 128;
    private final int HEIGHT   = 128;

    private Pane pane;
    private ImageView tickView;
    private ImageView actionImage = new ImageView();
    private HBox promptBox = new HBox();
    private Label label = new Label("PRESS");
    private Map<BoatControls, Image> imageMap = new HashMap<>();
    private int currentKeyIndex = 0;
    private List<BoatControls> keyList = Arrays.asList(BoatControls.SAILS,
                                                    BoatControls.UP,
                                                    BoatControls.DOWN,
                                                    BoatControls.VMG,
                                                    BoatControls.TACK_GYBE,
                                                    BoatControls.TACK_GYBE,
                                                    BoatControls.SAILS);


    /**
     * Begin a controls tutorial. Taking the user though the inputs to play the game.
     *
     * @param pane The pane to put graphical elements on.
     */
    public ControlsTutorial(Pane pane) {
        this.pane = pane;
        pane.getChildren().add(promptBox);
        promptBox.getChildren().addAll(label, actionImage);

        // Map all of the controls to an appropriate image
        imageMap.put(BoatControls.TACK_GYBE, new Image(TutorialImage.ENTER.toString()));
        imageMap.put(BoatControls.SAILS, new Image(TutorialImage.SHIFT.toString()));
        imageMap.put(BoatControls.UP, new Image(TutorialImage.UP.toString()));
        imageMap.put(BoatControls.DOWN, new Image(TutorialImage.DOWN.toString()));
        imageMap.put(BoatControls.VMG, new Image(TutorialImage.SPACE.toString()));
    }


    /**
     * Display the prompt for the next key to press.
     * If all actions completed, finish the tutorial.
     */
    public void displayNext() {
        if (currentKeyIndex < keyList.size()) {
            draw();
        } else {
            finishTutorial();
        }
    }


    /**
     * Finish the tutorial and inform the user.
     */
    private void finishTutorial(){
        currentKeyIndex -= 1;
        System.out.println("tutorial over");
    }


    /**
     * Method to check if the key the user has pressed progresses them in the tutorial
     *
     * @param code the keycode the user has pressed
     * @return true if the user has progressed in the tutorial
     */
    public boolean checkIfProgressed(KeyCode code) {
        boolean result = false;
        if (keyList.get(currentKeyIndex) == BoatControls.SAILS) {
            result = checkSails(code);
        } else if (keyList.get(currentKeyIndex) == BoatControls.UP) {
            result = checkUp(code);
        } else if (keyList.get(currentKeyIndex) == BoatControls.DOWN) {
            result = checkDown(code);
        } else if (keyList.get(currentKeyIndex) == BoatControls.TACK_GYBE) {
            result = checkTackGybe(code);
        } else if (keyList.get(currentKeyIndex) == BoatControls.VMG) {
            result = checkVMG(code);
        }
        if (result) {
            currentKeyIndex += 1;
        }
        return result;
    }


    private boolean checkSails(KeyCode code) {
        return code == KeyCode.SHIFT;
    }


    private boolean checkUp(KeyCode code) {
        return code == KeyCode.PAGE_UP || code == KeyCode.UP;
    }


    private boolean checkDown(KeyCode code) {
        return code == KeyCode.PAGE_DOWN || code == KeyCode.DOWN;
    }


    /**
     * May be updated in future to check if actual tack /
     * @param code Code for the key press.
     * @return True if key code was correct for a tack/gybe
     */
    private boolean checkTackGybe(KeyCode code) {
        return code == KeyCode.ENTER;
    }


    private boolean checkVMG(KeyCode code) {
        return code == KeyCode.SPACE;
    }


    /**
     * Begin an animation for a tick to let user know action is completed.
     */
    public void tickAnimation() {
        tickView = new ImageView("/images/tutorial/tick128.png");
        tickView.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));

        final Animation animation = new SpriteAnimation(
                tickView,
                Duration.millis(1500),
                COUNT, COLUMNS,
                OFFSET_X, OFFSET_Y,
                WIDTH, HEIGHT
        );
        tickView.setScaleX(0.5);
        tickView.setScaleY(0.5);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();

        pane.getChildren().add(tickView);
    }


    /**
     * Draw the current element(s) of the tutorial.
     * If screen is resized, this method will also be called so the elements can be repositioned.
     */
    public void draw() {
        actionImage.setImage(imageMap.get(keyList.get(currentKeyIndex)));
        System.out.println(actionImage.getImage());
        promptBox.setLayoutX(0.0);
        promptBox.setLayoutY(pane.getHeight() - 50);
    }


    /**
     * Register a code for each of the controls of the boat.
     */
    public enum BoatControls {
        SAILS,
        UP,
        DOWN,
        TACK_GYBE,
        VMG
    }
}
