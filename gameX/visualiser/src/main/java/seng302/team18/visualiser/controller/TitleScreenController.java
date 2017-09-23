package seng302.team18.visualiser.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import seng302.team18.model.RaceMode;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Controller for when the application first starts up
 */
public class TitleScreenController {
    @FXML
    private Label errorText;
    @FXML
    private AnchorPane pane;
    @FXML
    private AnchorPane paneInner;

    private ImageView controlsImageView;
    private boolean controlsVisible = false;

    private Stage stage;


    public void initialize()  {
        registerListeners();
        initialiseHostButton();
        initialiseControlsButton();
        initialiseQuitButton();
        initialiseTutorialButton();
        loadBoatAnimation();

        try {
            String song = "/Users/cslaven/Desktop/Uni/302/team-18/gameX/visualiser/src/main/resources/themetune.wav";
            Media hit = new Media(new File(song).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        errorText.setLayoutX((600 / 2) - errorText.getPrefWidth());
        errorText.setLayoutY((600 / 2) + 300);
    }


    /**
     * Called when users selects "Play" button.
     * Takes user to PlayInterface.
     */
    private void toPlayScreen() {
        Stage stage = (Stage) errorText.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ModeSelection.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("Error occurred loading play interface screen");
        }
        GameSelectionController controller = loader.getController();
        controller.setStage(stage);
        stage.setTitle("High Seas");
        pane.getScene().setRoot(root);
        stage.setMaximized(true);
        stage.show();

        new Thread(() -> {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(controller::reDraw);
        }).start();
    }


    /**
     * Register any necessary listeners.
     */
    private void registerListeners() {
        pane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> reDraw());
        pane.heightProperty().addListener((observableValue, oldHeight, newHeight) -> reDraw());
    }


    /**
     * Set up the button for hosting a new game.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseHostButton() {
        Label hostLabel = new Label();
        hostLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/titleScreen.css").toExternalForm());
        hostLabel.getStyleClass().add("hostImage");
        paneInner.getChildren().add(hostLabel);

        Image hostButtonImage = new Image("/images/play_button.png");
        hostLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) hostButtonImage.getWidth(), 2)));
        hostLabel.setLayoutY((600 / 2) + 100);
        hostLabel.setOnMouseClicked(event -> toPlayScreen());
    }


    /**
     * Set up the button for viewing the controls
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseControlsButton() {
        Label controlsLabel = new Label();
        controlsLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/titleScreen.css").toExternalForm());
        controlsLabel.getStyleClass().add("controlsImage");
        paneInner.getChildren().add(controlsLabel);

        Image controlsButtonImage = new Image("/images/title_screen/view_controls_button.png");
        controlsLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) controlsButtonImage.getWidth(), 2)));
        controlsLabel.setLayoutY((600 / 2) + 150);
        controlsLabel.setOnMouseClicked(event -> toggleControlsView());
    }


    /**
     * Set up the button for quitting the app.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseQuitButton() {
        Label quitLabel = new Label();
        quitLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/titleScreen.css").toExternalForm());
        quitLabel.getStyleClass().add("quitImage");
        paneInner.getChildren().add(quitLabel);

        Image quitButtonImage = new Image("/images/title_screen/quit_button.png");
        quitLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) quitButtonImage.getWidth(), 2)));
        quitLabel.setLayoutY((600 / 2) + 250);
        quitLabel.setOnMouseClicked(event -> System.exit(0));
    }


    /**
     * Set up the button for hosting a new game.
     * Image used will changed when hovered over as defined in the playInterface css.
     */
    private void initialiseTutorialButton() {
        Label tutorialLabel = new Label();
        tutorialLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/titleScreen.css").toExternalForm());
        tutorialLabel.getStyleClass().add("tutorialImage");
        paneInner.getChildren().add(tutorialLabel);

        Image tutorialImage = new Image("/images/title_screen/tutorial_button.gif");
        tutorialLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) tutorialImage.getWidth(), 2)));
        tutorialLabel.setLayoutY((600 / 2) + 200);
        tutorialLabel.setOnMouseClicked(event ->
                new GameConnection(
                        errorText.textProperty(),
                        paneInner,
                        RaceMode.CONTROLS_TUTORIAL,
                        Color.RED
                ).startGame(
                        "127.0.0.1",
                        "5010",
                        true
                )
        );
    }


    /**
     * Loads and places a small animation on the title screen in the bottom corner.
     */
    private void loadBoatAnimation() {
        Image boatImage = new Image("/images/title_screen/boatAnimated.gif");
        ImageView boatImageView = new ImageView(boatImage);
        paneInner.getChildren().add(boatImageView);
        boatImageView.setLayoutX((paneInner.getPrefWidth()) - (boatImage.getWidth() + 5));
        boatImageView.setLayoutY((paneInner.getPrefHeight()) - (boatImage.getHeight() + 5));

        ImageView boatImageView2 = new ImageView(boatImage);
        paneInner.getChildren().add(boatImageView2);
        boatImageView2.setLayoutX(5);
        boatImageView2.setLayoutY((paneInner.getPrefHeight()) - (boatImage.getHeight() + 5));
    }


    /**
     * Re draw the the pane which holds elements of the title screen to be in the middle of the window.
     */
    public void reDraw() {
        if (!(paneInner.getWidth() == 0)) {
            paneInner.setLayoutX((pane.getScene().getWidth() / 2) - (paneInner.getWidth() / 2));
            paneInner.setLayoutY((pane.getScene().getHeight() / 2) - (paneInner.getHeight() / 2));
        } else if (stage != null) {
            paneInner.setLayoutX((stage.getWidth() / 2) - (300));
            paneInner.setLayoutY((stage.getHeight() / 2) - (300));
        }
    }


    /**
     * Toggle the controls layout image in and out of view.
     * Image is placed in the middle of the pane, fit to the width.
     */
    private void toggleControlsView() {
        if (controlsVisible) {
            pane.getChildren().remove(controlsImageView);
            controlsVisible = false;
        } else {
            Image controlsImage = new Image("images/keyboardLayout.png");
            controlsImageView = new ImageView(controlsImage);
            pane.getChildren().add(controlsImageView);
            controlsImageView.setPreserveRatio(true);
            controlsImageView.setFitWidth(paneInner.getWidth());
            controlsImageView.setLayoutX((pane.getWidth() / 2) - (paneInner.getWidth() / 2));
            controlsVisible = true;
        }
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
