package seng302.team18.visualiser.controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import seng302.team18.messageparsing.SocketMessageReceiver;
import seng302.team18.model.*;
import seng302.team18.util.GPSCalculations;
import seng302.team18.visualiser.display.*;
import seng302.team18.visualiser.messageinterpreting.MessageInterpreter;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.ArrayList;
import java.util.List;


/**
 * The Controller class for the Main Window view.
 */
public class MainWindowController {
    @FXML private Group group;
    @FXML private Label timerLabel;
    @FXML private ToggleButton fpsToggler;
    @FXML private Label fpsLabel;
    @FXML private TableView tableView;
    @FXML private TableColumn<Boat, Integer> boatPositionColumn;
    @FXML private TableColumn<Boat, String> boatNameColumn;
    @FXML private TableColumn<Boat, Double> boatSpeedColumn;
    @FXML private Pane raceViewPane;
    @FXML private Polygon arrow;
    @FXML private WebView map;

    private Boolean onImportant;
    private Boolean boatNameImportant;
    private Boolean boatSpeedImportant;
    private Boolean estimatedTimeImportant;
    private Boolean timeSinceLastMarkImportant;

    private Race race;
    private RaceLoop raceLoop;
    private RaceRenderer raceRenderer;
    private CourseRenderer courseRenderer;
    private BackgroundRenderer backgroundRenderer;
    private RaceClock raceClock;
    private WindDirection windDirection;
    private PixelMapper pixelMapper;
    private Stage stage;

    @FXML
    public void initialize() {
        onImportant = true;
        boatNameImportant = true;
        boatSpeedImportant = false;
        estimatedTimeImportant = false;
        timeSinceLastMarkImportant = false;
        group.setManaged(false);
        map.setVisible(true);
    }


    @FXML
    private void zoomOutButtonAction() {
        pixelMapper.setViewPortCenter(race.getCourse().getCentralCoordinate());
        pixelMapper.setZoomLevel(0);
    }


    @FXML void closeAppAction(){
        stage.close();
    }


    @FXML
    public void toggleFPS() {
        fpsLabel.setVisible(!fpsToggler.isSelected());
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
    public void setImportantAnnotationLevel() {
        onImportant = true;
        raceRenderer.setVisibleAnnotations(AnnotationType.NAME, boatNameImportant);
        raceRenderer.setVisibleAnnotations(AnnotationType.SPEED, boatSpeedImportant);
        raceRenderer.setVisibleAnnotations(AnnotationType.ESTIMATED_TIME_NEXT_MARK, estimatedTimeImportant);
        raceRenderer.setVisibleAnnotations(AnnotationType.TIME_SINCE_LAST_MARK, timeSinceLastMarkImportant);
    }


    public void toggleBoatName() {
        boatNameImportant = !boatNameImportant;
        if (onImportant) {
            setImportantAnnotationLevel();
        }
    }


    public void toggleBoatSpeed() {
        boatSpeedImportant = !boatSpeedImportant;
        if (onImportant) {
            setImportantAnnotationLevel();
        }
    }


    public void toggleEstimatedTime() {
        estimatedTimeImportant = !estimatedTimeImportant;
        if (onImportant) {
            setImportantAnnotationLevel();
        }
    }

    public void toggleTimeSinceLastMark() {
        timeSinceLastMarkImportant = !timeSinceLastMarkImportant;
        if (onImportant) {
            setImportantAnnotationLevel();
        }
    }


    /**
     * Sets the cell values for the race table, these are place, boat name and boat speed.
     */
    private void setUpTable() {
        Callback<Boat, Observable[]> callback = (Boat boat) -> new Observable[]{
                boat.placeProperty(),
        };
        ObservableList<Boat> observableList = FXCollections.observableArrayList(callback);
        observableList.addAll(race.getStartingList());

        SortedList<Boat> sortedList = new SortedList<>(observableList,
                (Boat boat1, Boat boat2) -> {
                    if (boat1.getPlace() < boat2.getPlace()) {
                        return -1;
                    } else if (boat1.getPlace() > boat2.getPlace()) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
        tableView.setItems(sortedList);
        boatPositionColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        boatNameColumn.setCellValueFactory(new PropertyValueFactory<>("boatName"));
        boatSpeedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        boatSpeedColumn.setCellFactory(col -> new TableCell<Boat, Double>() {
            @Override
            public void updateItem(Double speed, boolean empty) {
                super.updateItem(speed, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.3f", speed));
                }
            }
        });
        tableView.getColumns().setAll(boatPositionColumn, boatNameColumn, boatSpeedColumn);
    }

    /**
     * sets teh wind direction of the app using the wind direction given
     */
    private void startWindDirection() {
        windDirection = new WindDirection(race, arrow, race.getCourse().getWindDirection());
        windDirection.start();
    }

    /**
     * initialises race variables and begins the race loop. Adds listeners to the race view to listen for when the window
     * has been re-sized.
     *
     * @param race        The race which is going to be displayed.
     * @param interpreter A message interpreter.
     * @param receiver    A socket message receiver.
     */
    public void setUp(Race race, MessageInterpreter interpreter, SocketMessageReceiver receiver) {
        this.race = race;
        setCourseCenter(race.getCourse());

        pixelMapper = new PixelMapper(race.getCourse(), raceViewPane, map.getEngine());
        backgroundRenderer = new BackgroundRenderer(pixelMapper, race, map.getEngine());
        raceRenderer = new RaceRenderer(pixelMapper, race, group, raceViewPane);
        raceRenderer.renderBoats();
        courseRenderer =  new CourseRenderer(pixelMapper, race.getCourse(), group, raceViewPane);

        raceClock = new RaceClock(timerLabel);
        raceClock.start();

        raceLoop = new RaceLoop(raceRenderer, courseRenderer, new FPSReporter(fpsLabel), backgroundRenderer);
        startWindDirection();

        for (Boat boat : race.getStartingList()) {
            boat.setPlace(race.getStartingList().size());
        }

        raceLoop.start();

        raceViewPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> redrawFeatures());
        raceViewPane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> redrawFeatures());
        pixelMapper.zoomLevelProperty().addListener((observable, oldValue, newValue) -> redrawFeatures());
        pixelMapper.addViewCenterListener(propertyChangeEvent -> redrawFeatures());

        // These listeners fire whenever the northern or southern bounds of the map change.
        backgroundRenderer.northProperty().addListener((observableValue, oldWidth, newWidth) -> redrawFeatures());
        backgroundRenderer.southProperty().addListener((observableValue, oldWidth, newWidth) -> redrawFeatures());

        setUpTable();
    }

    /**
     * To call when course features need redrawing.
     * (For example, when zooming in, the course features are required to change)
     */
    public void redrawFeatures() {
        backgroundRenderer.renderBackground();
        courseRenderer.renderCourse();
        raceRenderer.renderBoats();
        raceRenderer.reDrawTrails();
    }

    public RaceClock getRaceClock() {
        return raceClock;
    }

    private void setCourseCenter(Course course) {
        List<Coordinate> points = new ArrayList<>();
        for (BoundaryMark boundaryMark : course.getBoundaries()) {
            points.add(boundaryMark.getCoordinate());
        }
        for (CompoundMark compoundMark : course.getCompoundMarks()) {
            for (Mark mark : compoundMark.getMarks()) {
                points.add(mark.getCoordinate());
            }
        }

        List<Coordinate> extremes = GPSCalculations.findMinMaxPoints(race.getCourse());
        race.getCourse().setCentralCoordinate(GPSCalculations.midPoint(extremes.get(0), extremes.get(1)));
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
