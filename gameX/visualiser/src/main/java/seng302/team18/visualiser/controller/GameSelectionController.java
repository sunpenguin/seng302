package seng302.team18.visualiser.controller;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

/**
 * Controller For the game type selection screen
 */
public class GameSelectionController {

    @FXML
    private VBox buttonBox;
    @FXML
    private VBox optionsBox;
    @FXML
    private VBox selectionTrailBox;
    @FXML
    private Pane outerPane;
    @FXML
    private Label errorLabel;

    private Stage stage;

    private StringProperty ipStrProp;
    private StringProperty portStrProp;

    private RaceMode mode;
    private boolean isHosting;
    private Color boatColour;

    private final static double yPosSelectionBox = -250;
    private final static double yPosErrorText = -100;
    private final static double yPosButtonBox = 50;


    @FXML
    public void initialize() {
        registerListeners();
        setUpModeSelection();

        final double height = yPosButtonBox - yPosErrorText;
        errorLabel.setMaxHeight(height);
        errorLabel.setMinHeight(height);
        errorLabel.setPrefHeight(height);

        new Thread(() -> {
            try {
                Thread.sleep(35);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reDraw();
        }).start();
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
            selectionTrailBox.setLayoutY((outerPane.getScene().getHeight() / 2) + Math.min(selectionTrailBox.getHeight() + yPosErrorText, yPosSelectionBox));
        } else if (stage != null) {
            selectionTrailBox.setLayoutX((stage.getWidth() / 2) - (selectionTrailBox.getPrefWidth() / 2));
            selectionTrailBox.setLayoutY((stage.getHeight() / 2) + Math.min(selectionTrailBox.getPrefHeight() + yPosErrorText, yPosSelectionBox));
        }

        // button box
        //noinspection Duplicates
        if (buttonBox.getWidth() != 0) {
            buttonBox.setLayoutX((outerPane.getScene().getWidth() / 2) - (buttonBox.getWidth() / 2));
            buttonBox.setLayoutY((outerPane.getScene().getHeight() / 2) + yPosButtonBox);
        } else if (stage != null) {
            buttonBox.setLayoutX((stage.getWidth() / 2) - (buttonBox.getPrefWidth() / 2));
            buttonBox.setLayoutY((stage.getHeight() / 2) + yPosButtonBox);
        }

        // error text
        //noinspection Duplicates
        if (errorLabel.getWidth() != 0) {
            errorLabel.setLayoutX((outerPane.getScene().getWidth() / 2) - (errorLabel.getWidth() / 2));
            errorLabel.setLayoutY((outerPane.getScene().getHeight() / 2) + yPosErrorText);
        } else if (stage != null) {
            errorLabel.setLayoutX((stage.getWidth() / 2) - (errorLabel.getPrefWidth() / 2));
            errorLabel.setLayoutY((stage.getHeight() / 2) + yPosErrorText);
        }
    }


    private void setUpModeSelection() {
        optionsBox.getChildren().clear();
        optionsBox.getChildren().addAll(Arrays.asList(
                createRaceButton(),
                createArcadeButton(),
                createChallengeButton(),
                createBumperBoatsButton(),
                createSpectatorButton()
        ));

        reDraw();
    }


    private void setUpConnectionType(RaceMode mode) {
        this.mode = mode;

        // todo add thingy to upper box (transition)
        // .setOnClicked(null);

        optionsBox.getChildren().clear();
        optionsBox.getChildren().addAll(Arrays.asList(
                createJoinButton(),
                createCreateButton()
        ));

        reDraw();
    }


    private void setUpConnectionOptions(boolean isHosting) {
        this.isHosting = isHosting;

        // todo transition

        optionsBox.getChildren().clear();
        optionsBox.getChildren().addAll(Arrays.asList(
                createHostEntry(),
                createPlayButton()
        ));

        reDraw();
    }


    /**
     * Set up the button for starting a race.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private Node createRaceButton() {
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
    private Node createArcadeButton() {
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
    private Node createChallengeButton() {
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
    private Node createBumperBoatsButton() {
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
    private Node createSpectatorButton() {
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
    private Node createCreateButton() {
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
    private Node createJoinButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("joinImage");
        label.setOnMouseClicked(event -> setUpConnectionOptions(false));
        return label;
    }


    private Node createHostEntry() {
        Node ipEntry = createIpField();
        Node portEntry = createPortField();
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


    private Node createIpField() {
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

        return field;
    }


    private Node createPortField() {
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


    private Node createPlayButton() {
        Label label = new Label();
        label.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        label.getStyleClass().add("playImage");
        label.setOnMouseClicked(event -> startGame());
        return label;

    }


    private void startGame() {
        // Check ip
        if (ipStrProp.get().isEmpty()) {
            errorLabel.setText("Please enter a valid host address");
            return;
        }

        // Check port
        int port;
        try {
            port = Integer.parseInt(this.portStrProp.get());
        } catch (NumberFormatException e) {
            errorLabel.setText("Please enter a valid port number");
            return;
        }

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

        openStream(ipStrProp.get(), port);
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
        controller.initConnection(boatColour);
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
            errorLabel.setText("Failed to connect to " + host + ":" + port);
        }
    }


    /**
     * returns the player to the playInterface
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void backButtonAction() {
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


    void setStage(Stage stage) {
        this.stage = stage;
    }


    void setColour(Color colour) {
        this.boatColour = colour;
    }
}
