package seng302.team18.racemodel;

import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.message.RequestType;
import seng302.team18.messageparsing.MessageParserFactory;
import seng302.team18.messageparsing.Receiver;
import seng302.team18.model.Race;
import seng302.team18.model.RaceMode;
import seng302.team18.racemodel.connection.*;
import seng302.team18.racemodel.interpret.BoatActionInterpreter;
import seng302.team18.racemodel.model.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class to create PlayerControllerReaders when connections made to the server
 */
public class ConnectionListener extends Observable implements Observer {

    private List<PlayerControllerReader> players;
    private Race race;
    private List<Integer> ids;
    private MessageParserFactory factory;
    private ExecutorService executor;
    private Long timeout;

    private static final AbstractRaceBuilder RACE_BUILDER = new RaceBuilder1();
    private static final AbstractCourseBuilder COURSE_BUILDER = new CourseBuilderPractice();
    private static final AbstractRegattaBuilder REGATTA_BUILDER = new RegattaBuilder1();

    /**
     * Constructs a new ConnectionListener.
     *
     * @param race The race
     * @param participantIds List of participant IDs
     * @param factory Factory to convert bytes to a RequestMessage.
     */
    public ConnectionListener(Race race, List<Integer> participantIds, MessageParserFactory factory) {
        this.race = race;
        this.ids = participantIds;
        this.factory = factory;
        players = new ArrayList<>();
        executor = Executors.newCachedThreadPool();
        timeout = Long.MAX_VALUE;
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
            addClient((ClientConnection) arg);
        } else if (ServerState.CLOSED.equals(arg)) {
            close();
        }
    }


    /**
     * Add a new client.
     * If the registration is for a tutorial, update the race accordingly and the TestMock will send out the updated
     * XML files.
     * When the registration is received, send the client their source ID.
     *
     * @param client Client who is connecting.
     */
    private void addClient(ClientConnection client) {
        try {
            Receiver receiver = new Receiver(client.getSocket(), factory);
            int sourceID = ids.get(players.size());
            executor.submit(() -> {
                MessageBody message = null;

                while (message == null && System.currentTimeMillis() < timeout) {
                    try {
                        message = receiver.nextMessage();
                    } catch (IOException e) {
                    }
                }
                if (message instanceof RequestMessage) {
                    RequestMessage request = (RequestMessage) message;

                    RequestType requestType = request.getAction();

                    if (!players.isEmpty() && requestType.code() != race.getMode().getCode()){
                        sendMessage(client, sourceID, RequestType.FAILURE_CLIENT_TYPE);
                        return;
                    }

                    switch (requestType) {
                        case CONTROLS_TUTORIAL:
                            race = RACE_BUILDER.buildRace(race, REGATTA_BUILDER.buildRegatta(), COURSE_BUILDER.buildCourse(),
                                    RaceMode.CONTROLS_TUTORIAL);
                            setChanged();
                            notifyObservers(this);
                            race.setCourseForBoats();
                        case RACING:
                            addPlayer(receiver, sourceID);
                            sendMessage(client, sourceID, requestType);
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Sends a ResponseMessage to the player.
     *
     * @param player the socket to send player messages to.
     * @param sourceID the assigned id of the player's boat.
     */
    private void sendMessage(ClientConnection player, int sourceID, RequestType requestType) {
        byte[] message = new AcceptanceMessageGenerator(sourceID, requestType).getMessage();
        player.sendMessage(message);
        if (requestType == RequestType.FAILURE_CLIENT_TYPE) {
            try {
                player.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
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
        PlayerControllerReader player = new PlayerControllerReader(sourceID, receiver, new BoatActionInterpreter(race, sourceID));
        players.add(player);
        executor.submit(player);
    }


    /**
     * sets timeout
     *
     * @param timeout Time at which the ConnectionListener will stop listening for requests (Epoch milli)
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }


    private void close() {
        for (PlayerControllerReader player: players) {
            player.close();
        }
        executor.shutdownNow();
    }
}
