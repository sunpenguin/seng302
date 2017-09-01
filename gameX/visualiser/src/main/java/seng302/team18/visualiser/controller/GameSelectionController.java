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
        initialiseArcadeButton();
        initialiseSpyroButton();
        initialiseBumperBoatsButton();
    }


    private void initialiseRaceButton() {
        raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/titleScreen.css").toExternalForm());
        raceLabel.getStyleClass().add("raceImage");
        pane.getChildren().add(raceLabel);

        raceButtonImage = new Image("/images/RaceWhite.png");
        raceLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((600 / 2));
        raceLabel.setOnMouseClicked(event -> startBumperBoats());
    }

    private void initialiseArcadeButton() {
        raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/titleScreen.css").toExternalForm());
        raceLabel.getStyleClass().add("arcadeImage");
        pane.getChildren().add(raceLabel);

        raceButtonImage = new Image("/images/Arcade_race_White.png");
        raceLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((600 / 2) + 50);
        raceLabel.setOnMouseClicked(event -> startBumperBoats());
    }

    private void initialiseSpyroButton() {
        raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/titleScreen.css").toExternalForm());
        raceLabel.getStyleClass().add("spyroImage");
        pane.getChildren().add(raceLabel);

        raceButtonImage = new Image("/images/SpyroWhite.png");
        raceLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((600 / 2) + 100);
        raceLabel.setOnMouseClicked(event -> startBumperBoats());
    }

    private void initialiseBumperBoatsButton() {
        raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/titleScreen.css").toExternalForm());
        raceLabel.getStyleClass().add("bumperBoatsImage");
        pane.getChildren().add(raceLabel);

        raceButtonImage = new Image("/images/bumper-boatsWhite.png");
        raceLabel.setLayoutX((pane.getWidth() / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((pane.getHeight() / 2) + 150);
        raceLabel.setOnMouseClicked(event -> startBumperBoats());
    }

    private void startBumperBoats(){
        System.out.println("BumperBoats");
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
