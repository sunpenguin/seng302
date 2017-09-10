package seng302.team18.racemodel.connection;

import seng302.team18.interpret.CompositeMessageInterpreter;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.*;
import seng302.team18.message.RequestMessage;
import seng302.team18.messageparsing.MessageParserFactory;
import seng302.team18.messageparsing.Receiver;
import seng302.team18.model.Race;
import seng302.team18.racemodel.interpret.BoatActionInterpreter;
import seng302.team18.racemodel.interpret.ColourInterpreter;
import seng302.team18.racemodel.message_generating.AcceptanceMessageGenerator;
import seng302.team18.racemodel.model.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class to create PlayerControllerReaders when connections made to the server
 */
public class ConnectionListener extends Observable implements Observer {

    private List<PlayerControllerReader> players = new ArrayList<>();
    private Race race;
    private List<Integer> ids;
    private MessageParserFactory factory;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private Long timeout = Long.MAX_VALUE;

    private AbstractRaceBuilder raceBuilder;
    private AbstractCourseBuilder courseBuilder;
    private AbstractRegattaBuilder regattaBuilder = new RegattaBuilder1();

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
                        e.printStackTrace();
                    }
                }

                if (message instanceof RequestMessage) {
                    respond(sourceID, (RequestMessage) message, client, receiver);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Constructs a race with the given builders and sets the mode. Notify TestMock to regenerate XMLs.
     */
    private void constructRace() {
        race = raceBuilder.buildRace(race, regattaBuilder.buildRegatta(), courseBuilder.buildCourse());
        race.setCourseForBoats();
        setChanged();
        notifyObservers(this);
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
        MessageInterpreter interpreter = new CompositeMessageInterpreter();
        interpreter.add(AC35MessageType.COLOUR.getCode(), new ColourInterpreter(race.getStartingList()));
        interpreter.add(AC35MessageType.BOAT_ACTION.getCode(), new BoatActionInterpreter(race, sourceID));

        PlayerControllerReader player = new PlayerControllerReader(sourceID, receiver, interpreter);
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


    /**
     * Responds to a request message
     * @param id
     * @param request
     * @param client
     */
    public void respond(int id, RequestMessage request, ClientConnection client, Receiver receiver) {
        RequestType requestType = request.getAction();

        if (!isValidMode(requestType)) {
            sendFailureMessage(client, id);
            return;
        }

        setRaceMode(requestType);
        constructRace();

        addPlayer(receiver, id);
        sendMessage(client, id, requestType);
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


    /**
     * Sends a failure message to a client.
     *
     * @param client to send to.
     * @param id of the client.
     */
    private void sendFailureMessage(ClientConnection client, int id) {
        sendMessage(client, id, RequestType.FAILURE_CLIENT_TYPE);
        AcceptanceMessage failMessage = new AcceptanceMessage(id, RequestType.FAILURE_CLIENT_TYPE);
        setChanged();
        notifyObservers(failMessage);
    }


    /**
     * Sets the race mode to the correct type.
     *
     * @param type of the race.
     */
    private void setRaceMode(RequestType type) {
        switch (type) {
            case CONTROLS_TUTORIAL:
                raceBuilder = new TutorialRaceBuilder();
                courseBuilder = new CourseBuilderPractice();
                break;
            case ARCADE:
                raceBuilder = new ArcadeRaceBuilder();
                courseBuilder = new CourseBuilderRealistic();
                break;
            case BUMPER_BOATS:
                raceBuilder = new BumperBoatsRaceBuilder();
                courseBuilder = new CourseBuilderBumper();
                break;
            case RACING:
                raceBuilder = new RegularRaceBuilder();
                courseBuilder = new CourseBuilderRealistic();
                break;
            case CHALLENGE_MODE:
                raceBuilder = new ChallengeRaceBuilder();
                courseBuilder = new CourseBuilderChallenge();
                break;
        }
    }
}
