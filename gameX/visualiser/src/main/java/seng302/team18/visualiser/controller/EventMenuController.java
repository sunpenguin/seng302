package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng302.team18.encode.Sender;
import seng302.team18.visualiser.interpret.Interpreter;
import seng302.team18.visualiser.sound.AudioPlayer;
import seng302.team18.visualiser.sound.SoundEffect;

import java.io.IOException;

/**
 * Controller for the menu that is brought up from the raceController view.
 */
public class EventMenuController {

    @FXML
    private Pane pane;

    private Group group;
    private Interpreter interpreter;
    private Sender sender;
    private AudioPlayer audioPlayer;

    @FXML
    public void initialize() {
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
        quitLabel.setLayoutY((pane.getPrefHeight() / 2) - (Math.floorDiv((int) quitButtonImage.getHeight(), 2)));
        quitLabel.setOnMouseClicked(event -> {
            buttonClickedAction();
            closeConnection();
            returnToTitle();
        });
        quitLabel.setOnMouseEntered(event -> buttonEnteredAction());
    }


    /**
     * Return the user to the title screen.
     */
    @SuppressWarnings("Duplicates")
    private void returnToTitle() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("StartupInterface.fxml"));

        try {
            Parent root = loader.load();
            TitleScreenController controller = loader.getController();
            Stage stage = (Stage) group.getScene().getWindow();
            pane.getScene().setRoot(root);
            stage.setResizable(true);
            stage.setMaximized(true);
            controller.setup(stage, audioPlayer);
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
        sender.close();
    }


    /**
     * Set up the escape menu.
     *
     * @param group       Group to place menu on
     * @param interpreter Interpreter to close when we quit the race.
     * @param sender      Sender to close when we quit the race.
     * @param player      the manager for audio playback from this scene.
     */
    public void setup(Group group, Interpreter interpreter, Sender sender, AudioPlayer player) {
        this.group = group;
        this.interpreter = interpreter;
        this.sender = sender;
        this.audioPlayer = player;
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