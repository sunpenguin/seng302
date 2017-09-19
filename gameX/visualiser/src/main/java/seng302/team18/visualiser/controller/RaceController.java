package seng302.team18.visualiser.controller;

import com.google.common.collect.Lists;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import seng302.team18.interpret.CompositeMessageInterpreter;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35MessageType;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Coordinate;
import seng302.team18.model.RaceMode;
import seng302.team18.encode.Sender;
import seng302.team18.util.GPSCalculator;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.display.*;
import seng302.team18.visualiser.display.ui.Clock;
import seng302.team18.visualiser.display.render.*;
import seng302.team18.visualiser.display.ui.DisplaySparkline;
import seng302.team18.visualiser.display.ui.FPSReporter;
import seng302.team18.visualiser.display.ui.StopWatchClock;
import seng302.team18.visualiser.interpret.Interpreter;
import seng302.team18.visualiser.interpret.americascup.*;
import seng302.team18.visualiser.interpret.unique.*;
import seng302.team18.visualiser.interpret.xml.XMLBoatInterpreter;
import seng302.team18.visualiser.interpret.xml.XMLRaceInterpreter;
import seng302.team18.visualiser.interpret.xml.XMLRegattaInterpreter;
import seng302.team18.visualiser.userInput.ControlSchemeDisplay;
import seng302.team18.visualiser.util.PixelMapper;
import seng302.team18.visualiser.util.SparklineDataGetter;
import seng302.team18.visualiser.util.SparklineDataPoint;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The controller class for the Main Window.
 * The main window consists of the right hand pane with various displays and the race on the left.
 */
public class RaceController implements Observer {
    @FXML
    private Group group;
    @FXML
    private Label fpsLabel;
    @FXML
    private TableView<Boat> tableView;
    @FXML
    private TableColumn<Boat, Integer> boatPositionColumn;
    @FXML
    private TableColumn<Boat, String> boatNameColumn;
    @FXML
    private TableColumn<Boat, Double> boatSpeedColumn;
    @FXML
    private TableColumn<Boat, String> boatColorColumn;
    @FXML
    private TableColumn<Boat, String> boatStatusColumn;
    @FXML
    private Pane raceViewPane;
    @FXML
    private Polygon arrow;
    @FXML
    private Label speedLabel;
    @FXML
    private CategoryAxis yPositionsAxis;
    @FXML
    private LineChart<String, String> sparklinesChart;
    @FXML
    private Slider slider;
    @FXML
    private AnchorPane tabView;

    private Pane escapeMenuPane;

    private boolean fpsOn;
    private boolean onImportant;
    private boolean sailIn = false;

    private ClientRace race;
    private RaceRenderer raceRenderer;
    private CourseRenderer courseRenderer;
    private PixelMapper pixelMapper;
    private Map<AnnotationType, Boolean> importantAnnotations;
    private Sender sender;

    private Interpreter interpreter;
    private RaceBackground background;

    private FadeTransition fadeIn = new FadeTransition(Duration.millis(150));
    private Map<String, Color> colours;

    private ControlsTutorial controlsTutorial;

    private Clock clock;
    private HBox timeBox;

