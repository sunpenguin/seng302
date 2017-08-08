package seng302.team18.visualiser.display;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import seng302.team18.model.Race;

import javax.swing.*;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.ComparisonChain.start;

/**
 * Class to handle running the controls tutorial
 */
public class ControlsTutorial {
    private Race race;
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
     * @param race The race.
     * @param pane The pane to put graphical elements on.
     */
    public ControlsTutorial(Race race, Pane pane) {
        this.race = race;
        this.pane = pane;

        start();
    }

    public boolean checkIfProgressed(KeyCode code){
        return true;
    }

    public void displayNext() {
        System.out.println("displaying :" + keyList.get(currentKeyIndex));
        currentKeyIndex = (currentKeyIndex + 1) % keyList.size();
    }

    public enum BoatControls {
        SAILS,
        UP,
        DOWN,
        TACK_GYBE,
        VMG;
    }
}
