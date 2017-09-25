package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class HelpMenuController {

    @FXML private ToggleButton controlsToggle;
    @FXML private ToggleButton modesToggle;
    @FXML private Pane controlsPane;
    @FXML private Pane modePane;
    @FXML private Pane outerPane;
    @FXML private Label closeLabel;

    private Pane titleScreenPane;


    @FXML public void initialize() {
        controlsToggle.getStyleClass().add("controlsImage");
        controlsToggle.setOnMouseClicked(event -> showControlsHelp());

        modesToggle.getStyleClass().add("gameModeImage");
        modesToggle.setOnMouseClicked(event -> showGameModeHelp());

        closeLabel.setOnMouseClicked(event -> close());

        showControlsHelp();
    }


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
     * @param previous previous pane
     * @param selected ToggleButton that has been selected
     * @param next next pane (pane to be shown)
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


    public void setup(Pane pane) {
        titleScreenPane = pane;
    }
}
