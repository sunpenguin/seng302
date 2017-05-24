package seng302.team18.visualiser.controller;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import seng302.team18.model.*;
import seng302.team18.util.GPSCalculations;
import seng302.team18.visualiser.display.*;
import seng302.team18.visualiser.util.PixelMapper;
import seng302.team18.visualiser.util.SparklineDataGetter;
import seng302.team18.visualiser.util.SparklineDataPoint;

import java.io.IOException;
import java.util.*;


/**
 * The controller class for the Main Window.
 * The main window consists of the right hand pane with various displays and the race on the left.
 */
public class MainWindowController implements Observer {
    @FXML private Group group;
    @FXML private Label timerLabel;
    @FXML private Label fpsLabel;
    @FXML private TableView<Boat> tableView;
    @FXML private TableColumn<Boat, Integer> boatPositionColumn;
    @FXML private TableColumn<Boat, String> boatNameColumn;
    @FXML private TableColumn<Boat, Double> boatSpeedColumn;
    @FXML private TableColumn<Boat, String> boatColorColumn;
    @FXML private Pane raceViewPane;
    @FXML private Polygon arrow;
    @FXML private CategoryAxis yPositionsAxis;
    @FXML private LineChart<String, String> sparklinesChart;
    @FXML private ImageView imageViewMap;
    @FXML private Menu raceMenu;
    @FXML private MenuBar menuBar;
    @FXML private Button setAnnotations;
    @FXML private Button toggle;
    @FXML private Slider slider;

    private boolean fpsOn;
    private boolean onImportant;

    private Race race;
    private RaceLoop raceLoop;
    private RaceRenderer raceRenderer;
    private CourseRenderer courseRenderer;
    private BackgroundRenderer backgroundRenderer;
    private RaceClock raceClock;
    private WindDirection windDirection;
    private static RerenderMain rerenderMain;
    private PixelMapper pixelMapper;

    private Map<AnnotationType, Boolean> importantAnnotations;


    @FXML
    public void initialize() {
        setSlider();
        sliderSetup();
        fpsOn = true;
        importantAnnotations = new HashMap<>();
        for (AnnotationType type : AnnotationType.values()) {
            importantAnnotations.put(type, false);
        }
        group.setManaged(false);
    }

    private void setMenu(){
        menuBar.setCache(true);
        menuBar.setCacheShape(true);
        menuBar.setCacheHint(CacheHint.SPEED);
    }

    @FXML
    private void zoomOutButtonAction() {
        pixelMapper.setViewPortCenter(race.getCourse().getCentralCoordinate());
        pixelMapper.setZoomLevel(0);
    }


///**
//     * Loads an icon as an image, sets its size to 18x18 pixels then applies it to the menu
//     */    private void loadIcon(){
//        ImageView icon = new ImageView("/images/boat-310164_640.png");
//        icon.setFitHeight(18);
//        icon.setFitWidth(18);
//        raceMenu.setGraphic(icon);
//    }

    /**
     * initialises the sparkline graph.
     */
    private void setUpSparklinesCategory(Map<Boat, Color> boatColors) {
        List<String> list = new ArrayList<>();
        for (int i = race.getStartingList().size(); i > 0; i--) {
            list.add(String.valueOf(i));
        }
        ObservableList<String> observableList = FXCollections.observableList(list);
        yPositionsAxis.setCategories(observableList);
        Queue<SparklineDataPoint> dataQueue = new LinkedList<>();
        SparklineDataGetter dataGetter = new SparklineDataGetter(dataQueue, race);
        dataGetter.listenToBoat();

        DisplaySparkline displaySparkline = new DisplaySparkline(dataQueue, boatColors, sparklinesChart);
        displaySparkline.start();

    }

    /**
     * Toggles the fps by setting label to be visible / invisible.
     */
    @FXML
    public void toggleFPS() {
        fpsOn = !fpsOn;
        fpsLabel.setVisible(fpsOn);
    }



    private void sliderSetup(){
        TextArea t = new TextArea();
        t.setText("poop");
        IntegerProperty sliderValue = new SimpleIntegerProperty(0);
        slider.setSnapToTicks(true);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(0.5f);
        slider.setBlockIncrement(0.5f);
        t.textProperty().bind(sliderValue.asString());
        slider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                if (n == 0d) return "None";
                if (n == 0.5d) return "Important";
                if (n == 1d) return "Full";
                return "Important";
            }

