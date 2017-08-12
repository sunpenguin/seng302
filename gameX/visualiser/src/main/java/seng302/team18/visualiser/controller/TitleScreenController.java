package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.messageparsing.Receiver;
import seng302.team18.model.Race;
import seng302.team18.send.ControllerMessageFactory;
import seng302.team18.send.Sender;

import javax.net.SocketFactory;
import java.net.Socket;

/**
 * Controller for when the application first starts up
 */
public class TitleScreenController {
    @FXML private Text errorText;
    @FXML private TextField customPortField;
    @FXML private TextField customHostField;
    @FXML private AnchorPane pane;

    private Label hostLabel;
    private Image hostButtonImage;
    private Label controlsLabel;
    private Image controlsButtonImage;

    private Image controlsImage;
    private ImageView controlsImageView;
    private boolean controlsVisible = false;


    public void initialize() {
        initialiseHostButton();
        initialiseControlsButton();
    }


    /**
     * Called when the mock connection button is selected, sets up a connection with the mock feed
     */
    private void openMockStream() {
        openStream("127.0.0.1", 5005);
    }

    @FXML
    private void openCustomStream() {
        String host = customHostField.getText();
        String portString = customPortField.getText();

        if (host.isEmpty() || portString.isEmpty()) {
            errorText.setText("Please enter a custom host and port");
            return;
        }

        try {
            int port = Integer.parseInt(portString);
            openStream(host, port);
        } catch (NumberFormatException e) {
            errorText.setText("Please enter a valid port number");
            return;
        }
    }


    private void openStream(String host, int port) {
        try {
            Socket socket = SocketFactory.getDefault().createSocket(host, port);
            startConnection(new Receiver(socket, new AC35MessageParserFactory()), new Sender(socket, new ControllerMessageFactory()));
        } catch (Exception e) {
            errorText.setText(String.format("Could not establish connection to stream at: %s:%d", host, port));
        }
    }


    /**
     * Set up the button for hosting a new game.
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseHostButton() {
        hostLabel = new Label();
        hostLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/preRaceStyle.css").toExternalForm());
        hostLabel.getStyleClass().add("hostImage");
        pane.getChildren().add(hostLabel);

        hostButtonImage = new Image("/images/title_screen/host_new_game.png");
        hostLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) hostButtonImage.getWidth(), 2)));
        hostLabel.setLayoutY((600 / 2) + 100);
        hostLabel.setOnMouseClicked(event -> openMockStream());
    }


    /**
     * Set up the button for viewing the controls
     * Image used will changed when hovered over as defined in the preRaceStyle css.
     */
    private void initialiseControlsButton() {
        controlsLabel = new Label();
        controlsLabel.getStylesheets().add(this.getClass().getResource("/stylesheets/preRaceStyle.css").toExternalForm());
        controlsLabel.getStyleClass().add("controlsImage");
        pane.getChildren().add(controlsLabel);

        controlsButtonImage = new Image("/images/title_screen/view_controls.png");
        controlsLabel.setLayoutX((600 / 2) - (Math.floorDiv((int) controlsButtonImage.getWidth(), 2)));
        controlsLabel.setLayoutY((600 / 2) + 150);
        controlsLabel.setOnMouseClicked(event -> toggleControlsView());
    }


    /**
     * Creates a controller manager object and begins an instance of the program.
     *
     * @throws Exception A connection error
     */
    private void startConnection(Receiver receiver, Sender sender) throws Exception {
        Stage stage = (Stage) errorText.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PreRace.fxml"));
        Parent root = loader.load(); // throws IOException
        PreRaceController controller = loader.getController();
        stage.setTitle("RaceVision");
        Scene scene = new Scene(root, 777, 578);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        controller.setUp(new Race(), receiver, sender);
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
            controlsImage = new Image("images/keyboardLayout.png");
            controlsImageView = new ImageView(controlsImage);
            pane.getChildren().add(controlsImageView);
            controlsImageView.setPreserveRatio(true);
            controlsImageView.setFitWidth(pane.getWidth());
            controlsImageView.setLayoutY(pane.getHeight() / 2 - (controlsImage.getHeight() / 3));
            controlsVisible = true;
        }
    }
}
