package seng302.team18.test_mock.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Observable;

/**
 * Streaming server to connect test mock with clients.
 */
public class Server extends Observable {
    private final ClientList clientList = new ClientList();
    private final ServerConnectionListener listener = new ServerConnectionListener();
    private static final int MAX_CLIENT_CONNECTION = 6;

    private ServerSocket serverSocket;
    private final int PORT;



    public Server(int port) {
        this.PORT = port;
    }


    /**
     * Opens the server.
     * Blocks waiting for the first client connection, then opens a second thread to listen for subsequent connections
     */
    public void openServer() {
        System.out.println(1);
        try {
            System.out.println(2);
            serverSocket = new ServerSocket(PORT);
            System.out.println(3);
        } catch (IOException e) {
            System.out.println(4);
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

            setChanged();
            notifyObservers(client);

        } catch (SocketTimeoutException e) {
            // The time out expired, no big deal
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void stopAcceptingConnections() {
        listener.stopListening();
    }


    /**
     * Closes any open client connections and closes the server
     */
    public void closeServer() {
        for (ClientConnection client : clientList.getClients()) {
            try {
                client.close();
            } catch (IOException e) {
                System.err.println("Failed to close client connection to: " + client.getSocket().getInetAddress().toString());
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
    private class ServerConnectionListener extends Thread {
        private boolean listening = true;

        @Override
        public void run() {
            try {
                serverSocket.setSoTimeout(500); // TODO ask Anton about
            } catch (SocketException e) {
                e.printStackTrace();
            }

            while (listening) {
                if (clientList.getClients().size() < MAX_CLIENT_CONNECTION) {
                    acceptClientConnection();
                } else {
                    listening = false;
                }
            }
        }

        public void stopListening() {
            listening = false;
        }
    }
}