            @Override
            public Double fromString(String s) {
                switch (s) {
                    case "Novice":
                        return 0d;
                    case "Intermediate":
                        return 1d;
                    case "Advanced":
                        return 2d;
                    case "Expert":
                        return 3d;

                    default:
                        return 3d;
                }
            }
        });
    }
    private void setSlider() {
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            }
        });
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                if(new_val.doubleValue() == 0d){
                    setNoneAnnotationLevel();
                }
                if(new_val.doubleValue() == 0.5d){
                    setToImportantAnnotationLevel();
                }
                if(new_val.doubleValue() == 1d){
                    setFullAnnotationLevel();
                }
            }
        });
    }


    /**
     * Sets the annotation level to be full (all annotations showing)
     */
    @FXML
    public void setFullAnnotationLevel() {
        onImportant = false;

        for (AnnotationType type : AnnotationType.values()) {
            raceRenderer.setVisibleAnnotations(type, true);
        }
    }


    /**
     * Sets the annotation level to be none (no annotations showing)
     */
    @FXML
    public void setNoneAnnotationLevel() {
        onImportant = false;

        for (AnnotationType type : AnnotationType.values()) {
            raceRenderer.setVisibleAnnotations(type, false);
        }
    }


    /**
     * Sets the annotation level to be important (user selects annotations showing)
     */
    @FXML
    public void setToImportantAnnotationLevel() {
        onImportant = true;
        for (Map.Entry<AnnotationType, Boolean> importantAnnotation : importantAnnotations.entrySet()) {
            raceRenderer.setVisibleAnnotations(importantAnnotation.getKey(), importantAnnotation.getValue());
        }
    }


    /**
     * Brings up a pop-up window, showing all possible annotation options that the user can toggle on and off.
     * Only shows when the annotation level is on important.
     */
    @FXML
    public void openAnnotationsWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ImportantAnnotationsPopup.fxml"));
        Scene newScene;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            // TODO: pop up maybe
            return;
        }
        ImportantAnnotationsController controller = loader.getController();
        controller.addObserver(this);
        controller.setImportant(importantAnnotations);

        Stage inputStage = new Stage();
        inputStage.setScene(newScene);
        inputStage.showAndWait();

    }


    /**
     * Sets the cell values for the race table, these are place, boat name and boat speed.
     */
    private void setUpTable(Map<Boat, Color> boatColors) {
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
        boatNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        boatSpeedColumn.setCellValueFactory(new PropertyValueFactory<>("knotsSpeed"));
        boatSpeedColumn.setCellFactory(col -> new TableCell<Boat, Double>() {
            @Override
            public void updateItem(Double speed, boolean empty) {
                super.updateItem(speed, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", speed));
                }
            }
        });
        boatColorColumn.setCellFactory(column -> new TableCell<Boat, String>() {
            private final Map<Boat, Color> colors = boatColors;

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem("", empty);

                Boat boat = (Boat) getTableRow().getItem();
                if (boat != null) {
                    Color color = colors.get(boat);
                    if (color != null) {
                        setStyle(String.format("-fx-background-color: #%02x%02x%02x", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255)));
                    }
                }
            }
        });
        boatPositionColumn.setSortable(false);
        boatNameColumn.setSortable(false);
        boatSpeedColumn.setSortable(false);
        boatColorColumn.setSortable(false);
        tableView.getColumns().setAll(boatPositionColumn, boatColorColumn, boatNameColumn, boatSpeedColumn);

    }

    /**
     * retrieves the wind direction, scales the size of the arrow and then draws it on the Group
     */
    private void startWindDirection() {
        arrow.setScaleX(0.4);
        windDirection = new WindDirection(race, arrow, race.getCourse().getWindDirection());
        windDirection.start();
    }


    /**
     * initialises race variables and begins the race loop. Adds listeners to the race view to listen for when the window
     * has been re-sized.
     *
     * @param race The race which is going to be displayed.
     */
    public void setUp(Race race) {
        this.race = race;
        setCourseCenter(race.getCourse());

        pixelMapper = new PixelMapper(race.getCourse(), raceViewPane);
        raceRenderer = new RaceRenderer(pixelMapper, race, group);
        raceRenderer.renderBoats();
        courseRenderer = new CourseRenderer(pixelMapper, race.getCourse(), group, raceViewPane);
//        backgroundRenderer = new BackgroundRenderer(group, race.getCourse(), imageViewMap);
//        try {
//            backgroundRenderer.renderBackground();
//        } catch (IOException e) {
//            // TODO make pop up maybe or just handle it
//            backgroundRenderer.hideMap();
//            e.printStackTrace();
//        }

        raceClock = new RaceClock(timerLabel);
        raceClock.start();
//        Map<Boat, Color> boatColors = raceRenderer.boatColors()
//                .entrySet()
//                .stream()
//                .map((name, color) -> race.getStartingList().stream().filter(boat -> true).count() > 0)
//                .collect(Collectors::toMap);
        setUpSparklinesCategory(raceRenderer.boatColors());
        raceLoop = new RaceLoop(raceRenderer, courseRenderer, new FPSReporter(fpsLabel));
        startWindDirection();

        for (Boat boat : race.getStartingList()) {
            boat.setPlace(race.getStartingList().size());
        }

        raceLoop.start();

        rerenderMain = new RerenderMain(courseRenderer, raceRenderer, race);

        raceViewPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> rerenderMain.redrawFeatures());
        raceViewPane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> rerenderMain.redrawFeatures());

        pixelMapper.zoomLevelProperty().addListener((observable, oldValue, newValue) -> rerenderMain.redrawFeatures());
        pixelMapper.addViewCenterListener(propertyChangeEvent -> rerenderMain.redrawFeatures());

        setUpTable(raceRenderer.boatColors());


        setToImportantAnnotationLevel();
    }

    public RaceClock getRaceClock() {
        return raceClock;
    }


    /**
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(java.util.Observable o, Object arg) {
        if (arg instanceof Map) {
            Map annotations = (Map) arg;
            for (AnnotationType type : AnnotationType.values()) {
                if (annotations.containsKey(type)) {
                    Object on = annotations.get(type);
                    if (on instanceof Boolean) {
                        importantAnnotations.put(type, (Boolean) on);
                    }
                }
            }
            if (onImportant) {
                setToImportantAnnotationLevel();
            }
        }
    }

    /**
     * Extracts the central course coordinate from the course
     * @param course The course for which the midpoint is calculated
     */
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

}
