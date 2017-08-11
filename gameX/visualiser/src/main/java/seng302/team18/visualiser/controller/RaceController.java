package seng302.team18.visualiser.controller;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import seng302.team18.interpreting.CompositeMessageInterpreter;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35MessageType;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.messageparsing.Receiver;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.visualiser.display.*;
import seng302.team18.visualiser.messageinterpreting.*;
import seng302.team18.visualiser.send.Sender;
import seng302.team18.visualiser.util.PixelMapper;
import seng302.team18.visualiser.util.SparklineDataGetter;
import seng302.team18.visualiser.util.SparklineDataPoint;

import java.io.IOException;
import java.util.*;

/**
 * The controller class for the Main Window.
 * The main window consists of the right hand pane with various displays and the race on the left.
 */
public class RaceController implements Observer {
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
    @FXML private Label speedLabel;
    @FXML private CategoryAxis yPositionsAxis;
    @FXML private LineChart<String, String> sparklinesChart;
    @FXML private Slider slider;
    @FXML private Pane returnToTitleScreenPane;

    private boolean fpsOn;
    private boolean onImportant;
    private boolean sailIn = false;

    private Race race;
    private RaceLoop raceLoop;
    private RaceRenderer raceRenderer;
    private CourseRenderer courseRenderer;
    private RaceClock raceClock;
    private WindDisplay windDisplay;
    private PixelMapper pixelMapper;
    private Map<AnnotationType, Boolean> importantAnnotations;

    private Sender sender;
    private Receiver receiver;

    @FXML
    public void initialize() {
        installKeyHandler();
        setSliderListener();
        sliderSetup();
        fpsOn = true;
        importantAnnotations = new HashMap<>();
        for (AnnotationType type : AnnotationType.values()) {
            importantAnnotations.put(type, false);
        }
        group.setManaged(false);
    }

    @FXML
    private void zoomOutButtonAction() {
        pixelMapper.setViewPortCenter(race.getCourse().getCentralCoordinate());
        pixelMapper.setZoomLevel(0);
    }

    @FXML public void closeRace() {
        Stage stage = (Stage) raceViewPane.getScene().getWindow();
        stage.close();

    }

    /**
     * Loads an icon as an image, sets its size to 18x18 pixels then applies it to the menu
     */
    private void loadIcon() {
        ImageView icon = new ImageView("/images/boat-310164_640.png");
        icon.setFitHeight(18);
        icon.setFitWidth(18);
    }


    private void installKeyHandler() {
        final EventHandler<KeyEvent> keyEventHandler =
            keyEvent -> {
                if (keyEvent.getCode() != null) {
                    BoatActionMessage message = new BoatActionMessage(race.getPlayerId());
                    boolean send = true;
                    switch (keyEvent.getCode()){
                        case SPACE:
                            message.setAutopilot(true);
                            break;
                        case ENTER:
                            message.setTackGybe(true);
                            break;
                        case PAGE_UP:
                            message.setUpwind(true);
                            break;
                        case PAGE_DOWN:
                            message.setDownwind(true);
                            break;
                        case SHIFT:
                            sailIn = !sailIn;
                            message.setSailsIn(sailIn);
                            break;
                        case ESCAPE:
                            try {
                                System.out.println(returnToTitleScreenPane);
                                if (returnToTitleScreenPane == null) {
                                    returnToTitleScreen();
                                } else {
                                    group.getChildren().remove(returnToTitleScreenPane);
                                    returnToTitleScreenPane = null;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            send = false;
                            break;
                        default:
                            send = false;
                    }
                    if (send) {
                        sender.send(message);
                    }
                }
            };

        raceViewPane.addEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
        raceViewPane.setFocusTraversable(true);
        raceViewPane.requestFocus();
        raceViewPane.focusedProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> raceViewPane.requestFocus()));
    }


