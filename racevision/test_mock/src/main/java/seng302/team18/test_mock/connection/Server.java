package seng302.team18.test_mock.connection;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Streaming server to connect test mock with clients.
 */
public class Server {

    private final ClientList clientList = new ClientList();
    private final ConnectionListener connectionListener = new ConnectionListener();
    private final int PORT;
    private ServerSocket serverSocket;
    private String regattaXMLPath;
    private String boatsXMLPath;
    private String raceXMLPath;

    public Server(int port, String regattaXML, String boatsXML, String raceXML) {
        this.PORT = port;
        this.regattaXMLPath = regattaXML;
        this.boatsXMLPath = boatsXML;
        this.raceXMLPath = raceXML;
    }

    /**
     * Blocks while waiting for a client connection, setting up new connection when available.
     * This includes sending the initial XML files and adding to the client list.
     */
    private void acceptClientConnection() {
        try {
            ClientConnection client = new ClientConnection(serverSocket.accept());
            sendXmls(client);
            clientList.getClients().add(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the the three XML files to the specified client.
     * The three files are Race.xml, Regatta.xml and Boats.xml. Their order is not defined.
     *
     * @param client the client to send to
     * @throws IOException if an I/O exception occurs
     */
    private void sendXmls(ClientConnection client) throws IOException {
        //send regatta file
        File regattaXML = new File(this.getClass().getResource(regattaXMLPath).getFile());
        String content = new Scanner(regattaXML).useDelimiter("\\Z").next();
        client.sendMessage(content.getBytes(StandardCharsets.UTF_8));
        //send race file
        File raceXML = new File(this.getClass().getResource(raceXMLPath).getFile());
        content = new Scanner(raceXML).useDelimiter("\\Z").next();
        client.sendMessage(content.getBytes(StandardCharsets.UTF_8));
        //send boats file
        File boatsXML = new File(this.getClass().getResource(boatsXMLPath).getFile());
        content = new Scanner(boatsXML).useDelimiter("\\Z").next();
        client.sendMessage(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Closes any open client connections and closes the server
     */
    public void closeServer() {
        connectionListener.stopListening();

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
        acceptClientConnection();

        //connectionListener.run(); TODO make connection listener work
    }

    /**
     * Broadcasts a message to all connected clients
     *
     * @param message the message to broadcast
     * @see ClientConnection#sendMessage(byte[])
     */
    public void broadcast(byte[] message) {
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
                serverSocket.setSoTimeout(1000);
            } catch (SocketException e) {
                e.printStackTrace();
            }

            while (listening) {
                acceptClientConnection();
            }
        }

        public void stopListening() {
            listening = false;
        }
    }
}