    private VBox finishResultsBox = null;


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
        background = new RaceBackground(raceViewPane, "/images/water.gif");
        tabView.setVisible(false);
        initialiseFadeTransition();
    }


    /**
     * Handles all boat controls.
     *
     * @param keyEvent event for a particular key press
     */
    private MessageBody handlePlayerKeyPress(KeyEvent keyEvent) {
        if (race.getMode() != RaceMode.SPECTATION) {
            BoatActionMessage message = new BoatActionMessage(race.getPlayerId());
            switch (keyEvent.getCode()) {
                case SPACE:
                    message.setAutoPilot();
                    break;
                case ENTER:
                    message.setTackGybe();
                    break;
                case PAGE_UP:
                case UP:
                    message.setUpwind();
                    break;
                case PAGE_DOWN:
                case DOWN:
                    message.setDownwind();
                    break;
                case SHIFT:
                    sailIn = getPlayerBoat().isSailOut();
                    if (sailIn) {
                        message.setSailIn();
                    } else {
                        message.setSailOut();
                    }
                    break;
                case S:
                    message.setConsume();
                    race.activatePowerUp();
                    break;
                default:
                    return null;
            }
            return message;
        }
        return null;
    }


    /**
     * Handles zooming, escape and viewing annotations.
     *
     * @param keyEvent event for a particular key press
     */
    private void handleBasicKeyPress(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case Z:
                if (pixelMapper.getZoomLevel() > 0) {
                    pixelMapper.setZoomLevel(pixelMapper.getZoomLevel() + 1);
                } else {
                    Boat boat = race.getBoat(race.getStartingList().get(0).getId());
                    pixelMapper.setZoomLevel(1);
                    pixelMapper.track(boat);
                    pixelMapper.setTracking(true);
                }
                break;
            case X:
                if (pixelMapper.getZoomLevel() - 1 <= 0) {
                    pixelMapper.setTracking(false);
                    pixelMapper.setViewPortCenter(race.getCenter());
                }
                pixelMapper.setZoomLevel(pixelMapper.getZoomLevel() - 1);

                break;
            case ESCAPE:
                if (group.getChildren().contains(escapeMenuPane)) {
                    group.getChildren().remove(escapeMenuPane);
                } else {
                    openEscapeMenu("");
                }
                break;
            case TAB:
                toggleTabView();
                break;
//            default:
//                encode = true;
        }
    }


    /**
     * Register key presses to certain methods.
     * <p>
     * Allow boat control only to actual racing player (client in spectator mode can only view the race instead of playing).
     */
    private void installKeyHandler() {
        EventHandler<KeyEvent> keyEventHandler =
                keyEvent -> {
                    if (keyEvent.getCode() != null) {
//                        BoatActionMessage message = null;

                        MessageBody message = handlePlayerKeyPress(keyEvent);

                        handleBasicKeyPress(keyEvent);

                        if (message != null) {
                            if (race.getMode() == RaceMode.CONTROLS_TUTORIAL) {
                                controlsTutorial.setBoat(getPlayerBoat()); //TODO: get sam to change this seb67 17/8
                                controlsTutorial.setWindDirection(race.getCourse().getWindDirection());
                                if (controlsTutorial.checkIfProgressed(keyEvent.getCode())) {
                                    controlsTutorial.displayNext();
                                }
                            }
                            try {
                                sender.send(message);
                            } catch (IOException e) {
                                openEscapeMenu("You have been disconnected!");
                            }
                        }
                    }
                };

        raceViewPane.addEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
        raceViewPane.setFocusTraversable(true);
        raceViewPane.requestFocus();
        raceViewPane.focusedProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> raceViewPane.requestFocus()));
    }


    /**
     * Gets the current player's boat depending on the visualiser.
     *
     * @return the boat controlled by the user.
     */
    private Boat getPlayerBoat() {
        List<Boat> playerBoatList = race.getStartingList()
                .stream()
                .filter(boat -> boat.getId() == race.getPlayerId())
                .collect(Collectors.toList());
        if (!playerBoatList.isEmpty()) {
            return playerBoatList.get(0);
        }
        return null;
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
    private void sliderSetup() {
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
        slider.valueProperty().addListener((ov, old_val, new_val) -> {
            if (new_val.doubleValue() == 0d) {
                setNoneAnnotationLevel();
            }
            if (new_val.doubleValue() == 0.5d) {
                setToImportantAnnotationLevel();
            }
            if (new_val.doubleValue() == 1d) {
                setFullAnnotationLevel();
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
     * Set up a fade transition for the tab view.
     * Tab view will fade in when tab is pressed.
     */
    private void initialiseFadeTransition() {
        fadeIn.setNode(tabView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(0.85);
        fadeIn.setCycleCount(1);
        fadeIn.setAutoReverse(true);
    }


    /**
     * Toggle the tabView (holds detailed race info on and off)
     */
    private void toggleTabView() {
        if (tabView.isVisible()) {
            tabView.setVisible(false);
        } else {
            tabView.setVisible(true);
            fadeIn.play();
        }
    }


    /**
     * Loads the FXML and controller for the escape menu.
     */
    private void loadEscapeMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("EscapeMenu.fxml"));
            escapeMenuPane = loader.load();
            EscapeMenuController escapeMenuController = loader.getController();
            escapeMenuController.setup(group, interpreter, sender);
        } catch (IOException e) {
        }
    }


    /**
     * Opens the escapeMenu by adding to the group and placing it in the middle of the race view.
     */
    private void openEscapeMenu(String message) {
        if (!group.getChildren().contains(escapeMenuPane)) {
            Label label = new Label(message);
            label.getStylesheets().addAll(ControlsTutorial.class.getResource("/stylesheets/style.css").toExternalForm());
            label.getStyleClass().add("message");
            HBox box = new HBox(label);
            box.setAlignment(Pos.TOP_CENTER);
            box.setPadding(new Insets(6, 6, 6, 6));
            box.setPrefWidth(escapeMenuPane.getMinWidth());
            label.setMaxWidth(box.getPrefWidth());
            label.setWrapText(true);
            escapeMenuPane.getChildren().add(box);
            group.getChildren().add(escapeMenuPane);
            escapeMenuPane.toFront();
            escapeMenuPane.setLayoutX((raceViewPane.getWidth() / 2) - escapeMenuPane.getMinWidth() / 2);
            escapeMenuPane.setLayoutY((raceViewPane.getHeight() / 2) - escapeMenuPane.getMinHeight() / 2);
        }
    }


    /**
     * Sets the cell values for the race table, these are place, boat name and boat speed.
     */
    private void setUpTable() {
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
        boatNameColumn.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        boatStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusString"));

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

        colours = raceRenderer.boatColors();
        boatColorColumn.setCellFactory(column -> new TableCell<Boat, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem("", empty);

                Boat boat = (Boat) getTableRow().getItem();
                if (boat != null) {
                    Color colour = colours.get(boat.getShortName());
                    if (colour != null) {
                        setStyle(String.format("-fx-background-color: #%02x%02x%02x", (int) (colour.getRed() * 255), (int) (colour.getGreen() * 255), (int) (colour.getBlue() * 255)));
                    }
                }
            }
        });

        Collection<TableColumn<Boat, ?>> columns = new ArrayList<>();
        columns.add(boatPositionColumn);
        columns.add(boatColorColumn);
        columns.add(boatNameColumn);
        columns.add(boatSpeedColumn);
        columns.add(boatStatusColumn);

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
        WindDisplay windDisplay = new WindDisplay(race, arrow, speedLabel);
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
    public void setUp(ClientRace race, Interpreter interpreter, Sender sender) {
        this.sender = sender;
        this.race = race;

        GPSCalculator gps = new GPSCalculator();
        List<Coordinate> bounds = gps.findMinMaxPoints(race.getCourse());

        // Force a resize of the pane to ensure that pixel mapper gets correct width/height while display objects are
        // being initialized
        raceViewPane.resize(raceViewPane.getPrefWidth(), raceViewPane.getPrefHeight());

        pixelMapper = new PixelMapper(
                bounds.get(0), bounds.get(1), race.getCourse().getCenter(),
                raceViewPane.heightProperty(), raceViewPane.widthProperty()
        );

        pixelMapper.setMaxZoom(16d);
        pixelMapper.calculateMappingScale();
        raceRenderer = new RaceRenderer(pixelMapper, race, group);
        raceRenderer.render();
        colours = raceRenderer.boatColors();
        courseRenderer = new CourseRenderer(pixelMapper, race.getCourse(), group, race.getMode());
        raceRenderer.setDrawTrails(RaceMode.BUMPER_BOATS != race.getMode());
        List<Renderable> renderables = new ArrayList<>(Arrays.asList(raceRenderer, courseRenderer));
        if (race.getMode().hasLives() && getPlayerBoat() != null) {
            renderables.add(new VisualHealth(raceViewPane, getPlayerBoat()));
        }

        if (getPlayerBoat() != null) {
            renderables.add(new VisualPowerUp(raceViewPane, getPlayerBoat()));
        }

        setupRaceTimer();
        startRaceTimer();

        new ControlSchemeDisplay(raceViewPane);

        RaceLoop raceLoop = new RaceLoop(renderables, new FPSReporter(fpsLabel), pixelMapper);
        raceLoop.start();

        registerListeners();

        startWindDirection();
        setUpTable();
        setNoneAnnotationLevel();
        setUpSparklines(raceRenderer.boatColors());

        this.interpreter = interpreter;
        interpreter.setInterpreter(initialiseInterpreter());
        interpreter.addObserver(this);

        loadEscapeMenu();


        race.getStartingList().forEach(boat -> boat.setPlace(race.getStartingList().size()));
    }


    /**
     * Set up the container and label for the race clock. Text is displayed in the center of the container.
     */
    private void setupRaceTimer() {
        Label timeLabel = new Label();
        timeBox = new HBox(timeLabel);
        timeBox.getStylesheets().addAll(ControlsTutorial.class.getResource("/stylesheets/raceview.css").toExternalForm());
        timeBox.getStyleClass().add("timeBox");
        timeLabel.setPrefWidth(80);
        timeBox.setPrefWidth(120);
        timeBox.setAlignment(Pos.CENTER);
        clock = new StopWatchClock(timeLabel);
        raceViewPane.getChildren().add(timeBox);
    }


    /**
     * Start the race clock by invoking start() on the RaceClock's AnimationTimer.
     */
    private void startRaceTimer() {
        clock.start();
    }


    /**
     * Set the layout of the race clock to the top-center of the race view pane.
     */
    private void redrawTimeLabel() {
        timeBox.setLayoutX((raceViewPane.getWidth()) / 2.0 - (timeBox.getPrefWidth() / 2.0));
        timeBox.setLayoutY(10.0);
    }


    /**
     * Register any required listeners.
     */
    private void registerListeners() {
        InvalidationListener listenerWidth = observable -> {
            redrawFeatures();
            updateControlsTutorial();
            raceRenderer.clearCollisions();
        };
        raceViewPane.widthProperty().addListener(listenerWidth);

        InvalidationListener listenerHeight = observable -> {
            redrawFeatures();
            updateControlsTutorial();
            raceRenderer.clearCollisions();
        };
        raceViewPane.heightProperty().addListener(listenerHeight);

        pixelMapper.zoomLevelProperty().addListener((observable, oldValue, newValue) -> {
            redrawFeatures();
            raceRenderer.clearCollisions();
        });
        pixelMapper.addViewCenterListener(propertyChangeEvent -> redrawFeatures());
    }


    /**
     * Re draw the current elements of the controls tutorial, or initialise it if it is NULL.
     */
    public void updateControlsTutorial() {
        if (race.getMode() == RaceMode.CONTROLS_TUTORIAL) {
            if (controlsTutorial == null) {
                controlsTutorial = new ControlsTutorial(raceViewPane, race.getCourse().getWindDirection(), race.getStartingList().get(0));
                controlsTutorial.displayNext();
            } else {
                controlsTutorial.draw();
            }
        }
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
        BoatStatusInterpreter boatStatusInterpreter = new BoatStatusInterpreter(race);
        boatStatusInterpreter.addObserver(this);
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), boatStatusInterpreter);
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new BoatLocationInterpreter(race));
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new MarkLocationInterpreter(race));
        interpreter.add(AC35MessageType.MARK_ROUNDING.getCode(), new MarkRoundingInterpreter(race));
        interpreter.add(AC35MessageType.YACHT_EVENT.getCode(), new YachtEventInterpreter(race));
        interpreter.add(AC35MessageType.ACCEPTANCE.getCode(), new AcceptanceInterpreter(race));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceClockInterpreter(clock));
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new FinishRaceInterpreter(this));
        interpreter.add(AC35MessageType.POWER_UP.getCode(), new PowerUpInterpreter(race));
        interpreter.add(AC35MessageType.POWER_TAKEN.getCode(), new PowerTakenInterpreter(race));
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new BoatSailInterpreter(race));
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new BoatLivesInterpreter(race));
        interpreter.add(AC35MessageType.PROJECTILE_LOCATION.getCode(), new ProjectileLocationInterpreter(race));
        interpreter.add(AC35MessageType.PROJECTILE_CREATION.getCode(), new ProjectileCreationInterpreter(race));
        interpreter.add(AC35MessageType.PROJECTILE_GONE.getCode(), new ProjectileGoneInterpreter(race));

        return interpreter;
    }


    /**
     * To call when GUI features need redrawing.
     * (For example, when zooming in, the course features are required to change)
     */
    public void redrawFeatures() {
        pixelMapper.calculateMappingScale();
        background.renderBackground();
        courseRenderer.render();
        raceRenderer.render();
        raceRenderer.refresh();
        redrawTimeLabel();
    }


    /**
     * Displays the result of the race in table form
     */
    public void showFinishersList() {
        if (finishResultsBox == null) {
            String result = "MATCH COMPLETE\n\n";

            List<Boat> boats = race.getStartingList();

            if (race.getMode().equals(RaceMode.BUMPER_BOATS)) {
                result += bumperFinishInfo(boats);
            } else if (race.getMode().equals(RaceMode.CHALLENGE_MODE)) {
                result += challengeFinishInfo(boats);
            } else {
                result += genericFinishInfo(boats);
            }

            result = result.substring(0, result.length() - 1);
            Label resultLabel = new Label(result);
            finishResultsBox = new VBox(resultLabel);
            finishResultsBox.getStylesheets().addAll(RaceController.class.getResource("/stylesheets/style.css").toExternalForm());
            finishResultsBox.getStyleClass().add("finishTable");
            raceViewPane.getChildren().add(finishResultsBox);
            finishResultsBox.setLayoutX(50);
            finishResultsBox.setLayoutY(200);
        }
    }


    /**
     * Construct a string with finish information for a generic race.
     * Displays Placing, name, status(FINISHED, DSQ, etc) for each boat on a newline.
     *
     * @param boats to display info for.
     * @return constructed string
     */
    private String genericFinishInfo(List<Boat> boats) {
        boats.sort(Comparator.comparing(Boat::getPlace));
        String result = "";

        for (Boat b : boats) {
            result += b.getPlace() + ": " + b.getShortName() + " " + b.getStatus().name() + "\n";
        }

        return result;
    }


    /**
     * Construct a string with finish information for a generic race.
     * Displays Name, status(WINNER or DEAD) for each boat on a newline.
     *
     * @param boats to display info for.
     * @return constructed string
     */
    private String bumperFinishInfo(List<Boat> boats) {
        boats.sort(Comparator.comparing(Boat::getLives));
        boats = Lists.reverse(boats);
        String result = "";

        for (Boat b : boats) {
            String state = b.getLives() > 0 ? "WINNER" : "DEAD";
            result += b.getShortName() + " " + state + "\n";
        }

        return result;
    }


    /**
     * Construct a string with finish information for a generic race.
     * Displays Name, leg number for each boat on a newline.
     *
     * @param boats to display info for.
     * @return constructed string
     */
    private String challengeFinishInfo(List<Boat> boats) {
        boats.sort(Comparator.comparing(Boat::getPlace));

        String result = "";

        for (Boat b : boats) {
            result += b.getShortName() + " " + b.getLegNumber() + " GATES CLEARED\n";
        }

        return result;
    }


    /**
     * Receives updates from the ImportantAnnotationController to update the important annotations
     *
     * @param o   the observable object.
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
        } else if (arg instanceof Boolean) {
            Platform.runLater(() -> openEscapeMenu("CONNECTION TO SERVER LOST"));
        } else if (arg instanceof Boat) {
            Platform.runLater(() -> {
                if (race.getMode().equals(RaceMode.CHALLENGE_MODE) || race.getMode().equals(RaceMode.BUMPER_BOATS)) {
                    openEscapeMenu("GAME OVER");
                } else {
                    openEscapeMenu("YOU HAVE BEEN DISQUALIFIED FOR LEAVING THE COURSE BOUNDARIES");
                }
                sender.close();
            });
        }
    }
}
