package seng302.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.xml.sax.SAXException;
import seng302.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;


/**
 * Created by dhl25 on 15/03/17.
 */
public class MainWindowController {
    @FXML
    private Group group;
    @FXML
    private Label timerLabel;
    @FXML
    private ToggleButton playPauseToggleButton;
    @FXML
    private ToggleButton fpsToggler;
    @FXML
    private Label fpsLabel;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<Boat, Integer> boatPositionColumn;
    @FXML
    private TableColumn<Boat, String> boatNameColumn;
    @FXML
    private TableColumn<Boat, Integer> boatSpeedColumn;
    @FXML
    private AnchorPane raceViewAnchorPane;
    @FXML
    private TextField scaleTextField;

    private Race race;
    private RaceLoop raceLoop;
    private RaceRenderer raceRenderer;
    private RaceClock raceClock;

    @FXML
    private Polygon arrow;

    @FXML
    @SuppressWarnings("unused")
    public void initialize() {
        try {
            Course course = XMLParser.parseCourse(new File("src/main/resources/course.xml"));
            ArrayList<Boat> boats = XMLParser.parseBoats(new File("src/main/resources/boats.xml"));

            race = new Race(boats, course);
            raceRenderer = new RaceRenderer(race, group, raceViewAnchorPane);
            raceRenderer.renderBoats(true);
            raceClock = new RaceClock(timerLabel, race, race.getCourse().getCourseDistance() / (race.getStartingList().get(0).getSpeed() / 3.6) / race.getDuration());
            raceLoop = new RaceLoop(race, raceRenderer, new FPSReporter(fpsLabel), raceViewAnchorPane);
            arrow.setRotate(course.getWindDirection());


            raceViewAnchorPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
                raceRenderer.renderCourse();
                raceRenderer.renderBoats(false);
            });
            raceViewAnchorPane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> {
                raceRenderer.renderCourse();
                raceRenderer.renderBoats(false);
            });

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        setUpTable();
    }

    @FXML
    public void showLiveRaceView(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PreRace.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PreRace.fxml"));
        try {
            Parent root = loader.load(); // throws IOException
            PreRaceController preRaceController = loader.getController();
            preRaceController.setBoats(race.getStartingList());
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void scaleButtonHandle(){
        try {
            final double MINUTES_TO_SECONDS = 60d;
            double timeInSeconds = Integer.valueOf(scaleTextField.getText()) * MINUTES_TO_SECONDS;
            raceClock.setTimeScaleFactor(race.getCourse().getCourseDistance() / (race.getStartingList().get(0).getSpeed() / 3.6) / timeInSeconds);
            race.setDuration(timeInSeconds);
        } catch (NumberFormatException e1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("You have input a value which is not valid");
            alert.setContentText("Try an integer");
            alert.showAndWait();
        }
    }

    @FXML
    public void playPauseRace() {
        if (playPauseToggleButton.isSelected()) {
            raceClock.start();
            raceLoop.start();
        } else {
            raceClock.stop();
            raceLoop.stop();
        }
    }


    @FXML
    public void toggleFPS() {
        fpsLabel.setVisible(!fpsToggler.isSelected());
    }


    @FXML
    public void setFullAnnotationLevel() {
        HashMap<String, Boolean> visibleAnnotations = raceRenderer.getVisibleAnnotations();
        visibleAnnotations.put("Speed", true);
        visibleAnnotations.put("Name", true);

        raceRenderer.setVisibleAnnotations(visibleAnnotations);
    }


    @FXML
    public void setNoneAnnotationLevel() {
        HashMap<String, Boolean> visibleAnnotations = raceRenderer.getVisibleAnnotations();
        visibleAnnotations.put("Speed", false);
        visibleAnnotations.put("Name", false);

        raceRenderer.setVisibleAnnotations(visibleAnnotations);
    }


    public void setImportantAnnotationLevel() {
        HashMap<String, Boolean> visibleAnnotations = raceRenderer.getVisibleAnnotations();
        visibleAnnotations.put("Speed", false);
        visibleAnnotations.put("Name", true);

        raceRenderer.setVisibleAnnotations(visibleAnnotations);
    }


    private void setUpTable() {
        Callback<Boat, Observable[]> callback =(Boat boat) -> new Observable[]{
                boat.placeProperty(),
        };
        ObservableList<Boat> observableList = FXCollections.observableArrayList(callback);
        observableList.addAll(race.getStartingList());

        SortedList<Boat> sortedList = new SortedList<>(observableList,
                (Boat boat1, Boat boat2) -> {
                    if( boat1.getPlace() < boat2.getPlace() ) {
                        return -1;
                    } else if( boat1.getPlace() > boat2.getPlace() ) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
        tableView.setItems(sortedList);
        boatPositionColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        boatNameColumn.setCellValueFactory(new PropertyValueFactory<>("boatName"));
        boatSpeedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        tableView.getColumns().setAll(boatPositionColumn, boatNameColumn, boatSpeedColumn);
    }
}
