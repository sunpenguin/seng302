package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import seng302.team18.visualiser.display.AnnotationType;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * The controller class for the pop-up window ImportantAnnotationsPopup.fxml.
 * Uses the Session class to get data across controllers.
 */
public class ImportantAnnotationsController extends Observable {


    private Map<AnnotationType, CheckBox> checkBoxMap;

    @FXML private CheckBox boatNameCheckbox;
    @FXML private CheckBox boatSpeedCheckbox;
    @FXML private CheckBox estTimeToNextCheckbox;
    @FXML private CheckBox timeSinceLastCheckbox;

    @FXML private Button doneButton;


    @FXML
    private void doneButtonPressed() {
        Map<AnnotationType, Boolean> importantAnnotations = new HashMap<>();
        importantAnnotations.put(AnnotationType.ESTIMATED_TIME_NEXT_MARK,
                checkBoxMap.get(AnnotationType.ESTIMATED_TIME_NEXT_MARK).isSelected());

        importantAnnotations.put(AnnotationType.NAME,
                checkBoxMap.get(AnnotationType.NAME).isSelected());

        importantAnnotations.put(AnnotationType.SPEED,
                checkBoxMap.get(AnnotationType.SPEED).isSelected());

        importantAnnotations.put(AnnotationType.TIME_SINCE_LAST_MARK,
                checkBoxMap.get(AnnotationType.TIME_SINCE_LAST_MARK).isSelected());

        super.setChanged();
        super.notifyObservers(importantAnnotations);

        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        checkBoxMap = new HashMap<>();
        checkBoxMap.put(AnnotationType.ESTIMATED_TIME_NEXT_MARK, estTimeToNextCheckbox);
        checkBoxMap.put(AnnotationType.NAME, boatNameCheckbox);
        checkBoxMap.put(AnnotationType.SPEED, boatSpeedCheckbox);
        checkBoxMap.put(AnnotationType.TIME_SINCE_LAST_MARK, timeSinceLastCheckbox);
    }


    public void setImportant(Map<AnnotationType, Boolean> importantAnnotations) {
        for (Map.Entry<AnnotationType, Boolean> entry : importantAnnotations.entrySet()) {
            checkBoxMap.get(entry.getKey()).setSelected(entry.getValue());
        }
    }
}
