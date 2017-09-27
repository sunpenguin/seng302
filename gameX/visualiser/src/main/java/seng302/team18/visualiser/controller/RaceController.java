package seng302.team18.visualiser.controller;

import com.google.common.collect.Lists;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Duration;
import seng302.team18.encode.Sender;
import seng302.team18.interpret.CompositeMessageInterpreter;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35MessageType;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.*;
import seng302.team18.util.GPSCalculator;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.display.*;
import seng302.team18.visualiser.display.render.*;
import seng302.team18.visualiser.display.ui.Clock;
import seng302.team18.visualiser.display.ui.FPSReporter;
import seng302.team18.visualiser.display.ui.StopWatchClock;
import seng302.team18.visualiser.interpret.Interpreter;
import seng302.team18.visualiser.interpret.americascup.*;
import seng302.team18.visualiser.interpret.unique.*;
import seng302.team18.visualiser.interpret.xml.XMLBoatInterpreter;
import seng302.team18.visualiser.interpret.xml.XMLRaceInterpreter;
import seng302.team18.visualiser.interpret.xml.XMLRegattaInterpreter;
import seng302.team18.visualiser.sound.SoundEffect;
import seng302.team18.visualiser.sound.SoundEffectPlayer;
import seng302.team18.visualiser.sound.ThemeTunePlayer;
import seng302.team18.visualiser.userInput.ControlSchemeDisplay;
import seng302.team18.visualiser.util.PixelMapper;

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
    private TableColumn<Boat, Integer> boatStatusColumn;
    @FXML
    private Pane raceViewPane;
    @FXML
    private Label speedLabel;
    @FXML
    private AnchorPane tabView;
    @FXML
    private Label currentRankingsLabel;

    private Pane escapeMenuPane;
    private Pane eventMenuPane;
    private ThemeTunePlayer themeTunePlayer;
    private ThemeTunePlayer wavePlayer;

    private boolean annotationsOn;
    private boolean fpsOn;

    private ClientRace race;
    private RaceRenderer raceRenderer;
    private CourseRenderer courseRenderer;
    private PixelMapper pixelMapper;
    private Sender sender;
    private SoundEffectPlayer soundPlayer;

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
        raceViewPane.getStylesheets().add(this.getClass().getResource("/stylesheets/raceview.css").toExternalForm());
        tabView.getStylesheets().add(this.getClass().getResource("/stylesheets/raceview.css").toExternalForm());
        fpsLabel.getStyleClass().add("fpsLabel");
        installKeyHandler();
        installTabHandler();
        annotationsOn = false;
        fpsOn = true;
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
                    if (null != getPlayerBoat()) {
                        if (getPlayerBoat().isSailOut()) {
                            message.setSailIn();
                        } else {
                            message.setSailOut();
                        }
                    }
                    break;
                case S:
                    message.setConsume();
                    race.activatePowerUp();
                    break;
                default:
                    return null;
            }
            keyEvent.consume();
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
                    pixelMapper.setTracking(true);
                    pixelMapper.setZoomLevel(1);
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
                    openEscapeMenu("", escapeMenuPane);
                }
                break;
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

                        MessageBody message = handlePlayerKeyPress(keyEvent);

                        handleBasicKeyPress(keyEvent);

                        if (message != null) {
                            if (race.getMode() == RaceMode.CONTROLS_TUTORIAL) {
                                controlsTutorial.setBoat(getPlayerBoat());
                                controlsTutorial.setWindDirection(race.getCourse().getWindDirection());
                                if (controlsTutorial.checkIfProgressed(keyEvent.getCode())) {
                                    controlsTutorial.displayNext();
                                }
                            }
                            try {
                                sender.send(message);
                            } catch (IOException e) {
                                openEscapeMenu("You have been disconnected!", eventMenuPane);
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
     * Allows for the TAB menu to he viewed by holding down the tab button.
     */
    private void installTabHandler() {
        final KeyCombination tab = new KeyCodeCombination(KeyCode.TAB);

        raceViewPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (tab.match(event)) {
                tabView.setVisible(true);
                event.consume();
            }
        });

        raceViewPane.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (tab.match(event)) {
                tabView.setVisible(false);
                event.consume();
            }
        });
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
     * Gets the pane used for the escape menu.
     *
     * @return pane used for the escape menu.
     */
    public Pane getEscapeMenuPane() {
        return escapeMenuPane;
    }


    /**
     * Toggles the fps by setting label to be visible / invisible.
     */
    public void toggleFPS() {
        fpsOn = !fpsOn;
        fpsLabel.setVisible(fpsOn);
    }


    /**
     * Toggles the annotations by setting annotations to be visible / invisible.
     */
    public void toggleAnnotations() {
        annotationsOn = !annotationsOn;
        if (annotationsOn) {
            setFullAnnotationLevel();
        } else {
            setNoneAnnotationLevel();
        }
    }


    /**
     * Sets the annotation level to be full (all annotations showing)
     */
    private void setFullAnnotationLevel() {
        for (AnnotationType type : AnnotationType.values()) {
            raceRenderer.setVisibleAnnotations(type, true);
        }
    }


    /**
     * Sets the annotation level to be none (no annotations showing)
     */
    private void setNoneAnnotationLevel() {
        for (AnnotationType type : AnnotationType.values()) {
            raceRenderer.setVisibleAnnotations(type, false);
        }
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
     * Loads the FXML and controller for the escape menu.
     */
    @SuppressWarnings("Duplicates")
    private void loadEscapeMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("EscapeMenu.fxml"));
            escapeMenuPane = loader.load();
            EscapeMenuController escapeMenuController = loader.getController();
            escapeMenuController.setup(group, interpreter, sender, this, soundPlayer, themeTunePlayer, wavePlayer);
        } catch (IOException e) {
            //pass
        }
    }


    /**
     * Loads the FXML and controller for the event menu.
     */

    @SuppressWarnings("Duplicates")
    private void loadEventMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("EventMenu.fxml"));
            eventMenuPane = loader.load();
            EventMenuController eventMenuController = loader.getController();
            eventMenuController.setup(group, interpreter, sender, soundPlayer, themeTunePlayer, wavePlayer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Opens the menu by adding to the group and placing it in the middle of the race view.
     */
    private void openEscapeMenu(String message, Pane menuPane) {
        if (!group.getChildren().contains(menuPane)) {
            Label label = new Label(message);
            label.getStylesheets().addAll(ControlsTutorial.class.getResource("/stylesheets/style.css").toExternalForm());
            label.getStyleClass().add("message");
            HBox box = new HBox(label);
            box.setAlignment(Pos.TOP_CENTER);
            box.setPadding(new Insets(6, 6, 6, 6));
            box.setPrefWidth(menuPane.getMinWidth());
            label.setMaxWidth(box.getPrefWidth());
            label.setWrapText(true);
            menuPane.getChildren().add(box);
            group.getChildren().add(menuPane);
            menuPane.toFront();
            menuPane.setLayoutX((raceViewPane.getWidth() / 2) - menuPane.getMinWidth() / 2);
            menuPane.setLayoutY((raceViewPane.getHeight() / 2) - menuPane.getMinHeight() / 2);
        }
    }


    /**
     * Sets the cell values for the race table, these are place, boat name and boat speed.
     */
    private void setUpTable() {
        setTableData();
        boatPositionColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        boatNameColumn.setCellValueFactory(new PropertyValueFactory<>("shortName"));

        if (race.getMode() == RaceMode.BUMPER_BOATS) {
            bumperBoatTabMenu();
        } else {
            boatStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusString"));
        }


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
            private boolean suspended;

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
     * Sets the table data of the table depending on the mode.
     */
    private void setTableData() {
        if (race.getMode() == RaceMode.BUMPER_BOATS) {
            Callback<Boat, Observable[]> callback = (Boat boat) -> new Observable[]{
                    boat.livesIntegerProperty()
            };
            ObservableList<Boat> observableList = FXCollections.observableArrayList(callback);
            observableList.addAll(race.getStartingList());

            SortedList<Boat> sortedList = new SortedList<>(observableList, Comparator.comparingInt(Boat::getLives).reversed());
            tableView.setItems(sortedList);
        } else {
            Callback<Boat, Observable[]> callback = (Boat boat) -> new Observable[]{
                    boat.placeProperty(), boat.speedProperty()
            };
            ObservableList<Boat> observableList = FXCollections.observableArrayList(callback);
            observableList.addAll(race.getStartingList());

            SortedList<Boat> sortedList = new SortedList<>(observableList, Comparator.comparingInt(Boat::getPlace));
            tableView.setItems(sortedList);
        }
    }


    /**
     * Alters tab menu for Bumper Boats.
     */
    private void bumperBoatTabMenu() {
        boatPositionColumn.setCellFactory(col -> new TableCell<Boat, Integer>() {
            @Override
            public void updateItem(Integer position, boolean empty) {
                super.updateItem(position, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText("N/A");
                }
            }
        });

        boatStatusColumn.setText("Lives");
        boatStatusColumn.setCellValueFactory(new PropertyValueFactory<>("livesInteger"));
        boatStatusColumn.setCellFactory(col -> new TableCell<Boat, Integer>() {
            @Override
            public void updateItem(Integer lives, boolean empty) {
                super.updateItem(lives, empty);
                if (empty) {
                    setText(null);
                } else if (lives == 1) {
                    setText(lives + " life");
                } else {
                    setText(lives + " lives");
                }
            }
        });
    }


    /**
     * Retrieves the wind direction, scales the size of the arrow and then draws it on the Group
     */
    private void startWindDirection() {
        WindDisplay windDisplay = new WindDisplay(race, speedLabel, raceViewPane);
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

        themeTunePlayer = new ThemeTunePlayer();
        themeTunePlayer.playSound("audio/theme.mp3", 0.6);
        wavePlayer = new ThemeTunePlayer();
        wavePlayer.playSound("audio/Ocean_Waves-Mike_Koenig-980635527.mp3", 0.175);

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
        raceRenderer = new RaceRenderer(pixelMapper, race, group, soundPlayer);
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

        this.interpreter = interpreter;
        interpreter.setInterpreter(initialiseInterpreter());
        interpreter.addObserver(this);

        loadEscapeMenu();
        loadEventMenu();

        race.getStartingList().forEach(boat -> boat.setPlace(race.getStartingList().size()));

        if (race.getMode() != RaceMode.SPECTATION) {
            pixelMapper.track(race.getBoat(race.getPlayerId()));
        } else {
            pixelMapper.track(race.getBoat(race.getStartingList().get(0).getId()));
        }
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
     * Set the layout of the tab view to the middle-center of the race view pane.
     */
    private void redrawTabView() {
        currentRankingsLabel.setLayoutX(tabView.getPrefWidth() / 2 - currentRankingsLabel.getPrefWidth() / 2);
        tabView.setLayoutX((raceViewPane.getWidth() / 2) - tabView.getPrefWidth() / 2);
        tabView.setLayoutY((raceViewPane.getHeight() / 2) - tabView.getPrefHeight() / 2);
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
        boatStatusInterpreter.addCallback(BoatStatus.DSQ, (isPlayer) -> {
            if (isPlayer) soundPlayer.playEffect(SoundEffect.PLAYER_DISQUALIFIED);
        });
        boatStatusInterpreter.addCallback(BoatStatus.DSQ, (isPlayer) -> {
            if (isPlayer) openEscapeMenuOnDsq();
        });
        boatStatusInterpreter.addCallback(BoatStatus.FINISHED, (isPlayer) -> {
            if (isPlayer) soundPlayer.playEffect(SoundEffect.CROSS_FINISH_LINE);
        });

        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), boatStatusInterpreter);
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new BoatLocationInterpreter(race));
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new MarkLocationInterpreter(race));
        interpreter.add(AC35MessageType.MARK_ROUNDING.getCode(), new MarkRoundingInterpreter(race));
        YachtEventInterpreter yachtEventInterpreter = new YachtEventInterpreter(race);
        yachtEventInterpreter.addCallback(YachtEventCode.ACTIVATED_SPEED_BOOST, isPlayerBoost -> soundPlayer.playEffect(SoundEffect.ACTIVATE_SPEED_BOOST));
        interpreter.add(AC35MessageType.YACHT_EVENT.getCode(), yachtEventInterpreter);
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), new RaceClockInterpreter(clock));
        RaceStatusInterpreter raceStatusInterpreter = new RaceStatusInterpreter(race);
        raceStatusInterpreter.addCallback(RaceStatus.FINISHED, aBoolean -> onRaceFinishAction());
        interpreter.add(AC35MessageType.RACE_STATUS.getCode(), raceStatusInterpreter);
        interpreter.add(AC35MessageType.POWER_UP.getCode(), new PowerUpInterpreter(race));
        PowerTakenInterpreter powerTakenInterpreter = new PowerTakenInterpreter(race);
        powerTakenInterpreter.setCallback(isPlayerPickup -> {
            if (isPlayerPickup) soundPlayer.playEffect(SoundEffect.PICK_UP_POWER_UP);
        });
        interpreter.add(AC35MessageType.POWER_TAKEN.getCode(), powerTakenInterpreter);
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new BoatSailInterpreter(race));
        interpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), new BoatLivesInterpreter(race, (wasPlayerLostLife) -> soundPlayer.playEffect(SoundEffect.LOSE_LIFE)));
        interpreter.add(AC35MessageType.PROJECTILE_LOCATION.getCode(), new ProjectileLocationInterpreter(race));
        interpreter.add(AC35MessageType.PROJECTILE_CREATION.getCode(), new ProjectileCreationInterpreter(race, (wasFiredByPlayer) -> soundPlayer.playEffect(SoundEffect.FIRE_BULLET)));
        interpreter.add(AC35MessageType.PROJECTILE_GONE.getCode(), new ProjectileGoneInterpreter(race));

        return interpreter;
    }


    /**
     * To call when GUI features need redrawing.
     * (For example, when zooming in, the course features are required to change)
     */
    private void redrawFeatures() {
        pixelMapper.calculateMappingScale();
        background.renderBackground();
        courseRenderer.render();
        raceRenderer.render();
        raceRenderer.refresh();
        redrawTimeLabel();
        redrawTabView();
    }


    /**
     * Displays the result of the race in table form
     */
    private void showFinishersList() {
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
        StringBuilder result = new StringBuilder();

        for (Boat b : boats) {
            result.append(b.getPlace()).append(": ").append(b.getShortName()).append(" ").append(b.getStatus().name()).append("\n");
        }

        return result.toString();
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
        StringBuilder result = new StringBuilder();
        Boat winner = findWinner(boats);

        for (Boat b : boats) {
            String state = b.equals(winner) ? "WINNER" : "DEAD";
            result.append(b.getShortName()).append(" ").append(state).append("\n");
        }

        return result.toString();
    }


    /**
     * Find the boat that has the maximum number of lives in all players' boats.
     *
     * @param boats list of participanting boats.
     * @return a boat has the maximum number of lives
     */
    private Boat findWinner(List<Boat> boats) {
        Boat winner = boats.get(0);

        for (Boat boat : boats) {
            if (boat.getLives() > winner.getLives()) {
                winner = boat;
            }
        }

        return winner;
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

        StringBuilder result = new StringBuilder();

        for (Boat b : boats) {
            result.append(b.getShortName()).append(" ").append(b.getLegNumber()).append(" GATES CLEARED\n");
        }

        return result.toString();
    }


    /**
     * Receives updates from observed classes.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the notifyObservers method.
     */
    @Override
    public void update(java.util.Observable o, Object arg) {
        if (arg instanceof Boolean) {
            Platform.runLater(() -> openEscapeMenu("CONNECTION TO SERVER LOST", eventMenuPane));
        }
    }


    private void openEscapeMenuOnDsq() {
        boolean boundaryDisqualification = !race.getMode().equals(RaceMode.CHALLENGE_MODE) && !race.getMode().equals(RaceMode.BUMPER_BOATS);
        String message = (boundaryDisqualification) ? "YOU HAVE BEEN DISQUALIFIED FOR LEAVING THE COURSE BOUNDARIES" : "GAME OVER";

        Platform.runLater(() -> {
            openEscapeMenu(message, eventMenuPane);
            sender.close();
        });
    }


    private void onRaceFinishAction() {
        Platform.runLater(() -> {
            showFinishersList();
            openEscapeMenu("Match Complete", eventMenuPane);
            sender.close();
        });
    }


    /**
     * @param player manages the audio playback from this scene
     */
    public void setSoundPlayer(SoundEffectPlayer player) {
        this.soundPlayer = player;
    }
}
