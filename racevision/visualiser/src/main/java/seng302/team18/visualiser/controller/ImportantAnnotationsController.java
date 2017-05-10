package seng302.team18.visualiser.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import seng302.team18.visualiser.util.Session;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by sbe67 on 10/05/17.
 */
public class ImportantAnnotationsController implements Initializable {

    private boolean boatNameImportant;
    private boolean boatSpeedImportant;
    private boolean estimatedTimeImportant;
    private boolean timeSinceLastMarkImportant;

    @FXML private CheckBox checkboxBoatName;
    @FXML private CheckBox checkboxBoatSpeed;
    @FXML private CheckBox checkboxEstTimeToNext;
    @FXML private CheckBox checkboxTimeSinceLast;

    @FXML
    private Button doneButton;


    public void toggleBoatName() {
        boatNameImportant = !boatNameImportant;

    }

    public void toggleBoatSpeed() {
        boatSpeedImportant = !boatSpeedImportant;
    }

    public void toggleEstimatedTime() {
        estimatedTimeImportant = !estimatedTimeImportant;
    }

    public void toggleTimeSinceLastMark() {
        timeSinceLastMarkImportant = !timeSinceLastMarkImportant;
    }

    @FXML
    void doneButtonPressed() {
        Session.getInstance().setBoatNameImportant(boatNameImportant);
        Session.getInstance().setBoatSpeedImportant(boatSpeedImportant);
        Session.getInstance().setEstimatedTimeImportant(estimatedTimeImportant);
        Session.getInstance().setTimeSinceLastMarkImportant(timeSinceLastMarkImportant);
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //get values from session
        boatNameImportant = Session.getInstance().getBoatNameImportant();
        boatSpeedImportant = Session.getInstance().getBoatSpeedImportant();
        estimatedTimeImportant = Session.getInstance().getEstimatedTimeImportant();
        timeSinceLastMarkImportant = Session.getInstance().getTimeSinceLastMarkImportant();

        //set checkboxes tickitude
        checkboxBoatName.setSelected(boatNameImportant);
        checkboxBoatSpeed.setSelected(boatSpeedImportant);
        checkboxEstTimeToNext.setSelected(estimatedTimeImportant);
        checkboxTimeSinceLast.setSelected(timeSinceLastMarkImportant);
    }
}
