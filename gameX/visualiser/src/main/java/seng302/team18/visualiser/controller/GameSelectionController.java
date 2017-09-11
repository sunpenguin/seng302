package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
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
import java.util.List;

/**
 * Controller For the game type selection screen
 */
public class GameSelectionController {

    @FXML
    private Pane outerPane;
    @FXML
    private Pane innerPane;
    private Stage stage;

    private Label spectatorLabel;
    private RadioButton localHost;
    private RadioButton remoteHost;
    private ToggleGroup hostOption;
    private TextField ip;
    private TextField port;
    private Label errorText;

    private RaceMode mode;

    private int colourIndex = 0;
    private List<Color> boatColours = Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
            Color.CYAN, Color.BLUE, Color.PURPLE, Color.MAGENTA);


    @FXML
    public void initialize() {
        initialiseHostPort();
        initialiseHostChoice();
        initialiseRaceButton();
        initialiseErrorText();
        initialiseArcadeButton();
        initialiseSpyroButton();
        initialiseBumperBoatsButton();
        initialiseSpectatorButton();
        initialiseBackButton();
        registerListeners();
    }


    private void initialiseHostPort() {
        int height = 45;
        int ipWidth = 200;
        int gap = 5;
        int portWidth = 95;


        ip = new TextField();
        ip.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        ip.getStyleClass().add("addressInput");
        ip.setPromptText("host address");
        ip.setMaxHeight(height);
        ip.setPrefHeight(height);
        ip.setMinHeight(height);
        ip.setMaxWidth(ipWidth);
        ip.setPrefWidth(ipWidth);
        ip.setMinWidth(ipWidth);
        innerPane.getChildren().add(ip);
        ip.setLayoutX((600 / 2) - ((ipWidth + gap + portWidth) / 2));
        ip.setLayoutY((600 / 2) - 250);

        port = new TextField("5005");
        port.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        port.getStyleClass().add("addressInput");
        port.setPromptText("port");
        port.setMaxHeight(height);
        port.setPrefHeight(height);
        port.setMinHeight(height);
        port.setMaxWidth(portWidth);
        port.setPrefWidth(portWidth);
        port.setMinWidth(portWidth);
        innerPane.getChildren().add(port);
        port.setLayoutX((600 / 2) + ipWidth + gap - ((ipWidth + gap + portWidth) / 2));
        port.setLayoutY((600 / 2) - 250);
    }


    private void initialiseHostChoice() {
        localHost = new RadioButton();
        localHost.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        localHost.getStyleClass().add("localHost");
        innerPane.getChildren().add(localHost);

        Image localHostImage = new Image("/images/game_selection/RaceWhite.png");
        localHost.setLayoutX((600 / 2) - (Math.floorDiv((int) localHostImage.getWidth(), 2)) - 13);
        localHost.setLayoutY((600 / 2) - 150);

        remoteHost = new RadioButton();
        remoteHost.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        remoteHost.getStyleClass().add("remoteHost");
        innerPane.getChildren().add(remoteHost);

        Image remoteHostImage = new Image("/images/game_selection/RaceWhite.png");
        remoteHost.setLayoutX((600 / 2) - (Math.floorDiv((int) remoteHostImage.getWidth(), 2)) - 13);
        remoteHost.setLayoutY((600 / 2) - 200);

        hostOption = new ToggleGroup();
        hostOption.getToggles().addAll(localHost, remoteHost);
    }


    private void initialiseErrorText() {
        int height = 95;
        int width = 300;
        errorText = new Label();
        errorText.setWrapText(true);
        errorText.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        errorText.getStyleClass().add("errorText");
        errorText.setMaxHeight(height);
        errorText.setPrefHeight(height);
        errorText.setMinHeight(height);
        errorText.setMaxWidth(width);
        errorText.setPrefWidth(width);
        errorText.setMinWidth(width);
        innerPane.getChildren().add(errorText);
        errorText.setLayoutX((600 / 2) - (width / 2));
        errorText.setLayoutY((600 / 2) - 100);
    }


    private boolean isRemote() {
        return isRemote(hostOption.getSelectedToggle());
    }

    private boolean isRemote(Toggle toggle) {
        return toggle == remoteHost;
    }


    /**
     * Register any necessary listeners.
     */
    private void registerListeners() {
        outerPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> reDraw());
        outerPane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> reDraw());

        hostOption.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            boolean isRemote = isRemote(newValue);
            if (!isRemote) {
                ip.setText("127.0.0.1");
            }
            ip.setEditable(isRemote);
        });
        hostOption.selectToggle(remoteHost);
    }


    /**
     * Re draw the the pane which holds elements of the play interface to be in the middle of the window.
     */
    void reDraw() {
        if (!(innerPane.getWidth() == 0)) {
            innerPane.setLayoutX((outerPane.getScene().getWidth() / 2) - (innerPane.getWidth() / 2));
            innerPane.setLayoutY((innerPane.getScene().getHeight() / 2) - (innerPane.getHeight() / 2));
        } else if (stage != null) {
            innerPane.setLayoutX((stage.getWidth() / 2) - (innerPane.getPrefWidth() / 2));
            innerPane.setLayoutY((stage.getHeight() / 2) - (innerPane.getPrefHeight() / 2));
        }
    }


    /**
     * Set up the button for starting a race.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseRaceButton() {
        Label raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        raceLabel.getStyleClass().add("raceImage");
        innerPane.getChildren().add(raceLabel);

        Image raceButtonImage = new Image("/images/game_selection/RaceWhite.png");
        raceLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((600 / 2));
        raceLabel.setOnMouseClicked(event -> startGame(RaceMode.RACE));
    }


    /**
     * Set up the button for starting an arcade race.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseArcadeButton() {
        Label raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        raceLabel.getStyleClass().add("arcadeImage");
        innerPane.getChildren().add(raceLabel);

        Image raceButtonImage = new Image("/images/game_selection/Arcade_Race_White.png");
        raceLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((600 / 2) + 50);
        raceLabel.setOnMouseClicked(event -> startGame(RaceMode.ARCADE));
    }


    /**
     * Set up the button for starting a challenge mode game.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseSpyroButton() {
        Label raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        raceLabel.getStyleClass().add("spyroImage");
        innerPane.getChildren().add(raceLabel);

        Image raceButtonImage = new Image("/images/game_selection/SpyroWhite.png");
        raceLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((600 / 2) + 100);
        raceLabel.setOnMouseClicked(event -> startGame(RaceMode.CHALLENGE_MODE));
    }


    /**
     * Set up the button for starting a bumper boats game.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseBumperBoatsButton() {
        Label raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        raceLabel.getStyleClass().add("bumperBoatsImage");
        innerPane.getChildren().add(raceLabel);

        Image raceButtonImage = new Image("/images/game_selection/bumperBoatsWhite.png");
        raceLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((600 / 2) + 150);
        raceLabel.setOnMouseClicked(event -> startGame(RaceMode.BUMPER_BOATS));
    }


    /**
     * Sets up the button for spectating an existing game,
     * Image used will changed when hovered over as defined in the playInterface css.
     */
    private void initialiseSpectatorButton() {
        spectatorLabel = new Label();
        spectatorLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        spectatorLabel.getStyleClass().add("spectatorImage");
        innerPane.getChildren().add(spectatorLabel);

        Image spectatorImage = new Image("/images/game_selection/spectator_button.png");
        spectatorLabel.setLayoutX((innerPane.getPrefWidth() / 2) - (Math.floorDiv((int) spectatorImage.getWidth(), 2)));
        spectatorLabel.setLayoutY((innerPane.getPrefHeight() / 2) + 200);
        spectatorLabel.setOnMouseClicked(event -> startGame(RaceMode.SPECTATION));
    }


    /**
     * Set up the button for getting back to the title screen.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseBackButton() {
        Label raceLabel = new Label();
        raceLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/gameSelection.css").toExternalForm());
        raceLabel.getStyleClass().add("backButtonImage");
        innerPane.getChildren().add(raceLabel);

        Image raceButtonImage = new Image("/images/back_button.gif");
        raceLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) raceButtonImage.getWidth(), 2)));
        raceLabel.setLayoutY((600 / 2) + 275);
        raceLabel.setOnMouseClicked(event -> backButtonAction());
    }


    /**
     * Creates a controller manager object and begins an instance of the program.
     *
     * @throws Exception A connection error
     */
    @SuppressWarnings("Duplicates")
    private void startConnection(Receiver receiver, Sender sender) throws Exception {
        Stage stage = (Stage) innerPane.getScene().getWindow();
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
            errorText.setText("Failed to connect to " + host + ":" + port);
        }
    }


    /**
     * returns the player to the playInterface
     */
    @SuppressWarnings("Duplicates")
    private void backButtonAction() {
        Stage stage = (Stage) innerPane.getScene().getWindow();
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
     * Act on user pressing the host new game button.
     * Starts and arcade game
     */
    private void startGame(RaceMode raceMode) {
        int port;
        try {
            port = Integer.parseInt(this.port.getText());
        } catch (NumberFormatException e) {
            errorText.setText("Please enter a valid port number");
            return;
        }

        mode = raceMode;
        if (!isRemote()) {
            try {
                (new ModelLoader()).startModel(port);
            } catch (IOException e) {
                errorText.setText("Unable to initiate server!");
                e.printStackTrace();
                return;
            }
        }

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        openStream(ip.getText(), port);
    }


    void setStage(Stage stage) {
        this.stage = stage;
    }


    void setColourIndex(int colourIndex) {
        this.colourIndex = colourIndex;
    }
}
