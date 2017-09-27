package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import seng302.team18.visualiser.sound.AudioPlayer;
import seng302.team18.visualiser.sound.SoundEffect;

public class HelpMenuController {

    @FXML
    private ToggleButton controlsToggle;
    @FXML
    private ToggleButton modesToggle;
    @FXML
    private Pane controlsPane;
    @FXML
    private Pane modePane;
    @FXML
    private Pane outerPane;
    @FXML
    private Label closeLabel;

    private Pane titleScreenPane;
    private AudioPlayer audioPlayer;


    /**
     * Show the keyboard controls help tab
     */
    private void showControlsHelp() {
        switchTab(modesToggle, modePane, controlsToggle, controlsPane);
    }


    /**
     * Show the game mode help tab
     */
    private void showGameModeHelp() {
        switchTab(controlsToggle, controlsPane, modesToggle, modePane);
    }


    /**
     * Switch the displayed tab from the previous to the selected tab
     *
     * @param unselected ToggleButton that has been unselected
     * @param previous   previous pane
     * @param selected   ToggleButton that has been selected
     * @param next       next pane (pane to be shown)
     */
    private void switchTab(ToggleButton unselected, Pane previous, ToggleButton selected, Pane next) {
        unselected.setSelected(false);
        unselected.setDisable(false);
        selected.setSelected(true);
        selected.setDisable(true);
        selected.setOpacity(1.0);

        previous.setVisible(false);
        next.setVisible(true);
    }


    /**
     * Close the help menu by removing the node from the title screen
     */
    private void close() {
        titleScreenPane.getChildren().remove(outerPane);
    }


    /**
     * @param pane   encasing pane
     * @param player manages the audio playback from this scene
     */
    public void setup(Pane pane, AudioPlayer player) {
        this.audioPlayer = player;
        titleScreenPane = pane;

        controlsToggle.getStyleClass().add("controlsImage");
        controlsToggle.setOnMouseClicked(event -> {
            showControlsHelp();
            buttonClickedAction();
        });
        controlsToggle.setOnMouseEntered(event -> buttonEnteredAction());

        modesToggle.getStyleClass().add("gameModeImage");
        modesToggle.setOnMouseClicked(event -> {
            showGameModeHelp();
            buttonClickedAction();
        });
        modesToggle.setOnMouseEntered(event -> buttonEnteredAction());

        closeLabel.setOnMouseClicked(event -> {
            close();
            buttonClickedAction();
        });
        closeLabel.setOnMouseEntered(event -> buttonEnteredAction());

        showControlsHelp();
    }


    /**
     * Common actions for OnMouseEntered events of menu buttons.
     * <p>
     * Plays sound effect defined by {@link SoundEffect#BUTTON_MOUSE_ENTER SoundEffect#BUTTON_MOUSE_ENTER}
     */
    private void buttonEnteredAction() {
        audioPlayer.playEffect(SoundEffect.BUTTON_MOUSE_ENTER);
    }


    /**
     * Common actions for OnMouseClicked events of menu buttons.
     * <p>
     * Plays sound effect defined by {@link SoundEffect#BUTTON_MOUSE_CLICK SoundEffect#BUTTON_MOUSE_CLICK}
     */
    private void buttonClickedAction() {
        audioPlayer.playEffect(SoundEffect.BUTTON_MOUSE_CLICK);
    }
}