    /**
     * initialises the sparkline graph.
     */
    private void setUpSparklines(Map<String, Color> boatColors) {
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


    /**
     * Sets up the Slider so that it can be used to switch between the different levels of annotation
     */
    private void sliderSetup(){
        TextArea t = new TextArea();
        t.setText("Annotation Slider");
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
                return 0.0;
            }
        });
    }

    /**
     * Creates a listener so the slider knows when its value has changed and it can update the annotations accordingly
     */
    private void setSliderListener() {
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

    public void returnToTitleScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("return.fxml"));
        returnToTitleScreenPane = loader.load();
        ReturnToTitleScreenController controller = loader.getController();
        controller.setGroup(group);
        group.getChildren().add(returnToTitleScreenPane);
        returnToTitleScreenPane.toFront();
        returnToTitleScreenPane.setLayoutX((raceViewPane.getWidth() / 2) - returnToTitleScreenPane.getMinWidth() / 2);
        returnToTitleScreenPane.setLayoutY((raceViewPane.getHeight() / 2) - returnToTitleScreenPane.getMinHeight() / 2);
    }

    public void cancelButtonAction(){
        System.out.println("cancelled");
    }



    /**
     * Sets the cell values for the race table, these are place, boat name and boat speed.
     *
     * @param boatColors a map from short name to colour for the boats
     */
    private void setUpTable(Map<String, Color> boatColors) {
        Callback<Boat, Observable[]> callback = (Boat boat) -> new Observable[]{
                boat.placeProperty(), boat.speedProperty()
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
        boatSpeedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        boatSpeedColumn.setCellFactory(col -> new TableCell<Boat, Double>() {
            @Override
            public void updateItem(Double speed, boolean empty) {
                super.updateItem(speed, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.1f", speed));
                }
            }
        });

        boatColorColumn.setCellFactory(column -> new TableCell<Boat, String>() {
            private final Map<String, Color> colors = boatColors;

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem("", empty);

                Boat boat = (Boat) getTableRow().getItem();
                if (boat != null) {
                    Color color = colors.get(boat.getShortName());
                    if (color != null) {
                        setStyle(String.format("-fx-background-color: #%02x%02x%02x", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255)));
                    }
                }
            }
        });

        Collection<TableColumn<Boat, ?>> columns = new ArrayList<>();
        columns.add(boatPositionColumn);
        columns.add(boatColorColumn);
        columns.add(boatNameColumn);
        columns.add(boatSpeedColumn);

        for (TableColumn<Boat, ?> column : columns) {
            column.setResizable(false);
            column.setSortable(false);
        }

        tableView.getColumns().setAll(columns);

        // Resets the columns to the original order whenever the user tries to change them
        tableView.getColumns().addListener(new ListChangeListener<TableColumn<Boat, ?>>() {
            public boolean suspended;

            @Override
            public void onChanged(Change change) {
                change.next();
                if (change.wasReplaced() && !suspended) {
                    this.suspended = true;
                    tableView.getColumns().setAll(columns);
                    this.suspended = false;
                }
            }
        });
    }


    /**
     * Retrieves the wind direction, scales the size of the arrow and then draws it on the Group
     */
    private void startWindDirection() {
//        arrow.setScaleX(0.4);
        windDisplay = new WindDisplay(race, arrow, speedLabel);
        windDisplay.start();
    }


    /**
     * Initialises race variables and begins the race loop. Adds listeners to the race view to listen for when the window
     * has been re-sized.
     *
     * @param race        the race which is going to be displayed.
     * @param sender      the sender
     * @param interpreter the interpreter
     */
    public void setUp(Race race, Interpreter interpreter, Sender sender) {
//        this.receiver = receiver;
        this.sender = sender;
        this.race = race;

        pixelMapper = new PixelMapper(race.getCourse(), raceViewPane);
        raceRenderer = new RaceRenderer(pixelMapper, race, group);
        raceRenderer.renderBoats();
        courseRenderer = new CourseRenderer(pixelMapper, race.getCourse(), group, raceViewPane);

        raceClock = new RaceClock(timerLabel);
        raceClock.start();

        raceLoop = new RaceLoop(raceRenderer, courseRenderer, new FPSReporter(fpsLabel));
        raceLoop.start();

        raceViewPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> redrawFeatures());
        raceViewPane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> redrawFeatures());
        pixelMapper.zoomLevelProperty().addListener((observable, oldValue, newValue) -> redrawFeatures());
        pixelMapper.addViewCenterListener(propertyChangeEvent -> redrawFeatures());

        startWindDirection();
        setUpTable(raceRenderer.boatColors());
        setNoneAnnotationLevel();
        setUpSparklines(raceRenderer.boatColors());

        Stage stage = (Stage) tableView.getScene().getWindow();
//        Interpreter interpreter = new Interpreter(receiver, stage);
        interpreter.setInterpreter(initialiseInterpreter());
    }


    /**
     * Set up and initialise interpreter variables, adding interpreters of each relevant type to the global interpreter.
     *
     * @return the initialised interpreter
     */
    private MessageInterpreter initialiseInterpreter() {
        MessageInterpreter interpreter = new CompositeMessageInterpreter();

        interpreter.add(AC35MessageType.XML_RACE.getCode(), new XMLRaceInterpreter(race));
        interpreter.add(AC35MessageType.XML_BOATS.getCode(), new XMLBoatInterpreter(race));
        interpreter.add(AC35MessageType.XML_REGATTA.getCode(), new XMLRegattaInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceTimeInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new WindDirectionInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new WindSpeedInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new EstimatedTimeInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new FinishersListInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new BoatStatusInterpreter(race));
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new BoatLocationInterpreter(race));
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new MarkLocationInterpreter(race));
        interpreter.add(AC35MessageType.MARK_ROUNDING.getCode(), new MarkRoundingInterpreter(race));
        interpreter.add(AC35MessageType.ACCEPTANCE.getCode(), new AcceptanceInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceClockInterpreter(raceClock));

        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new BoatSailInterpreter(race));

        return interpreter;
    }


    /**
     * To call when course features need redrawing.
     * (For example, when zooming in, the course features are required to change)
     */
    public void redrawFeatures() {
        courseRenderer.renderCourse();
        raceRenderer.renderBoats();
        raceRenderer.reDrawTrails();
    }


    /**
     * Receives updates from the ImportantAnnotationController to update the important annotations
     *
     * @param o the observable object.
     * @param arg an argument passed to the notifyObservers method.
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
}
