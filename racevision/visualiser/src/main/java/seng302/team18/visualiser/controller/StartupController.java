package seng302.team18.visualiser.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.messageparsing.SocketMessageReceiver;

/**
 * Controller for when the application first starts up
 */
public class StartupController {
    @FXML private Text errorText;
    @FXML private TextField customPortField;
    @FXML private TextField customHostField;



    /**
     * Called when the live connection button is selected, sets up a connection with the live AC35 feed
     */
    @FXML
    private void openLiveStream() {
        openStream("livedata.americascup.com", 4940);
    }

    /**
     * Called when the test connection button is selected, sets up a connection with the UC test feed
     */
    @FXML
    private void openTestStream() {
        openStream("livedata.americascup.com", 4941);
    }

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
            startConnection(new SocketMessageReceiver(host, port, new AC35MessageParserFactory()));
        } catch (Exception e) {
            errorText.setText(String.format("Could not establish connection to stream at: %s:%d", host, port));
        }
    }

    /**
     * Creates a controller manager object and begins an instance of the program.
     *
     * @throws Exception A connection error
     */
    private  void startConnection(SocketMessageReceiver receiver) throws Exception {

        Stage s = (Stage) errorText.getScene().getWindow();
        ControllerManager manager = new ControllerManager(s, "MainWindow.fxml", "PreRace.fxml");
        manager.setReceiver(receiver);
        manager.start();
    }

}
