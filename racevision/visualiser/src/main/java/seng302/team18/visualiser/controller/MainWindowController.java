package seng302.team18.visualiser.controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Callback;
import jdk.nashorn.internal.codegen.CompilerConstants;
import seng302.team18.visualiser.messageinterpreting.MessageInterpreter;
import seng302.team18.messageparsing.SocketMessageReceiver;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.visualiser.RaceLoop;
import seng302.team18.visualiser.display.*;
import seng302.team18.visualiser.util.Session;
import seng302.team18.visualiser.util.SparklineDataQueue;
import seng302.team18.visualiser.util.SparklineDataGetter;
import seng302.team18.visualiser.util.SparklineDataQueue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * The controller class for the Main Window.
 * The main window consists of the right hand pane with various displays and the race on the left.
 */
public class MainWindowController {
    @FXML private Group group;
    @FXML private Label timerLabel;
    @FXML private Label fpsLabel;
    @FXML private TableView tableView;
    @FXML private TableColumn<Boat, Integer> boatPositionColumn;
    @FXML private TableColumn<Boat, String> boatNameColumn;
    @FXML private TableColumn<Boat, Double> boatSpeedColumn;
    @FXML private Pane raceViewPane;
    @FXML private Polygon arrow;
    @FXML private CategoryAxis yPositionsAxis;
    @FXML private LineChart<? ,?> sparklinesChart;

    private boolean fpsToggle;
    private boolean onImportant;

    private Race race;
    private RaceLoop raceLoop;
    private RaceRenderer raceRenderer;
    private CourseRenderer courseRenderer;
    private RaceClock raceClock;
    private WindDirection windDirection;


    @FXML
    public void initialize() {
        sparklinesChart.setAlternativeRowFillVisible(false);
        fpsToggle = true;
        Session.getInstance().setBoatNameImportant(true);
        Session.getInstance().setBoatSpeedImportant(false);
        Session.getInstance().setEstimatedTimeImportant(false);
        Session.getInstance().setTimeSinceLastMarkImportant(false);
    }

    private void setUpSparklinesCategory() {
        SparklineDataQueue dataQueue = new SparklineDataQueue();
        SparklineDataGetter dataGetter = new SparklineDataGetter(race.getStartingList(), dataQueue, race.getCourse());
        dataGetter.ListenToBoat();
        DisplaySparkline displaySparkline = new DisplaySparkline(dataQueue, race.getStartingList(), sparklinesChart);
        displaySparkline.start();

    }

    @FXML
    public void toggleFPS() {
        fpsToggle = !fpsToggle;
        fpsLabel.setVisible(fpsToggle);
    }


    @FXML
    public void setFullAnnotationLevel() {
        onImportant = false;
        for (AnnotationType type : AnnotationType.values()) {
            raceRenderer.setVisibleAnnotations(type, true);
        }
    }


    @FXML
    public void setNoneAnnotationLevel() {
        onImportant = false;
        for (AnnotationType type : AnnotationType.values()) {
            raceRenderer.setVisibleAnnotations(type, false);
        }
    }


    @FXML
    public void setToImportantAnnotationLevel() {
        onImportant = true;
        raceRenderer.setVisibleAnnotations(AnnotationType.NAME, Session.getInstance().getBoatNameImportant());
        raceRenderer.setVisibleAnnotations(AnnotationType.SPEED, Session.getInstance().getBoatSpeedImportant());
        raceRenderer.setVisibleAnnotations(AnnotationType.ESTIMATED_TIME_NEXT_MARK, Session.getInstance().getEstimatedTimeImportant());
        raceRenderer.setVisibleAnnotations(AnnotationType.TIME_SINCE_LAST_MARK, Session.getInstance().getTimeSinceLastMarkImportant());
    }

    /**
     * Brings up a pop-up window, showing all possible annotation options that the user can toggle on and off.
     * Only shows when the annotation level is on important.
     */
    @FXML
    public void setImportant(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ImportantAnnotationsPopup.fxml"));
        Scene newScene;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            return;
        }
        Stage inputStage = new Stage();
        inputStage.setScene(newScene);
        inputStage.showAndWait();

        if (onImportant) {
            setToImportantAnnotationLevel();
        }
    }


    /**
     * Sets the cell values for the race table, these are place, boat name and boat speed.
     */
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
        boatSpeedColumn.setCellValueFactory(new PropertyValueFactory<>("knotsSpeed"));
        boatSpeedColumn.setCellFactory(col -> new TableCell<Boat, Double>() {
            @Override
            public void updateItem(Double speed, boolean empty) {
                super.updateItem(speed, empty) ;
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.3f", speed));
                }
            }
        });
        tableView.getColumns().setAll(boatPositionColumn, boatNameColumn, boatSpeedColumn);
    }

    private void startWindDirection() {
        arrow.setScaleX(0.4);
        windDirection = new WindDirection(race, arrow, race.getCourse().getWindDirection());
        windDirection.start();
    }


//    public void startRace(long secondsDelay) {
//        final double KMPH_TO_MPS = 1000.0 / 3600.0;
////        double timeScaleFactor = race.getCourse().getCourseDistance()
////                / (race.getStartingList().get(0).getSpeed() * KMPH_TO_MPS) / race.getDuration();
////        secondsDelay /= (double) timeScaleFactor;
//        raceClock.start();
//        Timeline showLive = new Timeline(new KeyFrame(
//                Duration.seconds(secondsDelay),
//                event -> {
//                    raceClock = new RaceClock(timerLabel, race, 0d);
//                    raceClock.start();
//                    raceLoop.start();
//                }));
//        showLive.setCycleCount(1);
//        showLive.play();
//    }


    /**
     * initialises race variables and begins the race loop. Adds listers to the race view to listen for when the window
     * has been re-sized.
     * @param race The race which is going to be displayed.
     * @param interpreter A message interpreter.
     * @param receiver A socket message receiver.
     */
    public void setUp(Race race, MessageInterpreter interpreter, SocketMessageReceiver receiver) {
        this.race = race;
        raceRenderer = new RaceRenderer(race, group, raceViewPane);
        raceRenderer.renderBoats();
        courseRenderer =  new CourseRenderer(race.getCourse(), group, raceViewPane);
        raceClock = new RaceClock(timerLabel);
        raceClock.start();
        setUpSparklinesCategory();
        raceLoop = new RaceLoop(race, raceRenderer, courseRenderer, new FPSReporter(fpsLabel), interpreter, receiver);
        startWindDirection();

        for (Boat boat : race.getStartingList()) {
            boat.setPlace(race.getStartingList().size());
        }

        raceLoop.start();

        raceViewPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
            courseRenderer.renderCourse();
            raceRenderer.renderBoats();
            raceRenderer.reDrawTrails(race.getStartingList());
        });
        raceViewPane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> {
            courseRenderer.renderCourse();
            raceRenderer.renderBoats();
            raceRenderer.reDrawTrails(race.getStartingList());
        });
        setUpTable();

        setToImportantAnnotationLevel();
    }

    public RaceClock getRaceClock() {
        return raceClock;
    }
}
