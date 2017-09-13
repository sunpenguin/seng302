package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
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
 * Created by jth102 on 16/08/17.
 */
public class PlayInterfaceController {

    @FXML
    private Pane innerPane;
    @FXML
    private Pane outerPane;
    private Stage stage;
    private Image playImage;
    private Image tutorialImage;

    @FXML
    private Label errorText;

    private RaceMode mode;

    private Polyline boat;

    private int colourIndex = 0;
    private List<Color> boatColours = Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
            Color.CYAN, Color.BLUE, Color.PURPLE, Color.MAGENTA);


    @FXML
    public void initialize() {
        registerListeners();
        initialiseHostPort();
        initialiseHostChoice();
        initialiseTutorialButton();
        initialisePlayButton();
        initialiseBackButton();
        initialiseBoatPicker();
    }


    private void initialiseHostPort() {

    }


    private void initialiseHostChoice() {

    }


    /**
     * Set up the button for hosting a new game.
     * Image used will changed when hovered over as defined in the playInterface css.
     */
    private void initialisePlayButton() {
        Label playLabel = new Label();
        playLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/playInterface.css").toExternalForm());
        playLabel.getStyleClass().add("playImage");
        innerPane.getChildren().add(playLabel);

        playImage = new Image("/images/play_button.png");
        playLabel.setLayoutX((innerPane.getPrefWidth() / 2) - (Math.floorDiv((int) playImage.getWidth(), 2)));
        playLabel.setLayoutY((innerPane.getPrefHeight() / 2 + 100));
        playLabel.setOnMouseClicked(event -> playButtonAction());
    }


    /**
     * Set up the button for getting back to the title screen.
     * Image used will changed when hovered over as defined in the playInterface css.
     */
    private void initialiseBackButton() {
        Label backLabel = new Label();
        backLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/style.css").toExternalForm());
        backLabel.getStyleClass().add("backImage");
        innerPane.getChildren().add(backLabel);

        Image backImage = new Image("/images/back_button.gif");
        backLabel.setLayoutX((innerPane.getPrefWidth() / 2) - (Math.floorDiv((int) backImage.getWidth(), 2)));
        backLabel.setLayoutY((innerPane.getPrefHeight() / 2) + 225);
        backLabel.setOnMouseClicked(event -> backButtonAction());
    }


    /**
     * Set up the button for hosting a new game.
     * Image used will changed when hovered over as defined in the playInterface css.
     */
    private void initialiseTutorialButton() {
        Label tutorialLabel = new Label();
        tutorialLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/playInterface.css").toExternalForm());
        tutorialLabel.getStyleClass().add("tutorialImage");
        innerPane.getChildren().add(tutorialLabel);

        tutorialImage = new Image("/images/playInterface/tutorial_button.gif");
        tutorialLabel.setLayoutX((innerPane.getPrefWidth() / 2) - (Math.floorDiv((int) tutorialImage.getWidth(), 2)));
        tutorialLabel.setLayoutY((innerPane.getPrefHeight() / 2) + 150);
        tutorialLabel.setOnMouseClicked(event -> tutorialButtonAction());
    }


    /**
     * Creates buttons for left and right selection and sets up a preview of the boat with the current colour.
     */
    private void initialiseBoatPicker() {
        Label rightLabel = new Label();
        rightLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/playInterface.css").toExternalForm());
        rightLabel.getStyleClass().add("rightImage");
        innerPane.getChildren().add(rightLabel);

        new Image("/images/playInterface/tutorial_button.gif");
        rightLabel.setLayoutX((innerPane.getPrefWidth() / 2) + 50);
        rightLabel.setLayoutY((innerPane.getPrefHeight() / 2) - 80);
        rightLabel.setOnMouseClicked(event -> rightButtonAction());


        Label leftLabel = new Label();
        leftLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/playInterface.css").toExternalForm());
        leftLabel.getStyleClass().add("leftImage");
        innerPane.getChildren().add(leftLabel);

        new Image("/images/playInterface/tutorial_button.gif");
        leftLabel.setLayoutX((innerPane.getPrefWidth() / 2) - 150);
        leftLabel.setLayoutY((innerPane.getPrefHeight() / 2) - 80);
        leftLabel.setOnMouseClicked(event -> leftButtonAction());


        Label boatView = new Label();
        boatView.getStylesheets().add(this.getClass().getResource("/stylesheets/playInterface.css").toExternalForm());
        boatView.getStyleClass().add("borderImage");
        Image borderImage = new Image("/images/playInterface/border.png");
        innerPane.getChildren().add(boatView);
        boatView.setLayoutX((innerPane.getPrefWidth() / 2) - (Math.floorDiv((int) tutorialImage.getWidth(), 2)));
        boatView.setLayoutY(0);

        double boatHeight = playImage.getWidth() / 2.0;
        double boatWidth = playImage.getWidth() / 2.0;

        Double[] boatShape = new Double[]{
                0.0, boatHeight / -2,
                boatWidth / -2, boatHeight / 2,
                boatWidth / 2, boatHeight / 2,
                0.0, boatHeight / -2
        };

        boat = new Polyline();
        boat.getPoints().addAll(boatShape);
        boat.setRotate(90);
        boat.setLayoutX(boatView.getLayoutX() + (borderImage.getWidth() / 2.0));
        boat.setLayoutY(boatView.getLayoutY() + (borderImage.getHeight() / 2.0));
        boat.setFill(boatColours.get(colourIndex));

        innerPane.getChildren().add(boat);
    }


    /**
     * Display the next boat colour option.
     */
    private void rightButtonAction() {
        colourIndex = (colourIndex + 1) % boatColours.size();
        boat.setFill(boatColours.get(colourIndex));
    }


    /**
     * Display the previous boat colour option.
     */
    private void leftButtonAction() {
        colourIndex = Math.floorMod((colourIndex - 1), boatColours.size());
        boat.setFill(boatColours.get(colourIndex));
    }


    /**
     * Act on user pressing the back button.
     * Return the user to the title screen.
     */
    private void backButtonAction() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("StartupInterface.fxml"));
        Parent root = null; // throws IOException
        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("Error occurred loading title screen.");
        }
        TitleScreenController controller = loader.getController();
        controller.setStage(stage);
        outerPane.getScene().setRoot(root);
        stage.setMaximized(true);
        stage.show();
    }


    /**
     * Act on user pressing the host new game button.
     */
    private void playButtonAction() {
        try {
            toGameModeSelection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Act on user pressing the tutorial game button.
     */
    private void tutorialButtonAction() {
        final String host = "127.0.0.1";
        final int port = 5010;

        createModel(port);
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mode = RaceMode.CONTROLS_TUTORIAL;

        try {
            Socket socket = SocketFactory.getDefault().createSocket(host, port);
            startConnection(new Receiver(socket, new AC35MessageParserFactory()), new Sender(socket, new ControllerMessageFactory()));
        } catch (Exception e) {
            errorText.setText("Unable to connect to server on port " + port + '\n' +
                    "Please ensure this port is free for the server to bind to");
        }
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
     * Takes the player to the screen where the game mode to be hosted can be selected
     *
     * @throws IOException Thrown if there is an issue opening the fxml and loading the controller
     */
    private void toGameModeSelection() throws IOException {
        Stage stage = (Stage) innerPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ModeSelection.fxml"));
        Parent root = loader.load();
        GameSelectionController controller = loader.getController();
        controller.setColour(boatColours.get(colourIndex));
        controller.reDraw();
        controller.setStage(stage);
        stage.setTitle("High Seas");
        outerPane.getScene().setRoot(root);
        stage.show();
    }


    /**
     * Register any necessary listeners.
     */
    private void registerListeners() {
        outerPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> reDraw());
        outerPane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> reDraw());
    }


    /**
     * Re draw the the pane which holds elements of the play interface to be in the middle of the window.
     */
    private void reDraw() {
        if (!(innerPane.getWidth() == 0)) {
            innerPane.setLayoutX((outerPane.getScene().getWidth() / 2) - (innerPane.getWidth() / 2));
            innerPane.setLayoutY((innerPane.getScene().getHeight() / 2) - (innerPane.getHeight() / 2));
        } else if (stage != null) {
            innerPane.setLayoutX((stage.getWidth() / 2) - (400));
            innerPane.setLayoutY((stage.getHeight() / 2) - (400));
        }
    }


    void setStage(Stage stage) {
        this.stage = stage;
    }


    /**
     * Creates the model in a new process.
     */
    private void createModel(int port) {
        try {
            (new ModelLoader()).startModel(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
