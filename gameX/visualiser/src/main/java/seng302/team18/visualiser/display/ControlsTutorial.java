package seng302.team18.visualiser.display;

import javafx.beans.InvalidationListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import seng302.team18.model.Boat;
import seng302.team18.util.VMGAngles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to handle running the controls tutorial
 */
public class ControlsTutorial {

    private Boat boat;
    private double windDirection;
    private double boatOldTWA;
    private boolean boatOldSailsOut;

    private static final int COLUMNS = 7;
    private static final int COUNT = 44;
    private static final int OFFSET_X = 0;
    private static final int OFFSET_Y = 0;
    private static final int WIDTH = 128;
    private static final int HEIGHT = 128;

    private Pane pane;
    private SpriteAnimation animation;
    private ImageView tickView;

    private HBox controlPromptBox = new HBox();
    private Label pressLabel = new Label("PRESS");
    private Label keyActionLabel = new Label();
    private ImageView actionImage = new ImageView();

    private HBox actionPromptBox = new HBox();
    private Label actionLabel = new Label();

    private Map<BoatControls, Image> imageMap = new HashMap<>();
    private int currentKeyIndex = 0;

    private VBox tutorialBox = new VBox(actionPromptBox, controlPromptBox);


    private static final int INDEX_SAILS1 = 0;
    private static final int INDEX_UP1 = 1;
    private static final int INDEX_UP2 = 2;
    private static final int INDEX_UP3 = 3;
    private static final int INDEX_DOWN1 = 4;
    private static final int INDEX_DOWN2 = 5;
    private static final int INDEX_DOWN3 = 6;
    private static final int INDEX_VMG = 7;
    private static final int INDEX_TACK = 8;
    private static final int INDEX_GYBE = 9;
    private static final int INDEX_SAILS2 = 10;
    private static final int INDEX_ESC = 11;


    private List<BoatControls> keyList = Arrays.asList(BoatControls.SAILS,
                                                    BoatControls.UP,
                                                    BoatControls.UP,
                                                    BoatControls.UP,
                                                    BoatControls.DOWN,
                                                    BoatControls.DOWN,
                                                    BoatControls.DOWN,
                                                    BoatControls.VMG,
                                                    BoatControls.TACK_GYBE,
                                                    BoatControls.TACK_GYBE,
                                                    BoatControls.SAILS,
                                                    BoatControls.ESC);


    /**
     * Begin a controls tutorial. Taking the user though the inputs to play the game.
     *
     * @param pane The pane to put graphical elements on.
     */
    public ControlsTutorial(Pane pane, double windAngle, Boat boat) { // should only be one boat in tutorial
        this.pane = pane;
        this.windDirection = windAngle;
        this.boat = boat;

        boatOldSailsOut = boat.isSailOut();
        boatOldTWA = boat.getTrueWindAngle(windAngle);

        keyActionLabel.setWrapText(true);
        keyActionLabel.setPadding(new Insets(5));
        pressLabel.setMinWidth(60);
        pressLabel.setPadding(new Insets(5));
        pressLabel.setWrapText(true);
        actionLabel.setPadding(new Insets(5));
        actionLabel.setWrapText(true);

        pane.getChildren().addAll(actionPromptBox, controlPromptBox);

        actionPromptBox.getChildren().addAll(actionLabel);
        controlPromptBox.getChildren().addAll(pressLabel, actionImage, keyActionLabel);

        controlPromptBox.setAlignment(Pos.CENTER);
        actionPromptBox.setAlignment(Pos.CENTER);


        controlPromptBox.getStylesheets().addAll(ControlsTutorial.class.getResource("/stylesheets/tutorial.css").toExternalForm());
        controlPromptBox.getStyleClass().add("hbox1");

        actionPromptBox.getStylesheets().addAll(ControlsTutorial.class.getResource("/stylesheets/tutorial.css").toExternalForm());
        actionPromptBox.getStyleClass().add("hbox2");

        controlPromptBox.setMinHeight(60);
        controlPromptBox.setMaxHeight(60);
        controlPromptBox.setPrefHeight(60);

        actionPromptBox.setMinHeight(60);
        actionPromptBox.setMaxHeight(60);
        actionPromptBox.setPrefHeight(60);

        setMapping();
        registerListeners();
    }


