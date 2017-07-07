package seng302.team18.test_mock;

import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.messageparsing.MessageParserFactory;
import seng302.team18.messageparsing.SocketMessageReceiver;
import seng302.team18.model.Race;
import seng302.team18.test_mock.connection.*;
import seng302.team18.test_mock.interpret.BoatActionInterpreter;

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
public class ConnectionListener implements Observer {

    private List<PlayerControllerReader> players;
    private Race race;
    private List<Integer> ids;
    private MessageParserFactory factory;
    private ExecutorService executor;
    private long timeout;


    /**
     * Constructs a new ConnectionListener.
     *
     * @param race The race
     * @param participantIds List of participant IDs
     * @param timeout Time at which the ConnectionListener will stop listening for requests (Epoch milli)
     * @param factory Factory to convert bytes to a RequestMessage.
     */
    public ConnectionListener(Race race, List<Integer> participantIds, long timeout, MessageParserFactory factory) {
        this.race = race;
        this.ids = participantIds;
        this.factory = factory;
        players = new ArrayList<>();
        executor = Executors.newCachedThreadPool();
        this.timeout = timeout;
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
            ClientConnection client = (ClientConnection) arg;
            try {
                SocketMessageReceiver receiver = new SocketMessageReceiver(client.getSocket(), factory);
                int sourceID = ids.get(players.size() - 1);

                executor.submit(() -> {
                    try {
                        MessageBody message = null;
                        while (message == null && System.currentTimeMillis() < timeout) {
                            message = receiver.nextMessage();
                        }
                        if (message instanceof RequestMessage) {
                            RequestMessage request = (RequestMessage) message;
                            if (request.isParticipating()) {
                                addPlayer(receiver, sourceID);
                                sendMessage(client, sourceID);
                            }
                        }
                    } catch (IOException e) {
                        // no message
                    }
                });
            } catch (Exception e) {}
        }
    }


    /**
     * Sends a ResponseMessage to the player.
     *
     * @param player the socket to send player messages to.
     * @param sourceID the assigned id of the player's boat.
     * @throws IOException
     */
    private void sendMessage(ClientConnection player, int sourceID) throws IOException {
        player.sendMessage(new AcceptanceMessageGenerator(sourceID).getMessage());
    }


    /**
     * Creates a new player and adds them to the list of players.
     * We also start listening to the player's commands.
     *
     * @param receiver the socket the player sends commands from.
     * @param sourceID the assigned id of the player's boat.
     */
    private void addPlayer(SocketMessageReceiver receiver, int sourceID) {
        PlayerControllerReader player = new PlayerControllerReader(receiver, new BoatActionInterpreter(race, sourceID));
        players.add(player);
        executor.submit(player);
    }

}
