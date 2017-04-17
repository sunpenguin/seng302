package seng302.team18.visualiser.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.util.Callback;
import javafx.util.Duration;
import seng302.team18.data.AC35MessageParserFactory;
import seng302.team18.data.RaceMessageInterpreter;
import seng302.team18.data.SocketMessageReceiver;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.visualiser.RaceLoop;
import seng302.team18.visualiser.display.*;

import java.io.IOException;


/**
 * Created by dhl25 on 15/03/17.
 */
public class MainWindowController {
    @FXML private Group group;
    @FXML private Label timerLabel;
    @FXML private ToggleButton fpsToggler;
    @FXML private Label fpsLabel;
    @FXML private TableView tableView;
    @FXML private TableColumn<Boat, Integer> boatPositionColumn;
    @FXML private TableColumn<Boat, String> boatNameColumn;
    @FXML private TableColumn<Boat, Integer> boatSpeedColumn;
    @FXML private Pane raceViewPane;
    @FXML private Polygon arrow;

    private Race race;
    private RaceLoop raceLoop;
    private RaceRenderer raceRenderer;
    private CourseRenderer courseRenderer;
    private RaceClock raceClock;


    @FXML
    @SuppressWarnings("unused")
    public void initialize() {
        this.race = new Race();
        arrow.setRotate(race.getCourse().getWindDirection());
        raceRenderer = new RaceRenderer(race, group, raceViewPane);
        raceRenderer.renderBoats(true, 0);
        courseRenderer =  new CourseRenderer(race.getCourse(), group, raceViewPane);
//        raceClock = new RaceClock(timerLabel, race, race.getCourse().getCourseDistance() / (race.getStartingList().get(0).getSpeed() / 3.6) / race.getDuration());
        raceClock = new RaceClock(timerLabel, race, -Race.PREP_TIME_SECONDS);
        try {
            raceLoop = new RaceLoop(race, raceRenderer, new FPSReporter(fpsLabel), new RaceMessageInterpreter(race), new SocketMessageReceiver(4941, new AC35MessageParserFactory()));
            raceLoop.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        raceViewPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
            courseRenderer.renderCourse();
            raceRenderer.renderBoats(false, 0);
            raceRenderer.reDrawTrail(race.getStartingList());
        });
        raceViewPane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> {
            courseRenderer.renderCourse();
            raceRenderer.renderBoats(false, 0);
            raceRenderer.reDrawTrail(race.getStartingList());
        });
        setUpTable();
    }


    @FXML
    public void toggleFPS() {
        fpsLabel.setVisible(!fpsToggler.isSelected());
    }


    @FXML
    public void setFullAnnotationLevel() {
        for (AnnotationType type : AnnotationType.values()) {
            raceRenderer.setVisibleAnnotations(type, true);
        }
    }


    @FXML
    public void setNoneAnnotationLevel() {
        for (AnnotationType type : AnnotationType.values()) {
            raceRenderer.setVisibleAnnotations(type, false);
        }
    }


    @FXML
    public void setImportantAnnotationLevel() {
        raceRenderer.setVisibleAnnotations(AnnotationType.NAME, true);
        raceRenderer.setVisibleAnnotations(AnnotationType.SPEED, false);
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


    public void startRace(long secondsDelay) {
        final double KMPH_TO_MPS = 1000.0 / 3600.0;
//        double timeScaleFactor = race.getCourse().getCourseDistance()
//                / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / race.getDuration();
//        secondsDelay /= (double) timeScaleFactor;
        raceClock.start();
        Timeline showLive = new Timeline(new KeyFrame(
                Duration.seconds(secondsDelay),
                event -> {
                    raceClock = new RaceClock(timerLabel, race, 0d);
                    raceClock.start();
                    raceLoop.start();
                }));
        showLive.setCycleCount(1);
        showLive.play();
    }

//    public void setRace(Race race) {
//        this.race = race;
//        arrow.setRotate(race.getCourse().getWindDirection());
//        raceRenderer = new RaceRenderer(race, group, raceViewPane);
//        raceRenderer.renderBoats(true, 0);
//        courseRenderer =  new CourseRenderer(race.getCourse(), group, raceViewPane);
////            raceClock = new RaceClock(timerLabel, race, race.getCourse().getCourseDistance() / (race.getStartingList().get(0).getSpeed() / 3.6) / race.getDuration());
//        raceClock = new RaceClock(timerLabel, race, -Race.PREP_TIME_SECONDS);
//        raceLoop = new RaceLoop(race, raceRenderer, new FPSReporter(fpsLabel));
//
//        raceViewPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
//            courseRenderer.renderCourse();
//            raceRenderer.renderBoats(false, 0);
//            raceRenderer.reDrawTrail(race.getStartingList());
//        });
//        raceViewPane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> {
//            courseRenderer.renderCourse();
//            raceRenderer.renderBoats(false, 0);
//            raceRenderer.reDrawTrail(race.getStartingList());
//        });
//        setUpTable();
//    }
}
