package seng302.team18.test_mock.connection;

import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.model.Boat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Streaming server to connect test mock with clients.
 */
public class Server {
    private final ClientList clientList = new ClientList();
    private final ConnectionListener listener = new ConnectionListener();
    private static final int MAX_CLIENT_CONNECTION = 6;

    private ServerSocket serverSocket;
    private final int PORT;

    private final AC35XMLRaceMessage raceMessage;
    private final AC35XMLBoatMessage boatMessage;
    private final AC35XMLRegattaMessage regattaMessage;

    private XMLMessageGenerator regattaXMLMessageGenerator;
    private XMLMessageGenerator boatsXMLMessageGenerator;
    private XMLMessageGenerator raceXMLMessageGenerator;

    private final ParticipantManager participantManager;


    public Server(int port, ParticipantManager manager, AC35XMLRaceMessage raceMessage, AC35XMLBoatMessage boatMessage,
                  AC35XMLRegattaMessage regattaMessage) {
        this.PORT = port;
        this.participantManager = manager;

        this.raceMessage = raceMessage;
        this.boatMessage = boatMessage;
        this.regattaMessage = regattaMessage;

        raceXMLMessageGenerator = new XmlMessageGeneratorRace(raceMessage);
        boatsXMLMessageGenerator = new XmlMessageGeneratorBoats(boatMessage);
        regattaXMLMessageGenerator = new XmlMessageGeneratorRegatta(regattaMessage);
    }


    /**
     * Opens the server.
     * Blocks waiting for the first client connection, then opens a second thread to listen for subsequent connections
     */
    public void openServer() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Could not listen on port " + PORT);
            System.err.println("Exiting program");
            System.exit(-1);
        }

        System.out.println("Stream opened successfully on port: " + PORT);

        acceptClientConnection();

        listener.start();
    }


    /**
     * Blocks while waiting for a client connection, setting up new connection when available.
     * Adding new client to the client list.
     * Increment the number that represents number of connected clients.
     */
    private void acceptClientConnection() {
        try {
            ClientConnection client = new ClientConnection(serverSocket.accept());
            clientList.getClients().add(client);
            System.out.println("Player " + clientList.getClients().size() + " joined!");

            client.sendMessage(regattaXMLMessageGenerator.getMessage());
            addParticipant();

        } catch (SocketTimeoutException e) {
            // The time out expired, no big deal
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void stopAcceptingConnections() {
        listener.stopListening();
    }

    private final Boat[] boats = {
            new Boat("Emirates Team New Zealand", "TEAM New Zealand", 121),
            new Boat("Oracle Team USA", "TEAM USA", 122),
            new Boat("Artemis Racing", "TEAM SWE", 123),
            new Boat("Groupama Team France", "TEAM France", 124),
            new Boat("Land Rover BAR", "TEAM Britain", 125),
            new Boat("Softbank Team Japan", "TEAM Japan", 126)
    };

    private void addParticipant() {
        int number = clientList.getClients().size();
        Boat boat = boats[number];

        raceMessage.getParticipantIDs().add(boat.getId());
        boatMessage.getBoats().add(boat);
        participantManager.addBoat(boat);

        broadcast(raceXMLMessageGenerator.getMessage());
        broadcast(boatsXMLMessageGenerator.getMessage());
    }

    /**
     * Closes any open client connections and closes the server
     */
    public void closeServer() {
        for (ClientConnection client : clientList.getClients()) {
            try {
                client.close();
            } catch (IOException e) {
                System.err.println("Failed to close client connection to: " + client.getClient().getInetAddress().toString());
            }
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Failed to close server socket");
        }
    }

    /**
     * Broadcasts a message to all connected clients
     *
     * @param message the message to broadcast
     * @see ClientConnection#sendMessage(byte[])
     */
    public void broadcast(byte[] message) {
        if (message.length == 1) {//Scheduled messages should return {0} if there is an error when constructing them
            return;
        }
        for (ClientConnection client : clientList.getClients()) {
            client.sendMessage(message);
        }
    }

    /**
     * Prunes dead connections from the list of clients, where a connection is condsidered dead after failing
     * to respond a number of times
     *
     * @see ClientList#pruneConnections()
     * @see ClientConnection#MAX_FAILURES
     */
    public void pruneConnections() {
        clientList.pruneConnections();
    }


    /**
     * Thread that listens for incoming connections.
     */
    private class ConnectionListener extends Thread {
        private boolean listening = true;

        @Override
        public void run() {
            try {
                serverSocket.setSoTimeout(500);
            } catch (SocketException e) {
                e.printStackTrace();
            }

            while (listening) {
                if (clientList.getClients().size() < MAX_CLIENT_CONNECTION) {
                    acceptClientConnection();
                } else {
                    stopListening();
                }
            }
        }

        public void stopListening() {
            listening = false;
        }
    }
}
