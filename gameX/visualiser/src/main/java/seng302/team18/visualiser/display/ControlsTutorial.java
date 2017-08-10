package seng302.team18.visualiser.display;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sun.security.krb5.internal.KdcErrException;

import java.util.Arrays;
import java.util.List;

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

    private ImageView tickView;



    private Pane pane;
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
    }



    public void displayNext() {
        if (currentKeyIndex < keyList.size()) {
            System.out.println("displaying :" + keyList.get(currentKeyIndex));
        } else {
            finishTutorial();
        }
    }


    public void finishTutorial(){
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
     * @param code
     * @return
     */
    private boolean checkTackGybe(KeyCode code) {
        return code == KeyCode.ENTER;
    }


    private boolean checkVMG(KeyCode code) {
        return code == KeyCode.SPACE;
    }


    public enum BoatControls {
        SAILS,
        UP,
        DOWN,
        TACK_GYBE,
        VMG
    }




    /**
     * Begin an animation for a tick to let user know action is completed.
     */
    public void tickAnimation() {
        tickView = new ImageView("/images/tick128.png");
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
     * If screen is resized, this method will be called so the elements can be repositioned.
     */
    public void draw() {
//        // TODO jth102 09/08: Draw the current elements for the tutorial.
//        tickView.setLayoutX(pane.getWidth() / 2);
//        tickView.setLayoutY(pane.getHeight() / 2);
    }

}
