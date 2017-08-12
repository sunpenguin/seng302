package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the menu that is brought up from the race view.
 */
public class EscapeMenuController {

    private RaceController raceController;
    private Group group;
    private Label quitLabel;
    private Image quitButtonImage;

    @FXML private Pane pane;


    @FXML public void initialize() {
        initialiseQuitButton();
    }


    /**
     * Set up the button for viewing the controls
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseQuitButton() {
        quitLabel = new Label();
        quitLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/escape_menu.css").toExternalForm());
        quitLabel.getStyleClass().add("quitImage");
        pane.getChildren().add(quitLabel);

        quitButtonImage = new Image("/images/escape_menu/quit_button_image.png");
        quitLabel.setLayoutX((pane.getPrefWidth() / 2) - (Math.floorDiv((int) quitButtonImage.getWidth(), 2)));
        quitLabel.setLayoutY((pane.getPrefHeight() / 2) - (Math.floorDiv((int) quitButtonImage.getHeight(), 2)));
        quitLabel.setOnMouseClicked(event -> returnToTitle());
    }


    /**
     * Return the user to the title screen.
     */
    public void returnToTitle() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("StartupInterface.fxml"));

        try {
            Parent root = loader.load();
            Stage stage = (Stage) group.getScene().getWindow();
            Scene scene = new Scene(root, 600, 600);
            stage.setMinHeight(625);
            stage.setMinWidth(600);
            stage.setResizable(true);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {}
    }


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public RaceController getRaceController() {
        return raceController;
    }

    public void setRaceController(RaceController raceController) {
        this.raceController = raceController;
    }
}


