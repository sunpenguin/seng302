package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller For the game type selection screen
 */
public class GameSelectionController {

    @FXML private Pane pane;
    private Stage stage;


    private Label raceLabel;
    private Image raceButtonImage;


    public void initialize() {
        initialiseRaceButton();
    }


    private void initialiseRaceButton() {
        raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/titleScreen.css").toExternalForm());
        raceLabel.getStyleClass().add("controlsImage");
        pane.getChildren().add(raceLabel);

        raceButtonImage = new Image("/images/title_screen/RaceWhite.png");
        raceLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((600 / 2) + 150);
        raceLabel.setOnMouseClicked(event -> toggleControlsView());
    }

    private void toggleControlsView(){
        System.out.println("hello");
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
