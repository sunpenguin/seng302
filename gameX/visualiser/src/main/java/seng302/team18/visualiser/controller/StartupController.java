package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
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
public class StartupController {
    @FXML private Text errorText;
    @FXML private TextField customPortField;
    @FXML private TextField customHostField;


    /**
     * Called when the mock connection button is selected, sets up a connection with the mock feed
     */
    @FXML
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

}
