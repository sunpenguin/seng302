package seng302.team18.racemodel.connection;

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
    private final int maxClients;

    private ServerSocket serverSocket;
    private final int port;
    private boolean closeOnEmpty;

    public Server(int port, int maxClients) {
        this.port = port;
        this.maxClients = maxClients;

        try {
            serverSocket = ServerSocketFactory.getDefault().createServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.err.println("Exiting program");
            System.exit(-1);
        }
    }

    /**
     * Opens the server.
     * Blocks waiting for the first client connection, then opens a second thread to listen for subsequent connections
     */
    public void openServer() {
        System.out.println("Stream opened successfully on port: " + port);

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
            setChanged();
            notifyObservers(ServerState.CLOSED);
            close();
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
        stopAcceptingConnections();
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

        setChanged();
        notifyObservers(ServerState.CLOSED);
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
            Integer id = client.getId();
            if (!client.sendMessage(message)) {
                clients.remove(i);
                if (clients.isEmpty() && closeOnEmpty) {
                    close();
                } else {
                    setChanged();
                    notifyObservers(id);
                }
            }
        }
    }


    /**
     * Closes any clients with the given id.
     *
     * @param id of the client.
     */
    public void closeConnection(Integer id) {
        for (ClientConnection client : clients) {
            if (client.getId().equals(id)) {
                boolean isOpen = true;
                while (isOpen) {
                    try {
                        client.close();
                        isOpen = false;
                    } catch (IOException e) {}
                }
            }
        }
    }


    /**
     * Closes the server if there are no clients.
     *
     * @param close if there are no clients.
     */
    public void setCloseOnEmpty(boolean close) {
        closeOnEmpty = close;
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
                if (clients.size() < maxClients) {
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
