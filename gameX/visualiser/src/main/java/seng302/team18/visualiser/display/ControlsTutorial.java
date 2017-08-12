package seng302.team18.visualiser.display;

import javafx.beans.InvalidationListener;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.*;

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
    private SpriteAnimation animation;
    private ImageView tickView;
    private Label pressLabel = new Label("PRESS");
    private Label actionLabel = new Label();
    private ImageView actionImage = new ImageView();
    private HBox promptBox = new HBox();
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

        actionLabel.setWrapText(true);
        actionLabel.setPadding(new Insets(5));
        pressLabel.setMinWidth(60);
        pressLabel.setWrapText(true);

        pane.getChildren().add(promptBox);
        promptBox.getChildren().addAll(pressLabel, actionImage, actionLabel);

        promptBox.getStylesheets().addAll(ControlsTutorial.class.getResource("/stylesheets/tutorial.css").toExternalForm());
        promptBox.getStyleClass().add("hbox");

        setMapping();
        registerListeners();
    }


    /**
     * Register any required listeners.
     *
     */
    private void registerListeners() {
        InvalidationListener listenerWidth = observable -> {
            draw();
        };
        promptBox.widthProperty().addListener(listenerWidth);

        InvalidationListener listenerHeight = observable -> {
            draw();
        };
        promptBox.heightProperty().addListener(listenerHeight);
    }


    /**
     * Set up a mapping from each action to an associated image.
     */
    private void setMapping() {
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
     * Draw the current element(s) of the tutorial.
     * If screen is resized, this method will also be called so the elements can be repositioned.
     */
    public void draw() {
        promptBox.setPrefWidth(pane.getWidth());
        promptBox.setMaxWidth(pane.getWidth());
        promptBox.setMinHeight(76);
        promptBox.setMaxHeight(76);
        promptBox.setPrefHeight(76);



        Image image = imageMap.get(keyList.get(currentKeyIndex));
        actionImage.setImage(image);


        actionLabel.setText(getCurrentPromptText());

        promptBox.setLayoutX(0);
        promptBox.setLayoutY(pane.getHeight() - promptBox.getHeight());
//        System.out.println("BOX HEIGHT -> " + promptBox.getHeight() + "  Ylayout:" + promptBox.getLayoutY());
//        System.out.println("  ");
    }


    /**
     * Begin an animation for a tick to let user know action is completed.
     */
    public void tickAnimation() {
        tickView = new ImageView("/images/tutorial/tick128.png");

        animation = new SpriteAnimation(
                tickView,
                Duration.millis(1500),
                COUNT, COLUMNS,
                OFFSET_X, OFFSET_Y,
                WIDTH, HEIGHT
        );
        tickView.setScaleX(1);
        tickView.setScaleY(1);
        animation.setCycleCount(1);
        animation.play();
        pane.getChildren().add(tickView);
    }


    private String getCurrentPromptText(){
        BoatControls control = keyList.get(currentKeyIndex);
        if (control == BoatControls.SAILS) {
            return "TO TOGGLE SAIL IN/OUT";
        } else if (control == BoatControls.UP) {
            return "TO STEER BOAT UPWIND";
        }else if (control == BoatControls.DOWN) {
            return "TO STEER BOAT DOWNWIND";
        }else if (control == BoatControls.TACK_GYBE) {
            return "TO PREFORM A TACK OR A GYBE";
        }else if (control == BoatControls.VMG) {
            return "TO SNAP TO THE OPTIMAL VMG";
        }
        return "Oh no";
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
