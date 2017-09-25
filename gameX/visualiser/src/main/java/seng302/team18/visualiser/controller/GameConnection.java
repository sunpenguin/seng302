package seng302.team18.visualiser.controller;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import seng302.team18.parse.AC35MessageParserFactory;
import seng302.team18.parse.Receiver;
import seng302.team18.model.RaceMode;
import seng302.team18.encode.ControllerMessageFactory;
import seng302.team18.encode.Sender;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.util.ModelLoader;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.Socket;


/**
 * Manages the initiation of a connection to a server and the transition to the pre-race UI
 */
public class GameConnection {

    private final StringProperty errorText;
    private final Node node;
    private final RaceMode mode;
    private final Color color;


    /**
     * Constructs a GameConnection
     *
     * @param errorText the text property to log errors to
     * @param node      a node from the calling scene
     * @param mode      the game type to launch/connect to
     * @param color     the colour of the player's boat
     */
    public GameConnection(StringProperty errorText, Node node, RaceMode mode, Color color) {
        this.errorText = errorText;
        this.node = node;
        this.mode = mode;
        this.color = color;
        errorText.set("");
    }


    /**
     * Starts a connection with the specified host. Has the option to start the host.
     *
     * @param hostString the address of the host
     * @param portString the port to connect to
     * @param isHosting  true if a server should be started, else false
     * @return true if the operation succeeded, else false
     */
    public boolean startGame(String hostString, String portString, boolean isHosting) {
        String hostAddress = parseHostAddress(hostString);
        int port = parsePort(portString);
        if (port < 0 || hostAddress == null) return false;

        if (isHosting) {
            try {
                (new ModelLoader()).startModel(port);
            } catch (IOException e) {
                errorText.set("Unable to initiate server!");
                e.printStackTrace();
                return false;
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return openStream(hostAddress, port);
    }


    /**
     * Checks the host address, logging errors
     *
     * @param hostAddress the address to check
     * @return the host address if it is valid, else {@code null}
     */
    private String parseHostAddress(String hostAddress) {
        if (hostAddress.isEmpty()) {
            errorText.set(errorText.get() + "\nPlease enter a valid host address!");
            return null;
        }

        return hostAddress;
    }


    /**
     * Checks the port, logging errors
     * Must be a number in the range 1025-65534
     *
     * @param portString the port number to check
     * @return the port address if it is valid, else some negative number
     */
    private int parsePort(String portString) {
        int port = -1;
        try {
            port = Integer.parseInt(portString);
            if (port < 1024 || port > 65535) {
                port = -1;
                errorText.set(errorText.get() + "\nPlease enter a port number in the range 1025-65534!");
            }
        } catch (NumberFormatException e) {
            errorText.set(errorText.get() + "\nPlease enter a valid port number!");
        }

        return port;
    }


    /**
     * Creates a controller manager object and begins an instance of the program.
     *
     * @param receiver a reciver
     * @param sender   a sender
     * @return true if the operation is successful, else false
     * @throws Exception if the next scene cannot be loaded
     */
    private boolean startConnection(Receiver receiver, Sender sender) throws Exception {
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PreRace.fxml"));
        Parent root = loader.load();
        PreRaceController controller = loader.getController();
        stage.setTitle("High Seas");
        node.getScene().setRoot(root);
        stage.show();

        ClientRace race = new ClientRace();
        race.setMode(mode);
        controller.setUp(race, receiver, sender);
        controller.initConnection(color);
        return true;
        // TODO afj19 14/09 here(probably) we should be checking the response from the server and returning false if it is rejected
    }


    /**
     * Opens a socket and connection on the given host and port number
     *
     * @param host The host IP address for the socket
     * @param port The port number used for the socket
     */
    private boolean openStream(String host, int port) {
        try {
            Socket socket = SocketFactory.getDefault().createSocket(host, port);
            return startConnection(new Receiver(socket, new AC35MessageParserFactory()), new Sender(socket, new ControllerMessageFactory()));
        } catch (Exception e) {
            errorText.set("Failed to connect to " + host + ":" + port + "!");
        }
        return false;
    }
}
