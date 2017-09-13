package seng302.team18.visualiser.controller;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.messageparsing.Receiver;
import seng302.team18.model.RaceMode;
import seng302.team18.send.ControllerMessageFactory;
import seng302.team18.send.Sender;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.util.ModelLoader;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * Controller For the game type selection screen
 */
public class GameSelectionController {

    @FXML
    private Pane buttonBox;
    @FXML
    private Pane optionsBox;
    @FXML
    private VBox selectionTrailBox;
    @FXML
    private Pane outerPane;
    @FXML
    private Label errorLabel;
    @FXML
    private ImageView trailConnectionMode;
    @FXML
    private ImageView trailGameMode;
    @FXML
    private Pane boatView;
    @FXML
    private Label arrowLeft;
    @FXML
    private Label arrowRight;

    private Stage stage;

    private StringProperty ipStrProp;
    private StringProperty portStrProp;

    private RaceMode mode;
    private Boolean isHosting;

    private final static double Y_POS_BOAT_VIEW = -200;
    private final static double Y_POS_SELECTION_BOX = -250;
    private final static double Y_POS_ERROR_TEXT = -150;
    private final static double Y_POS_BUTTON_BOX = -40;
    private final static double OPTION_BUTTONS_HEIGHT = 50;
    private final static double ARROW_Y_GAP = 5;
    private final static double BOAT_SIZE = 100;

