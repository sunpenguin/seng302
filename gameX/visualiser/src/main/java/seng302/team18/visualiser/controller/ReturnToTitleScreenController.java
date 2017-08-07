package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by cslaven on 6/08/17.
 */
public class ReturnToTitleScreenController {

    RaceController raceController;


    @FXML
    private GridPane gridPane;


    public void cancelButtonAction(){

        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();

    }

    public void exitRace() throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("StartupInterface.fxml"));
        Parent root = loader.load(); // throws IOException
        Stage stage = new Stage();
        stage.setTitle("RaceVision");
        Scene scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.show();
        //raceController.closeRace();
        Stage currentStage = (Stage) gridPane.getScene().getWindow();
        currentStage.close();
    }

    public RaceController getRaceController() {
        return raceController;
    }

    public void setRaceController(RaceController raceController) {
        this.raceController = raceController;
    }
}


