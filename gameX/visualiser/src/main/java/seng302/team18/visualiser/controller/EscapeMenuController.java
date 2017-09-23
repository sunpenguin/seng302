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

import java.io.IOException;

/**
 * Controller for the menu that is brought up from the raceController view.
 */
public class EscapeMenuController {

    @FXML private Pane pane;

    private Group group;
    private Label quitLabel;
    private Image quitButtonImage;
    private Interpreter interpreter;
    private Sender sender;
    private RaceController raceController;

    @FXML public void initialize() {
        initialiseAnnotationsButton();
        initialiseFPSButton();
        initialiseQuitButton();
    }


    /**
     * Set up the button for quitting a race.
     * Image used will changed when hovered over as defined in the escape_menu css.
     */
    private void initialiseQuitButton() {
        quitLabel = new Label();
        quitLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/escape_menu.css").toExternalForm());
        quitLabel.getStyleClass().add("quitImage");
        pane.getChildren().add(quitLabel);

        quitButtonImage = new Image("/images/escape_menu/quit_button_image.png");
        quitLabel.setLayoutX((pane.getPrefWidth() / 2) - (Math.floorDiv((int) quitButtonImage.getWidth(), 2)));
        quitLabel.setLayoutY((pane.getPrefHeight() / 2) + (int) quitButtonImage.getHeight() * 2);
        quitLabel.setOnMouseClicked(event -> {
            closeConnection();
            returnToTitle();
        });
    }


    /**
     * Set up the button for toggling the FPS.
     * Image used will changed when hovered over as defined in the escape_menu css.
     */
    private void initialiseFPSButton() {
        Label fpsLabel = new Label();
        fpsLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/escape_menu.css").toExternalForm());
        fpsLabel.getStyleClass().add("fpsImage");
        pane.getChildren().add(fpsLabel);

        Image fpsButtonImage = new Image("/images/escape_menu/fps_button_image.png");
        fpsLabel.setLayoutX((pane.getPrefWidth() / 2) - (Math.floorDiv((int) fpsButtonImage.getWidth(), 2)));
        fpsLabel.setLayoutY((pane.getPrefHeight() / 2));
        fpsLabel.setOnMouseClicked(event -> {
            raceController.toggleFPS();
        });
    }


    /**
     * Set up the button for toggling the annotations.
     * Image used will changed when hovered over as defined in the escape_menu css.
     */
    private void initialiseAnnotationsButton() {
        Label annotationsLabel = new Label();
        annotationsLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/escape_menu.css").toExternalForm());
        annotationsLabel.getStyleClass().add("annotationsImage");
        pane.getChildren().add(annotationsLabel);

        Image annotationsButtonImage = new Image("/images/escape_menu/annotations_button_image.png");
        annotationsLabel.setLayoutX((pane.getPrefWidth() / 2) - (Math.floorDiv((int) annotationsButtonImage.getWidth(), 2)));
        annotationsLabel.setLayoutY((pane.getPrefHeight() / 2) - (int) annotationsButtonImage.getHeight() * 2);
        annotationsLabel.setOnMouseClicked(event -> {
            raceController.toggleAnnotations();
        });
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
        } catch (IOException e) {}
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
     * @param group Group to place menu on
     * @param interpreter Interpreter to close when we quit the raceController.
     * @param sender Sender to close when we quit the raceController.
     */
    public void setup(Group group, Interpreter interpreter, Sender sender, RaceController raceController) {
        this.group = group;
        this.interpreter = interpreter;
        this.sender = sender;
        this.raceController = raceController;
    }
}