    private Polyline boat;
    private int colourIndex = 0;
    private List<Color> boatColours = Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
            Color.CYAN, Color.BLUE, Color.PURPLE, Color.MAGENTA);

    @FXML
    public void initialize() {
        registerListeners();
        initialiseBoatPicker();
        setUpModeSelection();

        final double height = Y_POS_BUTTON_BOX - Y_POS_ERROR_TEXT;
        errorLabel.setMaxHeight(height);
        errorLabel.setMinHeight(height);
        errorLabel.setPrefHeight(height);

        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reDraw();
        }).start();
        reDraw();
    }


    /**
     * Register any necessary listeners.
     */
    private void registerListeners() {
        outerPane.widthProperty().addListener(((observable, oldValue, newValue) -> reDraw()));
        outerPane.heightProperty().addListener(((observable, oldValue, newValue) -> reDraw()));
    }


    /**
     * Re draw the the pane which holds elements of the play interface to be in the middle of the window.
     */
    void reDraw() {
        // selection trail
        if (selectionTrailBox.getWidth() != 0) {
            selectionTrailBox.setLayoutX((outerPane.getScene().getWidth() / 2) - (selectionTrailBox.getWidth() / 2));
            selectionTrailBox.setLayoutY((outerPane.getScene().getHeight() / 2) + Math.min(Y_POS_ERROR_TEXT - selectionTrailBox.getHeight(), Y_POS_SELECTION_BOX));
        } else if (stage != null) {
            selectionTrailBox.setLayoutX((stage.getWidth() / 2) - (selectionTrailBox.getPrefWidth() / 2));
            selectionTrailBox.setLayoutY((stage.getHeight() / 2) + Math.min(selectionTrailBox.getPrefHeight() + Y_POS_ERROR_TEXT, Y_POS_SELECTION_BOX));
        }

        // button box
        //noinspection Duplicates
        if (buttonBox.getWidth() != 0) {
            buttonBox.setLayoutX((outerPane.getScene().getWidth() / 2) - (buttonBox.getWidth() / 2));
            buttonBox.setLayoutY((outerPane.getScene().getHeight() / 2) + Y_POS_BUTTON_BOX);
        } else if (stage != null) {
            buttonBox.setLayoutX((stage.getWidth() / 2) - (buttonBox.getPrefWidth() / 2));
            buttonBox.setLayoutY((stage.getHeight() / 2) + Y_POS_BUTTON_BOX);
        }

        // error text
        //noinspection Duplicates
        if (errorLabel.getWidth() != 0) {
            errorLabel.setLayoutX((outerPane.getScene().getWidth() / 2) - (errorLabel.getWidth() / 2));
            errorLabel.setLayoutY((outerPane.getScene().getHeight() / 2) + Y_POS_ERROR_TEXT);
        } else if (stage != null) {
            errorLabel.setLayoutX((stage.getWidth() / 2) - (errorLabel.getPrefWidth() / 2));
            errorLabel.setLayoutY((stage.getHeight() / 2) + Y_POS_ERROR_TEXT);
        }

        if (boatView.getWidth() != 0) {
            boatView.setLayoutX((outerPane.getScene().getWidth() / 2) - buttonBox.getWidth() - (0.75 * BOAT_SIZE));
            boatView.setLayoutY((outerPane.getScene().getHeight() / 2) + Y_POS_BOAT_VIEW);
        } else if (stage != null) {
            boatView.setLayoutX((stage.getWidth() / 2) - (300 * 1.75));
            boatView.setLayoutX((stage.getHeight() / 2) - (225));
        }

        final double arrowWidth = 55;
        arrowLeft.setLayoutX(boatView.getLayoutX());
        arrowLeft.setLayoutY(boatView.getLayoutY() + boatView.getHeight() + ARROW_Y_GAP);
        arrowRight.setLayoutX(boatView.getLayoutX() + boatView.getWidth() - arrowWidth);
        arrowRight.setLayoutY(boatView.getLayoutY() + boatView.getHeight() + ARROW_Y_GAP);
    }


    private void setUpModeSelection() {
        setOptionButtons(Arrays.asList(
                createRaceButton(),
                createArcadeButton(),
                createChallengeButton(),
                createBumperBoatsButton(),
                createSpectatorButton()
        ));

        selectionTrailBox.getChildren().setAll(Arrays.asList(
                trailGameMode,
                trailConnectionMode
        ));

        reDraw();
    }


    private void setUpConnectionType(RaceMode mode) {
        this.mode = mode;

        selectionTrailBox.getChildren().setAll(Arrays.asList(
                getTrailGameMode(mode),
                trailConnectionMode
        ));

        // todo add thingy to upper box (transition)
        // .setOnClicked(null);

        setOptionButtons(Arrays.asList(
                createJoinButton(),
                createCreateButton()
        ));

        reDraw();
    }


    private void setUpConnectionOptions(boolean isHosting) {
        this.isHosting = isHosting;

        selectionTrailBox.getChildren().setAll(Arrays.asList(
                getTrailGameMode(mode),
                getTrailConnectionMode(isHosting)
        ));

        // todo transition

        setOptionButtons(Arrays.asList(
                createHostEntry(),
                createPlayButton()
        ));

        reDraw();
    }


    private ImageView getTrailGameMode(RaceMode mode) {
        String url = "/images/game_selection/game_mode.png";
        switch (mode) {
            case RACE:
                url = "images/game_selection/race_white.png";
                break;
            case ARCADE:
                url = "images/game_selection/arcade_white.png";
                break;
            case CHALLENGE_MODE:
                url = "images/game_selection/challenge_white.png";
                break;
            case BUMPER_BOATS:
                url = "images/game_selection/bumper_white.png";
                break;
            case SPECTATION:
                url = "images/game_selection/spectator_white.png";
                break;
        }

        return new ImageView(url);
    }


    private ImageView getTrailConnectionMode(boolean isHosting) {
        String url = (isHosting) ? "images/game_selection/create_game_white.png" : "images/game_selection/join_game_white.png";
        return new ImageView(url);
    }


    /**
     * Set up the button for starting a race.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private Labeled createRaceButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("raceImage");
        label.setOnMouseClicked(event -> setUpConnectionType(RaceMode.RACE));
        return label;
    }


    /**
     * Set up the button for starting an arcade race.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private Labeled createArcadeButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("arcadeImage");
        label.setOnMouseClicked(event -> setUpConnectionType(RaceMode.ARCADE));
        return label;
    }


    /**
     * Set up the button for starting a challenge mode game.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private Labeled createChallengeButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("challengeImage");
        label.setOnMouseClicked(event -> setUpConnectionType(RaceMode.CHALLENGE_MODE));
        return label;
    }


    /**
     * Set up the button for starting a bumper boats game.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private Labeled createBumperBoatsButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("bumperBoatsImage");
        label.setOnMouseClicked(event -> setUpConnectionType(RaceMode.BUMPER_BOATS));
        return label;
    }


    /**
     * Sets up the button for spectating an existing game,
     * Image used will changed when hovered over as defined in the playInterface css.
     */
    private Labeled createSpectatorButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("spectatorImage");
        label.setOnMouseClicked(event -> setUpConnectionType(RaceMode.SPECTATION));
        return label;
    }


    /**
     * Sets up the button for spectating an existing game,
     * Image used will changed when hovered over as defined in the playInterface css.
     */
    private Labeled createCreateButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("createImage");
        label.setOnMouseClicked(event -> setUpConnectionOptions(true));
        return label;
    }


    /**
     * Sets up the button for spectating an existing game,
     * Image used will changed when hovered over as defined in the playInterface css.
     */
    private Labeled createJoinButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("joinImage");
        label.setOnMouseClicked(event -> setUpConnectionOptions(false));
        return label;
    }


    private Region createHostEntry() {
        Control ipEntry = createIpField();
        Control portEntry = createPortField();
        AnchorPane pane = new AnchorPane();

        final double width = 300;
        final double height = 45;
        pane.setMinWidth(width);
        pane.setPrefWidth(width);
        pane.setMaxWidth(width);
        pane.setMinHeight(height);
        pane.setPrefHeight(height);
        pane.setMaxHeight(height);

        AnchorPane.setBottomAnchor(ipEntry, 0D);
        AnchorPane.setTopAnchor(ipEntry, 0D);
        AnchorPane.setLeftAnchor(ipEntry, 0D);

        AnchorPane.setBottomAnchor(portEntry, 0D);
        AnchorPane.setTopAnchor(portEntry, 0D);
        AnchorPane.setRightAnchor(portEntry, 0D);

        pane.getChildren().add(ipEntry);
        pane.getChildren().add(portEntry);

        return pane;
    }


    private boolean isIpFirstFocus = true;

    private Control createIpField() {
        TextField field = new TextField();
        field.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        field.getStyleClass().add("addressInput");
        field.setPromptText("host address");

        final int height = 45;
        final int width = 200;
        field.setMaxHeight(height);
        field.setPrefHeight(height);
        field.setMinHeight(height);
        field.setMaxWidth(width);
        field.setPrefWidth(width);
        field.setMinWidth(width);

        if (isHosting) {
            field.setText("127.0.0.1");
            field.setDisable(true);
        }

        ipStrProp = field.textProperty();

        isIpFirstFocus = true;
        field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (isIpFirstFocus && newValue) {
                outerPane.requestFocus();
                isIpFirstFocus = false;
            }
        });

        return field;
    }


    private Control createPortField() {
        final TextField field = new TextField("5005");
        field.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        field.getStyleClass().add("addressInput");
        field.setPromptText("port");

        final int height = 45;
        final int width = 95;
        field.setMaxHeight(height);
        field.setPrefHeight(height);
        field.setMinHeight(height);
        field.setMaxWidth(width);
        field.setPrefWidth(width);
        field.setMinWidth(width);

        portStrProp = field.textProperty();

        return field;
    }


    private Labeled createPlayButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("playImage");
        label.setOnMouseClicked(event -> startGame());
        return label;

    }


    private void startGame() {
        errorLabel.setText("");

        // Check ip
        String hostAddress = getHostAddress();
        int port = getPort();
        if (port < 0 || hostAddress == null) return;

        if (isHosting) {
            try {
                (new ModelLoader()).startModel(port);
            } catch (IOException e) {
                errorLabel.setText("Unable to initiate server!");
                e.printStackTrace();
                return;
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        openStream(hostAddress, port);
    }


    private String getHostAddress() {
        if (ipStrProp.get().isEmpty()) {
            errorLabel.setText(errorLabel.getText() + "\nPlease enter a valid host address!");
            return null;
        }

        return ipStrProp.get();
    }


    private int getPort() {
        int port = -1;
        try {
            port = Integer.parseInt(this.portStrProp.get());
            if (port < 1024 || port > 65535) {
                port = -1;
                errorLabel.setText(errorLabel.getText() + "\nPlease enter a port number in the range 1025-65534!");
            }
        } catch (NumberFormatException e) {
            errorLabel.setText(errorLabel.getText() + "\nPlease enter a valid port number!");
        }

        return port;
    }


    /**
     * Creates a controller manager object and begins an instance of the program.
     *
     * @throws Exception A connection error
     */
    @SuppressWarnings("Duplicates")
    private void startConnection(Receiver receiver, Sender sender) throws Exception {
        Stage stage = (Stage) outerPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PreRace.fxml"));
        Parent root = loader.load();
        PreRaceController controller = loader.getController();
        stage.setTitle("High Seas");
        outerPane.getScene().setRoot(root);
        stage.show();

        ClientRace race = new ClientRace();
        race.setMode(mode);
        controller.setUp(race, receiver, sender);
        controller.initConnection(boatColours.get(colourIndex));
    }


    /**
     * Opens a socket and connection on the given host and port number
     *
     * @param host The host IP address for the socket
     * @param port The port number used for the socket
     */
    private void openStream(String host, int port) {
        try {
            Socket socket = SocketFactory.getDefault().createSocket(host, port);
            startConnection(new Receiver(socket, new AC35MessageParserFactory()), new Sender(socket, new ControllerMessageFactory()));
        } catch (Exception e) {
            errorLabel.setText("Failed to connect to " + host + ":" + port + "!");
        }
    }


    /**
     * returns the player to the playInterface
     */
    @FXML
    private void backButtonAction() {
        if (mode == null) {
            exitSelectionScreen();

        } else if (isHosting == null) {
            // Go back to mode selection
            mode = null;
            setUpModeSelection();
        } else {
            // go back to hosting selection
            isHosting = null;
            setUpConnectionType(mode);
        }
    }


    @SuppressWarnings("Duplicates")
    private void exitSelectionScreen() {
        Stage stage = (Stage) outerPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PlayInterface.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("Error occurred loading play interface screen");
        }
        PlayInterfaceController controller = loader.getController();
        controller.setStage(stage);
        stage.setTitle("High Seas");
        outerPane.getScene().setRoot(root);
        stage.setMaximized(true);
        stage.show();
    }


    /**
     * Creates buttons for left and right selection and sets up a preview of the boat with the current colour.
     */
    private void initialiseBoatPicker() {
        Double[] boatShape = new Double[]{
                0.0, BOAT_SIZE / -2,
                BOAT_SIZE / -2, BOAT_SIZE / 2,
                BOAT_SIZE / 2, BOAT_SIZE / 2,
                0.0, BOAT_SIZE / -2
        };

        boat = new Polyline();
        boat.getPoints().addAll(boatShape);
        boat.setRotate(90);
        boat.setFill(boatColours.get(colourIndex));
        boat.setStrokeWidth(BOAT_SIZE / 40);
        boatView.getChildren().add(boat);
        boatView.setPadding(new Insets(BOAT_SIZE / 8));
        boat.toFront();
    }


    /**
     * Display the next boat colour option.
     */
    @FXML
    private void rightButtonAction() {
        colourIndex = (colourIndex + 1) % boatColours.size();
        boat.setFill(boatColours.get(colourIndex));
    }


    /**
     * Display the previous boat colour option.
     */
    @FXML
    private void leftButtonAction() {
        colourIndex = Math.floorMod((colourIndex - 1), boatColours.size());
        boat.setFill(boatColours.get(colourIndex));
    }


    void setStage(Stage stage) {
        this.stage = stage;
    }


    void setColour(Color colour) {
    }


    private void setOptionButtons(List<Region> buttons) {
        double x = optionsBox.getLayoutX();
        double y = optionsBox.getLayoutY();

        optionsBox.getChildren().setAll(buttons);

        for (Region button : buttons) {
            button.setLayoutX(x);
            button.setLayoutY(y);

            y += OPTION_BUTTONS_HEIGHT;
        }
    }
}
