package seng302.team18.visualiser.controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng302.team18.message.BoatActionMessage;

import java.io.IOException;

/**
 * Controller for the menu that is brought up from the race view.
 */
public class EscapeMenuController {

    RaceController raceController;
    Group group;

    @FXML private Pane pane;


    @FXML public void initialize() {}


    @FXML private void returnAction() {
        returnToTitle();
    }


    public void returnToTitle() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("StartupInterface.fxml"));

        try {
            Parent root = loader.load();
            Stage stage = (Stage) group.getScene().getWindow();
            Scene scene = new Scene(root, 600, 600);
            stage.setMaxHeight(625);
            stage.setMaxWidth(600);
            stage.setResizable(false);
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


