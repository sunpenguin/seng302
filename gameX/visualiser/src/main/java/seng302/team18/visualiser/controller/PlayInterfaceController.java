package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;
import javafx.stage.Stage;
import seng302.team18.message.MessageBody;
import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.messageparsing.Receiver;
import seng302.team18.model.Race;
import seng302.team18.model.RaceMode;
import seng302.team18.send.ControllerMessageFactory;
import seng302.team18.send.Sender;
import seng302.team18.visualiser.util.ConfigReader;

import javax.net.SocketFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by jth102 on 16/08/17.
 */
public class PlayInterfaceController {

    @FXML private Pane innerPane;
    @FXML private Pane outerPane;
    private Stage stage;
    private Label hostLabel;
    private Label tutorialLabel;
    private Label backLabel;
    private Image hostImage;
    private Image tutorialImage;
    private Image backImage;

    private List<MessageBody> customisationMessages;

    private Image leftImage;
    private Image rightImage;
    private Label leftLabel;
    private Label rightLabel;
    private RaceMode mode;

    private Polyline boat;

    private int colourIndex = 0;
    private List<Color> boatColours = Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                                                    Color.CYAN, Color.BLUE, Color.PURPLE, Color.MAGENTA);


    @FXML
    public void initialize() {
        customisationMessages = new ArrayList<>();
        registerListeners();
        initialiseTutorialButton();
        initialiseHostButton();
        initialiseBackButton();
        initialiseBoatPicker();
    }


    /**
     * Set up the button for hosting a new game.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseHostButton() {
        hostLabel = new Label();
        hostLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/playInterface.css").toExternalForm());
        hostLabel.getStyleClass().add("hostImage");
        innerPane.getChildren().add(hostLabel);

        hostImage = new Image("/images/playInterface/host_button.gif");
        hostLabel.setLayoutX((innerPane.getPrefWidth() / 2) - (Math.floorDiv((int) hostImage.getWidth(), 2)));
        hostLabel.setLayoutY((innerPane.getPrefHeight() / 2) + 100);
        hostLabel.setOnMouseClicked(event -> hostButtonAction());
    }


    /**
     * Set up the button for getting back to the title screen.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseBackButton() {
        backLabel = new Label();
        backLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/style.css").toExternalForm());
        backLabel.getStyleClass().add("backImage");
        innerPane.getChildren().add(backLabel);

        backImage= new Image("/images/back_button.gif");
        backLabel.setLayoutX((innerPane.getPrefWidth() / 2) - (Math.floorDiv((int) backImage.getWidth(), 2)));
        backLabel.setLayoutY((innerPane.getPrefHeight() / 2) + 200);
        backLabel.setOnMouseClicked(event -> backButtonAction());
    }


    /**
     * Set up the button for hosting a new game.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseTutorialButton() {
        tutorialLabel = new Label();
        tutorialLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/playInterface.css").toExternalForm());
        tutorialLabel.getStyleClass().add("tutorialImage");
        innerPane.getChildren().add(tutorialLabel);

        tutorialImage= new Image("/images/playInterface/tutorial_button.gif");
        tutorialLabel.setLayoutX((innerPane.getPrefWidth() / 2) - (Math.floorDiv((int) tutorialImage.getWidth(), 2)));
        tutorialLabel.setLayoutY((innerPane.getPrefHeight() / 2) + 150);
        tutorialLabel.setOnMouseClicked(event -> tutorialButtonAction());
    }


    /**
     * Creates buttons for left and right selection and sets up a preview of the boat with the current colour.
     */
    private void initialiseBoatPicker() {
        rightLabel = new Label();
        rightLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/playInterface.css").toExternalForm());
        rightLabel.getStyleClass().add("rightImage");
        innerPane.getChildren().add(rightLabel);

        rightImage= new Image("/images/playInterface/tutorial_button.gif");
        rightLabel.setLayoutX((innerPane.getPrefWidth() / 2) + 50);
        rightLabel.setLayoutY((innerPane.getPrefHeight() / 2) - 80);
        rightLabel.setOnMouseClicked(event -> rightButtonAction());


        leftLabel = new Label();
        leftLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/playInterface.css").toExternalForm());
        leftLabel.getStyleClass().add("leftImage");
        innerPane.getChildren().add(leftLabel);

        leftImage = new Image("/images/playInterface/tutorial_button.gif");
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

        double boatHeight = hostImage.getWidth() / 2.0;
        double boatWidth = hostImage.getWidth() / 2.0;

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
        colourIndex =  Math.floorMod((colourIndex - 1), boatColours.size());
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
    private void hostButtonAction() {
        createModel();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mode = RaceMode.RACE;
        openStream("127.0.0.1", 5005);
    }


    /**
     * Act on user pressing the host new game button.
     */
    private void tutorialButtonAction() {
        createModel();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mode = RaceMode.CONTROLS_TUTORIAL;
        openStream("127.0.0.1", 5005);
    }


    private void openStream(String host, int port) {
        try {
            Socket socket = SocketFactory.getDefault().createSocket(host, port);
            startConnection(new Receiver(socket, new AC35MessageParserFactory()), new Sender(socket, new ControllerMessageFactory()));
        } catch (Exception e) {
            e.printStackTrace();
            //errorText.setText(String.format("Could not establish connection to stream at: %s:%d", host, port));
        }
    }


    /**
     * Creates a controller manager object and begins an instance of the program.
     *
     * @throws Exception A connection error
     */
    private void startConnection(Receiver receiver, Sender sender) throws Exception {
        Stage stage = (Stage) innerPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PreRace.fxml"));
        Parent root = loader.load();
        PreRaceController controller = loader.getController();
        controller.setStage(stage);
        stage.setTitle("High Seas");
        outerPane.getScene().setRoot(root);
        stage.show();

        Race race = new Race();
        race.setMode(mode);
        controller.setUp(race, receiver, sender);
        controller.initConnection(new ArrayList<>());
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
    public void reDraw() {
        if (!(innerPane.getWidth() == 0)) {
            innerPane.setLayoutX((outerPane.getScene().getWidth() / 2) - (innerPane.getWidth() / 2));
            innerPane.setLayoutY((innerPane.getScene().getHeight() / 2) - (innerPane.getHeight() / 2));
        }

        else if (stage != null) {
            innerPane.setLayoutX((stage.getWidth() / 2) - (400));
            innerPane.setLayoutY((stage.getHeight() / 2) - (400));
        }
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }


    //@FXML
    private void setColor() { //TODO FINSIH SAM DAVID 16 AUG
//        customisationMessages.add(new ColourMessage());
    }

    //@FXML
    private void setName() { //TODO FINSIH SAM DAVID 16 AUG
//        customisationMessages.add(new NameMessage());
    }


    /**
     * Creates the model in a new process.
     * Reads in the file path to the model jar from the config file "visualiser-config.txt" (from the same directory).
     */
    private void createModel() {
        final String CONFIG_FILE_NAME = "visualiser-config.txt";
        final List<String> tokens = Collections.singletonList("MODEL_PATH");
        ConfigReader reader = new ConfigReader(tokens);
        InputStream configStream = null;
        try {
            configStream = new FileInputStream(CONFIG_FILE_NAME);
            String filePath = reader.parseConfig(configStream).get("MODEL_PATH");
            Runtime.getRuntime().exec("java -jar " + filePath);
        } catch (IOException e) {
            if (null == configStream) {
                System.out.println("You don't have a config file"); // TODO August 12 DHL25 / HQI19 have to show error but title screen incomplete
            } else {
                e.printStackTrace();
            }
        }
    }



}
