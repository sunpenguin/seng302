package seng302.team18.visualiser.controller;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import seng302.team18.encode.ControllerMessageFactory;
import seng302.team18.encode.Sender;
import seng302.team18.interpret.CompositeMessageInterpreter;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35MessageType;
import seng302.team18.message.ColourMessage;
import seng302.team18.message.RequestMessage;
import seng302.team18.message.RequestType;
import seng302.team18.model.RaceMode;
import seng302.team18.parse.AC35MessageParserFactory;
import seng302.team18.parse.Receiver;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.interpret.Interpreter;
import seng302.team18.visualiser.interpret.unique.AcceptanceInterpreter;
import seng302.team18.visualiser.interpret.unique.ColourResponder;
import seng302.team18.visualiser.interpret.xml.XMLBoatInterpreter;
import seng302.team18.visualiser.interpret.xml.XMLRaceInterpreter;
import seng302.team18.visualiser.interpret.xml.XMLRegattaInterpreter;
import seng302.team18.visualiser.sound.AudioPlayer;
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
    private Interpreter interpreter;
    private Receiver receiver;
    private Sender sender;
    private ClientRace race;
    private AudioPlayer audioPlayer;
    private boolean failed = false;


    /**
     * Constructs a GameConnection
     *
     * @param errorText the text property to log errors to
     * @param node      a node from the calling scene
     * @param mode      the game type to launch/connect to
     * @param color     the colour of the player's boat
     * @param player    manages the audio playback from this scene
     */
    public GameConnection(StringProperty errorText, Node node, RaceMode mode, Color color, AudioPlayer player) {
        this.errorText = errorText;
        this.node = node;
        this.mode = mode;
        this.color = color;
        this.audioPlayer = player;
        errorText.set("");
    }


    /**
     * Starts a connection with the specified host. Has the option to start the host.
     *
     * @param hostString the address of the host
     * @param portString the port to connect to
     * @param isHosting  true if a server should be started, else false
     */
    public void startGame(String hostString, String portString, boolean isHosting) {
        String hostAddress = parseHostAddress(hostString);
        int port = parsePort(portString);
        if (port < 0 || hostAddress == null) {
            return;
        }

        if (isHosting) {
            try {
                new ModelLoader().startModel(port);
            } catch (IOException e) {
                errorText.set("Unable to initiate server!");
                failed = true;
                e.printStackTrace();
                return;
            }

            try {
                Thread.sleep(1000); // This is to give time for the process to start
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        openStream(hostAddress, port);
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
            failed = true;
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
                failed = true;
            }
        } catch (NumberFormatException e) {
            errorText.set(errorText.get() + "\nPlease enter a valid port number!");
            failed = true;
        }

        return port;
    }


    /**
     * Creates a controller manager object and begins an instance of the program.
     *
     * @param receiver a receiver
     * @param sender   a sender
     */
    private void startConnection(Receiver receiver, Sender sender) {
        this.receiver = receiver;
        this.sender = sender;
        race = new ClientRace();
        race.setMode(mode);
        attemptConnection();
    }


    /**
     * Method to attempt to connection with server
     */
    private void attemptConnection() {
        interpreter = new Interpreter(receiver);
        interpreter.setInterpreter(makeInterpreter(race));
        interpreter.start();

        RequestType requestType;
        switch (race.getMode()) {
            case RACE:
                requestType = RequestType.RACING;
                break;
            case CONTROLS_TUTORIAL:
                requestType = RequestType.CONTROLS_TUTORIAL;
                break;
            case CHALLENGE_MODE:
                requestType = RequestType.CHALLENGE_MODE;
                break;
            case ARCADE:
                requestType = RequestType.ARCADE;
                break;
            case BUMPER_BOATS:
                requestType = RequestType.BUMPER_BOATS;
                break;
            case SPECTATION:
                requestType = RequestType.VIEWING;
                break;
            default:
                requestType = RequestType.RACING;
        }


        try {
            sender.send(new RequestMessage(requestType));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private MessageInterpreter makeInterpreter(ClientRace race) {
        MessageInterpreter interpreter = new CompositeMessageInterpreter();
        interpreter.add(AC35MessageType.XML_RACE.getCode(), new XMLRaceInterpreter(race));
        interpreter.add(AC35MessageType.XML_BOATS.getCode(), new XMLBoatInterpreter(race));
        interpreter.add(AC35MessageType.XML_REGATTA.getCode(), new XMLRegattaInterpreter(race));
        interpreter.add(AC35MessageType.ACCEPTANCE.getCode(), new AcceptanceInterpreter(race, this));
        interpreter.add(AC35MessageType.ACCEPTANCE.getCode(), new ColourResponder(color, sender));

        return interpreter;
    }


    /**
     * Opens a socket and connection on the given host and port number
     *
     * @param host The host IP address for the socket
     * @param port The port number used for the socket
     */
    private void openStream(String host, int port) {
        try {
            Socket socket = SocketFactory.getDefault().createSocket(host, port);
            startConnection(new Receiver(socket, new AC35MessageParserFactory()), new Sender(socket, new ControllerMessageFactory()));
            failed = false;
        } catch (Exception e) {
            errorText.set("Failed to connect to " + host + ":" + port + "!");
            failed = true;
        }
    }


    public void goToPreRace() throws IOException {
        failed = false;
        sender.send(new ColourMessage(color, race.getPlayerId()));

        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PreRace.fxml"));
        Parent root = loader.load();
        PreRaceController controller = loader.getController();
        stage.setTitle("High Seas");
        node.getScene().setRoot(root);
        stage.show();

        controller.setUp(race, sender, interpreter, audioPlayer);
    }

    public void setFailedConnection() {
        errorText.set("Failed to connect to server:\nServer rejected connection");
        failed = true;
    }


    /**
     * Returns if the connection has failed or not
     *
     * @return
     */
    public boolean hasFailed() {
        return failed;
    }
}
