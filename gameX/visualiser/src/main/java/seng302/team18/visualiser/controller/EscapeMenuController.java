package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng302.team18.send.Sender;
import seng302.team18.visualiser.interpret.Interpreter;
import seng302.team18.visualiser.sound.SoundEffect;

import java.io.IOException;

/**
 * Controller for the menu that is brought up from the race view.
 */
public class EscapeMenuController {

    @FXML
    private Pane pane;

    private Group group;
    private Interpreter interpreter;
    private Sender sender;

    @FXML
    public void initialize() {
        initialiseQuitButton();
    }


    /**
     * Set up the button for viewing the controls
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
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
    private void returnToTitle() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("StartupInterface.fxml"));

        try {
            Parent root = loader.load();
            TitleScreenController controller = loader.getController();
            Stage stage = (Stage) group.getScene().getWindow();
            pane.getScene().setRoot(root);
            stage.setResizable(true);
            stage.setMaximized(true);
            controller.setStage(stage);
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
     */
    public void setup(Group group, Interpreter interpreter, Sender sender) {
        this.group = group;
        this.interpreter = interpreter;
        this.sender = sender;
    }


    private void buttonEnteredAction() {
        SoundEffect.BUTTON_MOUSE_ENTER.play();
    }


    private void buttonClickedAction() {
        SoundEffect.BUTTON_MOUSE_CLICK.play();
    }
}


