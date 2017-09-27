package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng302.team18.encode.Sender;
import seng302.team18.visualiser.interpret.Interpreter;
import seng302.team18.visualiser.sound.SoundEffect;
import seng302.team18.visualiser.sound.SoundEffectPlayer;
import seng302.team18.visualiser.sound.ThemeTunePlayer;

import java.io.IOException;

/**
 * Controller for the menu that is brought up from the raceController view.
 */
public class EscapeMenuController {

    @FXML
    private Pane pane;

    private Group group;
    private Interpreter interpreter;
    private Sender sender;
    private RaceController raceController;
    private SoundEffectPlayer soundPlayer;

    private boolean fpsOn = true;
    private boolean annotationsOn = false;
    private final int BUTTON_LAYOUTX_OFFSET = 9;
    private ThemeTunePlayer themeTunePlayer;

    @FXML
    public void initialize() {
        initialiseAnnotationsButton();
        initialiseFPSButton();
        initialiseQuitButton();
    }


    /**
     * Set up the button for quitting a race.
     * Image used will changed when hovered over as defined in the escape_menu css.
     */
    @SuppressWarnings("Duplicates")
    private void initialiseQuitButton() {
        Label quitLabel = new Label();
        quitLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/escape_menu.css").toExternalForm());
        quitLabel.getStyleClass().add("quitImage");
        pane.getChildren().add(quitLabel);

        Image quitButtonImage = new Image("/images/escape_menu/quit_button_image.png");
        quitLabel.setLayoutX((pane.getPrefWidth() / 2) - (Math.floorDiv((int) quitButtonImage.getWidth(), 2)));
        quitLabel.setLayoutY((pane.getPrefHeight() / 2) + (int) quitButtonImage.getHeight() * 2);
        quitLabel.setOnMouseClicked(event -> {
            buttonClickedAction();
            closeConnection();
            returnToTitle();
        });
        quitLabel.setOnMouseEntered(event -> buttonEnteredAction());
    }


    /**
     * Set up the button for toggling the FPS.
     * Image used will changed when hovered over as defined in the escape_menu css.
     */
    private void initialiseFPSButton() {
        ToggleButton fpsButton = new ToggleButton();
        fpsButton.getStylesheets().add(this.getClass().getResource("/stylesheets/escape_menu.css").toExternalForm());
        fpsButton.getStyleClass().add("fpsImage");
        pane.getChildren().add(fpsButton);

        Image fpsButtonImage = new Image("/images/escape_menu/fps_button_image.png");
        fpsButton.setLayoutX((pane.getPrefWidth() / 2) - (Math.floorDiv((int) fpsButtonImage.getWidth(), 2)) - BUTTON_LAYOUTX_OFFSET);
        fpsButton.setLayoutY((pane.getPrefHeight() / 2));
        fpsButton.setSelected(fpsOn);
        fpsButton.setOnMouseClicked(event -> {
            buttonClickedAction();
            raceController.toggleFPS();
            fpsOn = !fpsOn;
            setButtonSelected(fpsButton, fpsOn);
            group.getChildren().remove(raceController.getEscapeMenuPane());
        });
        fpsButton.setOnMouseEntered(event -> buttonEnteredAction());
    }


    /**
     * Sets a button to be selected.
     *
     * @param button   to set selected for.
     * @param selected boolean to change the button selected.
     */
    private void setButtonSelected(ToggleButton button, boolean selected) {
        button.setSelected(selected);
    }


    /**
     * Set up the button for toggling the annotations.
     * Image used will changed when hovered over as defined in the escape_menu css.
     */
    private void initialiseAnnotationsButton() {
        ToggleButton annotationsButton = new ToggleButton();
        annotationsButton.getStylesheets().add(this.getClass().getResource("/stylesheets/escape_menu.css").toExternalForm());
        annotationsButton.getStyleClass().add("annotationsImage");
        pane.getChildren().add(annotationsButton);

        Image annotationsButtonImage = new Image("/images/escape_menu/annotations_button_image.png");
        annotationsButton.setLayoutX((pane.getPrefWidth() / 2) - (Math.floorDiv((int) annotationsButtonImage.getWidth(), 2)) - BUTTON_LAYOUTX_OFFSET);
        annotationsButton.setLayoutY((pane.getPrefHeight() / 2) - (int) annotationsButtonImage.getHeight() * 2);
        annotationsButton.setSelected(annotationsOn);
        annotationsButton.setOnMouseClicked(event -> {
            buttonClickedAction();
            raceController.toggleAnnotations();
            annotationsOn = !annotationsOn;
            setButtonSelected(annotationsButton, annotationsOn);
            group.getChildren().remove(raceController.getEscapeMenuPane());
        });
        annotationsButton.setOnMouseEntered(event -> buttonEnteredAction());
    }


    /**
     * Return the user to the title screen.
     */
    @SuppressWarnings("Duplicates")
    private void returnToTitle() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("StartupInterface.fxml"));
        themeTunePlayer.stopTrack();

        try {
            Parent root = loader.load();
            TitleScreenController controller = loader.getController();
            Stage stage = (Stage) group.getScene().getWindow();
            pane.getScene().setRoot(root);
            stage.setResizable(true);
            stage.setMaximized(true);
            controller.setStage(stage);
            controller.setSoundPlayer(soundPlayer);
            controller.reDraw();
            stage.show();
        } catch (IOException e) {
            // pass
        }
    }


    /**
     * Close the connection to the server by closing the interpreter and sender.
     */
    private void closeConnection() {
        interpreter.close();
        interpreter.closeReceiver();
        sender.close();
    }


    /**
     * Set up the escape menu.
     *
     * @param group       Group to place menu on
     * @param interpreter Interpreter to close when we quit the race.
     * @param sender      Sender to close when we quit the race.
     * @param raceController to connect the escaoe menu to.
     * @param player      the manager for audio playback from this scene.
     * @param themeTunePlayer the player to manage the theme tune
     */
    public void setup(Group group, Interpreter interpreter, Sender sender, RaceController raceController, SoundEffectPlayer player, ThemeTunePlayer themeTunePlayer) {
        this.group = group;
        this.interpreter = interpreter;
        this.sender = sender;
        this.raceController = raceController;
        this.soundPlayer = player;
        this.themeTunePlayer = themeTunePlayer;
    }


    /**
     * Common actions for OnMouseEntered events of menu buttons.
     * <p>
     * Plays sound effect defined by {@link SoundEffect#BUTTON_MOUSE_ENTER SoundEffect#BUTTON_MOUSE_ENTER}
     */
    private void buttonEnteredAction() {
        soundPlayer.playEffect(SoundEffect.BUTTON_MOUSE_ENTER);
    }


    /**
     * Common actions for OnMouseClicked events of menu buttons.
     * <p>
     * Plays sound effect defined by {@link SoundEffect#BUTTON_MOUSE_CLICK SoundEffect#BUTTON_MOUSE_CLICK}
     */
    private void buttonClickedAction() {
        soundPlayer.playEffect(SoundEffect.BUTTON_MOUSE_CLICK);
    }
}


