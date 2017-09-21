package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class HelpMenuController {

    @FXML private Label gameModeLabel;
    @FXML private HBox tabHBox;
    @FXML private Label controlsLabel;
    @FXML private Pane contentPane;


    @FXML public void initialize() {
        gameModeLabel.getStyleClass().add("gameModeImage");
        controlsLabel.getStyleClass().add("controlsImage");
    }
}
