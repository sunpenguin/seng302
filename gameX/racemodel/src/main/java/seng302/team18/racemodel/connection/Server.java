package seng302.team18.racemodel.connection;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Streaming server to connect test mock with clients.
 */
public class Server extends Observable {
    private final List<ClientConnection> clients = new ArrayList<>();
    private final ServerConnectionListener listener = new ServerConnectionListener();
    private static final int MAX_CLIENTS = 6;

    private ServerSocket serverSocket;
    private final int port;
    private ServerState state;

    public Server(int port) {
        this.port = port;

        try {
            serverSocket = ServerSocketFactory.getDefault().createServerSocket(port);

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                private ServerSocket socket = serverSocket;

                @Override
                public void run() {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }));

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
    public void open() {
        System.out.println("Stream opened successfully on port: " + port);

        acceptClientConnection();
        listener.start();
        state = ServerState.OPEN;
    }


    /**
     * Blocks while waiting for a client connection, setting up new connection when available.
     * Adding new client to the client list.
     * Increment the number that represents number of connected clients.
     */
    private void acceptClientConnection() {
        try {
            ClientConnection client = new ClientConnection(serverSocket.accept());
            clients.add(client);
            System.out.println("Player " + clients.size() + " joined!");
            setChanged();
            notifyObservers(client);
        } catch (IOException e) {
            // close();
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
        for (ClientConnection client : clients) {
            client.close();
        }
        clients.clear();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        state = ServerState.CLOSED;
        setChanged();
        notifyObservers(state);
    }


    /**
     * Broadcasts a message to all connected clients
     *
     * @param message the message to broadcast
     * @see ClientConnection#send(byte[])
     */
    public void broadcast(byte[] message) {
        if (message.length == 1) { //Scheduled messages should return {0} if there is an error when constructing them
            return;
        }
        List<Integer> toRemove = new ArrayList<>();
        for (int i = 0; i < clients.size(); i++) {
            ClientConnection client = clients.get(i);
            Integer id = client.getId();

            if (!client.send(message) || client.isClosed()) {
                toRemove.add(i);
                ExecutorService executor = Executors.newSingleThreadScheduledExecutor(); // For what ever reason the setChanged + notify takes forever
                executor.submit(() -> {
                    setChanged();
                    notifyObservers(id);
                });
                executor.shutdown();
            }
        }

        removeClients(toRemove);

        if (clients.isEmpty()) {
            close();
        }

    }


    /**
     * Removes all clients at the given indices
     *
     * @param indices of the clients to remove
     */
    private void removeClients(List<Integer> indices) {
        List<ClientConnection> newClients = new ArrayList<>();
        for (int i = 0; i < clients.size(); i++) {
            if (!indices.contains(i)) {
                newClients.add(clients.get(i));
            }
        }
        clients.clear();
        clients.addAll(newClients);
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
                if (clients.size() < MAX_CLIENTS) {
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


    public ServerState getState() {
        return state;
    }

}
