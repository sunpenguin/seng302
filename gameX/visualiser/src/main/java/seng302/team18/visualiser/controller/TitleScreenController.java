package seng302.team18.visualiser.controller;

import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import seng302.team18.model.RaceMode;
import seng302.team18.visualiser.sound.AudioPlayer;
import seng302.team18.visualiser.sound.Music;
import seng302.team18.visualiser.sound.SoundEffect;

import java.io.IOException;

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

    private Pane helpMenuPane;

    private Stage stage;
    private AudioPlayer audioPlayer;

    private ToggleButton soundEffectsButton = new ToggleButton();
    private ToggleButton musicButton = new ToggleButton();

    private boolean soundOn = true;
    private boolean musicOn = true;


    public void initialize() {
        registerListeners();
        initialiseHostButton();
        initialiseHelpButton();
        initialiseQuitButton();
        initialiseTutorialButton();
        loadBoatAnimation();

        errorText.setLayoutX((600 / 2) - errorText.getPrefWidth());
        errorText.setLayoutY((600 / 2) + 300);

        paneInner.getChildren().add(soundEffectsButton);
        paneInner.getChildren().add(musicButton);

        musicButton.setLayoutY(-50);
        musicButton.setLayoutX(100);
        soundEffectsButton.setLayoutY(-50);

        musicButton.setOnMouseClicked(event -> toggleMusic());
        soundEffectsButton.setOnMouseClicked(event -> toggleSoundEffects());

        musicButton.getStyleClass().add("musicView");
        soundEffectsButton.getStyleClass().add("soundView");
    }


    private void toggleMusic() {
        musicOn = !musicOn;
    }


    private void toggleSoundEffects() {
        soundOn = !soundOn;
    }


    /**
     * @param stage  the stage for this view
     * @param player manages the audio playback from this scene
     */
    public void setup(Stage stage, @NotNull AudioPlayer player) {
        this.stage = stage;
        this.audioPlayer = player;

        loadHelpMenu(audioPlayer);

        audioPlayer.loopMusic(Music.BEEPBOOP);
        audioPlayer.stopAllAmbient();
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
        controller.setup(audioPlayer);
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
        hostLabel.setOnMouseClicked(event -> {
            buttonClickedAction();
            toPlayScreen();
        });
        hostLabel.setOnMouseEntered(event1 -> buttonEnteredAction());
    }


    /**
     * Set up the button for viewing the controls
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseHelpButton() {
        Label controlsLabel = new Label();
        controlsLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/titleScreen.css").toExternalForm());
        controlsLabel.getStyleClass().add("helpImage");
        paneInner.getChildren().add(controlsLabel);

        Image controlsButtonImage = new Image("/images/title_screen/help_menu/help_button.png");
        controlsLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) controlsButtonImage.getWidth(), 2)));
        controlsLabel.setLayoutY((600 / 2) + 150);
        controlsLabel.setOnMouseClicked(event -> {
            buttonClickedAction();
            openHelpMenu();
        });

        controlsLabel.setOnMouseEntered(event1 -> buttonEnteredAction());

    }


    /**
     * Load the associated FXML for the help menu into a Pane object.
     */
    private void loadHelpMenu(@NotNull AudioPlayer player) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("helpMenu.fxml"));
            helpMenuPane = loader.load();
            HelpMenuController helpMenuController = loader.getController();
            helpMenuController.setup(paneInner, player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Show the help menu on top of the title screen
     */
    private void openHelpMenu() {
        if (!paneInner.getChildren().contains(helpMenuPane)) {
            paneInner.getChildren().add(helpMenuPane);
            helpMenuPane.toFront();
            helpMenuPane.setLayoutX(0);
            helpMenuPane.setLayoutY(-100);
        }
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
        quitLabel.setOnMouseClicked(event -> {
            buttonClickedAction();
            System.exit(0);
        });
        quitLabel.setOnMouseEntered(event1 -> buttonEnteredAction());
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
        tutorialLabel.setOnMouseClicked(event -> {
            buttonClickedAction();
            GameConnection gameConnection = new GameConnection(errorText.textProperty(), paneInner, RaceMode.CONTROLS_TUTORIAL, Color.RED, audioPlayer);
            gameConnection.startGame("127.0.0.1", "5010", true);
        });
        tutorialLabel.setOnMouseEntered(event1 -> buttonEnteredAction());
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
     * Common actions for OnMouseEntered events of menu buttons.
     * <p>
     * Plays sound effect defined by {@link SoundEffect#BUTTON_MOUSE_ENTER SoundEffect#BUTTON_MOUSE_ENTER}
     */
    private void buttonEnteredAction() {
        audioPlayer.playEffect(SoundEffect.BUTTON_MOUSE_ENTER);
    }


    /**
     * Common actions for OnMouseClicked events of menu buttons.
     * <p>
     * Plays sound effect defined by {@link SoundEffect#BUTTON_MOUSE_CLICK SoundEffect#BUTTON_MOUSE_CLICK}
     */
    private void buttonClickedAction() {
        audioPlayer.playEffect(SoundEffect.BUTTON_MOUSE_CLICK);
    }


}
