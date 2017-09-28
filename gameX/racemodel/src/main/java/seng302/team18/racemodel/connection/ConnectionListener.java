package seng302.team18.racemodel.connection;

import seng302.team18.interpret.CompositeMessageInterpreter;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35MessageType;
import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.message.RequestType;
import seng302.team18.model.Race;
import seng302.team18.model.RaceStatus;
import seng302.team18.parse.MessageParserFactory;
import seng302.team18.parse.Receiver;
import seng302.team18.racemodel.generate.AcceptanceMessageGenerator;
import seng302.team18.racemodel.interpret.BoatActionInterpreter;
import seng302.team18.racemodel.interpret.ColourInterpreter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class to create PlayerControllerReaders when connections made to the server
 */
public class ConnectionListener extends Observable implements Observer {

    private List<PlayerControlsReader> players = new ArrayList<>();
    private List<Integer> ids;
    private MessageParserFactory factory;
    private ExecutorService executor = Executors.newCachedThreadPool();

    private Race race;

    private int playersJoined = 0;
    private static final int MAX_PLAYERS = 6;

    /**
     * Constructs a new ConnectionListener.
     *
     * @param participantIds List of participant IDs
     * @param factory Factory to convert bytes to a RequestMessage.
     */
    public ConnectionListener(Race race, List<Integer> participantIds, MessageParserFactory factory) {
        this.race = race;
        this.ids = participantIds;
        this.factory = factory;
    }


    /**
     * Called when server notifies of a new connection.
     * Listens for a request packet to view or participate until either one is received or the timeout.
     *
     * @param o The observable object
     * @param arg ClientConnection, the socket from which the client is connected.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ClientConnection) {
            executor.submit(() -> {
                try {
                    handleConnection((ClientConnection) arg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else if (arg instanceof Integer) {
            removePlayer((Integer) arg);
            setChanged();
            notifyObservers(arg);
        } else if (arg instanceof ServerState && !ServerState.OPEN.equals(arg)) {
            close();
            setChanged();
            notifyObservers(arg);
        }
    }


    /**
     * If the registration is for a tutorial, update the race accordingly and the TestMock will encode out the updated
     * XML files.
     * When the registration is received, encode the client their source ID.
     *
     * @param client Client who is connecting.
     */
    private void handleConnection(ClientConnection client) throws IOException {
        final int SPECTATOR_ID = 9000;
        Receiver receiver = new Receiver(client.getSocket(), factory); // IOException
        MessageBody message = getMessage(receiver);
        if (message instanceof RequestMessage) {
            RequestMessage request = (RequestMessage) message;
            int id = ids.get(players.size());
            client.setRequestType(request.getAction());
            if (request.getAction() == RequestType.VIEWING) { // spectator
                sendMessage(client, SPECTATOR_ID, request.getAction());
                setChanged();
                notifyObservers(client);
            } else if (!isValidMode(request.getAction())) { // invalid
                sendMessage(client, id, RequestType.FAILURE_CLIENT_TYPE);
                client.close(); // IOException
            } else if (playersJoined < MAX_PLAYERS && isJoinable(race.getStatus())) { // a valid player before the race starts
                addPlayer(receiver, id);
                sendMessage(client, id, request.getAction());
                setChanged();
                notifyObservers(client);
                playersJoined++;
            } else { // a valid player after the race starts
                sendMessage(client, id, RequestType.FAILURE_CLIENT_TYPE);
                client.close(); // IOException
            }
        }
    }


    /**
     * Returns the next message from the socket. (blocking)
     * @param receiver to get the message from.
     * @return the next message.
     */
    private MessageBody getMessage(Receiver receiver) {
        MessageBody message = null;
        while (message == null) {
            try {
                message = receiver.nextMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return message;
    }


    /**
     * Sends a ResponseMessage to the player.
     *
     * @param player the socket to encode player messages to.
     * @param sourceID the assigned id of the player's boat.
     */
    private void sendMessage(ClientConnection player, int sourceID, RequestType requestType) {
        byte[] message = new AcceptanceMessageGenerator(sourceID, requestType).getMessage();
        player.send(message);
        player.setId(sourceID);
    }


    /**
     * Creates a new player and adds them to the list of players.
     * We also start listening to the player's commands.
     *
     * @param receiver the socket the player sends commands from.
     * @param sourceID the assigned id of the player's boat.
     */
    private void addPlayer(Receiver receiver, int sourceID) {
        MessageInterpreter interpreter = new CompositeMessageInterpreter();
        interpreter.add(AC35MessageType.COLOUR.getCode(), new ColourInterpreter(race.getStartingList()));
        interpreter.add(AC35MessageType.BOAT_ACTION.getCode(), new BoatActionInterpreter(race, sourceID));

        PlayerControlsReader player = new PlayerControlsReader(sourceID, receiver, interpreter);
        players.add(player);
        executor.submit(player);
    }


    private void close() {
        for (PlayerControlsReader player: players) {
            player.close();
        }
        executor.shutdownNow();
    }


    /**
     * Checks that the given mode is the same as the current race OR there race is empty.
     *
     * @param type of request
     * @return if correct mode
     */
    private boolean isValidMode(RequestType type) {
        return players.isEmpty() || type.getCode() == race.getMode().getCode();
    }


    private void removePlayer(int id) {
        int removeIndex = -1;
        for (int i = 0; i < players.size(); i++) {
            PlayerControlsReader player = players.get(i);
            if (player.getId() == id) {
                player.close();
                removeIndex = i;
            }
        }
        if (removeIndex != -1) {
            players.remove(removeIndex);
        }
    }

    private boolean isJoinable(RaceStatus status) {
        return status == RaceStatus.WARNING || status.isBefore(RaceStatus.WARNING);
    }

}
