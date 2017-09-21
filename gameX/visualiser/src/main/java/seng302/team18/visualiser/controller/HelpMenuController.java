package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class HelpMenuController {

    @FXML private HBox tabHBox;
    @FXML private Pane contentPane;
    @FXML private ToggleButton controlsToggle;
    @FXML private ToggleButton modesToggle;


    @FXML public void initialize() {
        controlsToggle.getStyleClass().add("controlsImage");
        controlsToggle.setOnMouseClicked(event -> showControlsHelp());

        modesToggle.getStyleClass().add("gameModeImage");
        modesToggle.setOnMouseClicked(event -> showGameModeHelp());
    }


    private void showControlsHelp() {
        modesToggle.setSelected(false);
        modesToggle.setDisable(false);
        controlsToggle.setDisable(true);
        controlsToggle.setOpacity(1.0);
    }


    private void showGameModeHelp() {
        controlsToggle.setSelected(false);
        controlsToggle.setDisable(false);
        modesToggle.setDisable(true);
        modesToggle.setOpacity(1.0);
    }
}