    /**
     * Register any required listeners.
     *
     */
    private void registerListeners() {
        InvalidationListener listenerWidth = observable -> draw();
        controlPromptBox.widthProperty().addListener(listenerWidth);
        actionPromptBox.widthProperty().addListener(listenerWidth);

        InvalidationListener listenerHeight = observable -> draw();
        controlPromptBox.heightProperty().addListener(listenerHeight);
        actionPromptBox.heightProperty().addListener(listenerHeight);
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
        imageMap.put(BoatControls.ESC, new Image(TutorialImage.ESC.toString()));

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
    }


    /**
     * Method to check if the key the user has pressed progresses them in the tutorial
     *
     * @param code the keycode the user has pressed
     * @return true if the user has progressed in the tutorial
     */
    public boolean checkIfProgressed(KeyCode code) {
        boolean result = false;
        //
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


    /**
     * Checks if the user has progressed throught the sail section
     * @param code key code given by user
     * @return true if progressed
     */
    private boolean checkSails(KeyCode code) {
        boolean result = false;

        if (currentKeyIndex == INDEX_SAILS1) {
            if (boatOldSailsOut) {
                result = (code == KeyCode.SHIFT);
            }
        } else if (currentKeyIndex == INDEX_SAILS2) {
            if (!boatOldSailsOut) {
                result = (code == KeyCode.SHIFT);
            }
        }

        boatOldSailsOut = boat.isSailOut();
        boatOldTWA = boat.getTrueWindAngle(windDirection);

        return result;
    }


    /**
     * Method to check if the user has progressed though up section of the tutorial
     *
     * @param code key code given by user
     * @return true if progressed
     */
    private boolean checkUp(KeyCode code) {
        //NOTE the odd setting and comparisons of sails out, this is because everthing is checked before the message is sent
        if (code == KeyCode.PAGE_UP || code == KeyCode.UP) {
            if (currentKeyIndex == INDEX_UP1 && boatOldTWA >= boat.getTrueWindAngle(windDirection)) {
                return true;
            }else if (currentKeyIndex == INDEX_UP2 && boatOldTWA > boat.getTrueWindAngle(windDirection) + 2.9) {
                return true;
            }else if (currentKeyIndex == INDEX_UP3 && boatOldTWA > boat.getTrueWindAngle(windDirection)  + 5.9) {
                boatOldSailsOut = boat.isSailOut();
                boatOldTWA = boat.getTrueWindAngle(windDirection);
                return true;
            }
        }
        return false;
    }


    /**
     * Method to check if the user has progressed though DOWN section of the tutorial
     *
     * @param code key code given by user
     * @return true if progressed
     */
    private boolean checkDown(KeyCode code) {
        //NOTE the odd setting and comparisons of sails out, this is because everthing is checked before the message is sent
        if (code == KeyCode.PAGE_DOWN || code == KeyCode.DOWN) {
            if (currentKeyIndex == INDEX_DOWN1 && boatOldTWA + 3 >= boat.getTrueWindAngle(windDirection)) {
                boatOldSailsOut = boat.isSailOut();
                boatOldTWA = boat.getTrueWindAngle(windDirection);
                return true;
            }else if (currentKeyIndex == INDEX_DOWN2 && boatOldTWA + 2.9 <= boat.getTrueWindAngle(windDirection)) {
                return true;
            }else if (currentKeyIndex == INDEX_DOWN3 && boatOldTWA + 5.9 <= boat.getTrueWindAngle(windDirection)) {
                boatOldSailsOut = boat.isSailOut();
                boatOldTWA = boat.getTrueWindAngle(windDirection);
                return true;
            }
        }
        return false;
    }


    /**
     * May be updated in future to check if actual tack /
     * @param code Code for the key press.
     * @return True if key code was correct for a tack/gybe
     */
    private boolean checkTackGybe(KeyCode code) {
        if (code == KeyCode.ENTER) {
            if (currentKeyIndex == INDEX_TACK) {
                return (boat.getTrueWindAngle(windDirection) <= 90);
            } else if (currentKeyIndex == INDEX_GYBE) {
                return (boat.getTrueWindAngle(windDirection) >= 90);
            }
        }
        return false;
    }


    /**
     * Method to check if user has optimised their VMG
     * @param code users key code
     * @return true if the user optimises their VMG
     */
    private boolean checkVMG(KeyCode code) {
        if (code == KeyCode.SPACE) {
            double relativeHeading = (boat.getHeading() - windDirection + 360) % 360;
            double left = VMGAngles.LEFT.getValue();
            double right = VMGAngles.RIGHT.getValue();
            double deadZone = VMGAngles.DEAD_ZONE.getValue();
            if (left - deadZone > relativeHeading && relativeHeading > right + deadZone) {
                return true;
            } else if (left + deadZone < relativeHeading || relativeHeading < right - deadZone) {
                return true;
            }
        }

        return false;
    }


    /**
     * Draw the current element(s) of the tutorial.
     * If screen is resized, this method will also be called so the elements can be repositioned.
     */
    public void draw() {
        controlPromptBox.setPrefWidth(pane.getWidth());
        controlPromptBox.setMaxWidth(pane.getWidth());


        actionPromptBox.setPrefWidth(pane.getWidth());
        actionPromptBox.setMaxWidth(pane.getWidth());

        Image image = imageMap.get(keyList.get(currentKeyIndex));
        actionImage.setImage(image);

        keyActionLabel.setText(getCurrentPromptText());
        actionLabel.setText(getActionPromptText());

        controlPromptBox.setLayoutY(actionPromptBox.getHeight());

        actionPromptBox.setLayoutY(0);
        controlPromptBox.setLayoutY(actionPromptBox.getHeight());

        actionPromptBox.setTranslateY(pane.getHeight() - (controlPromptBox.getHeight() * 2));
        controlPromptBox.setTranslateY(pane.getHeight() - (controlPromptBox.getHeight() * 2));
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


    /**
     * Gets the text to inform the user of what to do
     * @return returns the text as a string
     */
    private String getActionPromptText(){
        if (currentKeyIndex == INDEX_SAILS1) {              //Sails
            return "WELCOME TO THE TUTORIAL! BEGIN BY BRINGING YOUR SAILS IN TO POWER THE BOAT";
        } else if (currentKeyIndex == INDEX_UP1) {          //UP
            return "STEER UPWIND 9 DEGREES";
        } else if (currentKeyIndex == INDEX_UP2) {          //UP
            return "6 DEGREES TO GO";
        } else if (currentKeyIndex == INDEX_UP3) {          //UP
            return "3 DEGREES TO GO";
        } else if (currentKeyIndex == INDEX_DOWN1) {        //DOWN
            return "STEER DOWNWIND 9 DEGREES";
        } else if (currentKeyIndex == INDEX_DOWN2) {        //DOWN
            return "6 DEGREES TO GO";
        } else if (currentKeyIndex == INDEX_DOWN3) {        //DOWN
            return "3 DEGREES TO GO";
        }  else if (currentKeyIndex == INDEX_VMG) {         //VMG
            return "OPTIMISE YOUR UPWIND/DOWNWIND VMG";
        } else if (currentKeyIndex == INDEX_TACK) {         //TACK
            return "PERFORM A TACK (YOU MUST BE FACING UPWIND)";
        } else if (currentKeyIndex == INDEX_GYBE) {         //GYBE
            return "PERFORM A GYBE (YOU MUST BE FACING DOWNWIND)";
        } else if (currentKeyIndex == INDEX_SAILS2) {       //SAILS
            return "PUT THE SAILS OUT TO STOP THE BOAT";
        } else if (currentKeyIndex == INDEX_ESC) {          //ESC
            return "TUTORIAL FINISHED! YOU ARE NOW READY TO SAIL THE HIGH SEAS";
        }
        return "oh no";
    }


    /**
     * Gets the test to be displayed next to the prompt button
     * @return the text as a string
     */
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
        } else if (control == BoatControls.ESC) {
            return "TO EXIT TUTORIAL";
        }
        return "Oh no";
    }


    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }


    /**
     * Register a code for each of the controls of the boat.
     */
    public enum BoatControls {
        SAILS,
        UP,
        DOWN,
        TACK_GYBE,
        VMG,
        ESC
    }

    public void setBoat (Boat boat){
        this.boat = boat;
    }
}
