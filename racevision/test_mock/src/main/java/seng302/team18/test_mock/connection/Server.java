package seng302.team18.test_mock.connection;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * Streaming server to connect test mock with clients.
 */
public class Server extends Observable {
    private final List<ClientConnection> clients = new ArrayList<>();
    private final ServerConnectionListener listener = new ServerConnectionListener();
    private final int MAX_CLIENT_CONNECTION;

    private ServerSocket serverSocket;
    private final int PORT;

    public Server(int port, int MAX_CLIENT_CONNECTION) {
        this.PORT = port;
        this.MAX_CLIENT_CONNECTION = MAX_CLIENT_CONNECTION;
    }

    /**
     * Opens the server.
     * Blocks waiting for the first client connection, then opens a second thread to listen for subsequent connections
     */
    public void openServer() {
        try {
            serverSocket = ServerSocketFactory.getDefault().createServerSocket(PORT);
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
    private synchronized void acceptClientConnection() {
        try {
            ClientConnection client = new ClientConnection(serverSocket.accept());
            clients.add(client);
            System.out.println("Player " + clients.size() + " joined!");
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
     * Waits until all clients disconnects and then closes the server.
     * (Blocking)
     */
    public void close() {
        while (!clients.isEmpty()) {
            for (int i = 0; i < clients.size(); i++) {
                if (clients.get(i).isClosed()) {
                    clients.remove(i);
                }
            }
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Broadcasts a message to all connected clients
     *
     * @param message the message to broadcast
     * @see ClientConnection#sendMessage(byte[])
     */
    public void broadcast(byte[] message) {
        if (message.length == 1) { //Scheduled messages should return {0} if there is an error when constructing them
            return; // TODO move this out side of server
        }
        for (int i = 0; i < clients.size(); i++) {
            ClientConnection client = clients.get(i);
            if (!client.sendMessage(message)) {
                clients.remove(i);
//                setChanged();
//                notifyObservers(client.getId());
            }
        }
    }


    /**
     * Thread that listens for incoming connections.
     */
    private class ServerConnectionListener extends Thread {
        private boolean listening = true;

        @Override
        public void run() {
            try {
                serverSocket.setSoTimeout(500);
            } catch (SocketException e) {
                e.printStackTrace();
            }

            while (listening) {
                if (clients.size() < MAX_CLIENT_CONNECTION) {
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
