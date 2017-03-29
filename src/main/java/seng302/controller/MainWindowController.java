package seng302.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.Observable;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;
import javafx.util.Callback;
import javafx.util.Duration;
import seng302.*;
import seng302.display.FPSReporter;
import seng302.display.RaceClock;
import seng302.display.RaceRenderer;
import seng302.model.Boat;
import seng302.model.Race;

import java.util.HashMap;


/**
 * Created by dhl25 on 15/03/17.
 */
public class MainWindowController {
    @FXML
    private Group group;
    @FXML
    private Label timerLabel;
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
    private Polygon arrow;

    private Race race;
    private RaceLoop raceLoop;
    private RaceRenderer raceRenderer;
    private RaceClock raceClock;


    @FXML
    @SuppressWarnings("unused")
    public void initialize() {

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

    @FXML
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

    public void startRace() {
        raceLoop.start();
        raceClock.start();
    }

    public void startRace(long secondsDelay) {
        final double KMPH_TO_MPS = 1000.0 / 3600.0;
        double timeScaleFactor = race.getCourse().getCourseDistance()
                / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / race.getDuration();
        secondsDelay /= (double) timeScaleFactor;
        raceClock.start();
        Timeline showLive = new Timeline(new KeyFrame(
                Duration.seconds(secondsDelay),
                event -> {
                    raceClock = new RaceClock(timerLabel, race, 0d);
                    raceLoop.start();
                }));
        showLive.setCycleCount(1);
        showLive.play();
    }

    public void setRace(Race race) {
        this.race = race;
        arrow.setRotate(race.getCourse().getWindDirection());
        raceRenderer = new RaceRenderer(race, group, raceViewAnchorPane);
        raceRenderer.renderBoats(true, 0);
//            raceClock = new RaceClock(timerLabel, race, race.getCourse().getCourseDistance() / (race.getStartingList().get(0).getSpeed() / 3.6) / race.getDuration());
        raceClock = new RaceClock(timerLabel, race, -Race.PREP_TIME_SECONDS);
        raceLoop = new RaceLoop(race, raceRenderer, new FPSReporter(fpsLabel), raceViewAnchorPane);

        raceViewAnchorPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
            raceRenderer.renderCourse();
            raceRenderer.renderBoats(false, 0);
            raceRenderer.reDrawTrail(race.getStartingList());
        });
        raceViewAnchorPane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> {
            raceRenderer.renderCourse();
            raceRenderer.renderBoats(false, 0);
            raceRenderer.reDrawTrail(race.getStartingList());
        });
        setUpTable();
    }
}
