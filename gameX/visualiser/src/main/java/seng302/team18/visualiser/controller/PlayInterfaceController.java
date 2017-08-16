package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng302.team18.message.ColourMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.message.NameMessage;

import java.util.ArrayList;
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
    private Image hostImage;
    private Image tutorialImage;

    private List<MessageBody> customisationMessages;


    @FXML
    public void initialize() {
        customisationMessages = new ArrayList<>();
        registerListeners();
        initialiseTutorialButton();
        initialiseHostButton();
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
        hostLabel.setLayoutX((800 / 2) - (Math.floorDiv((int) hostImage.getWidth(), 2)));
        hostLabel.setLayoutY((800 / 2) + 50);
        hostLabel.setOnMouseClicked(event -> hostButtonAction());
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
        tutorialLabel.setLayoutX((800 / 2) - (Math.floorDiv((int) tutorialImage.getWidth(), 2)));
        tutorialLabel.setLayoutY((800 / 2) + 100);
        tutorialLabel.setOnMouseClicked(event -> hostButtonAction());
    }


    /**
     * Act on user pressing the host new game button.
     */
    private void hostButtonAction() {
        System.out.println("PLAY");
    }


    /**
     * Act on user pressing the host new game button.
     */
    private void tutorialButtonAction() {
        System.out.println("TUTORIAL");
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

}
