package seng302.team18.test_mock.connection;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

//TODO verify that this is actually thread safe

/**
 * Thread-safe list of client connections
 */
class ClientList {
    private volatile List<ClientConnection> clients = new ArrayList<>();

    public synchronized List<ClientConnection> getClients() {
        return clients;
    }

    /**
     * Removes all connections where the number of consecutive failed connections exceeds the threshold
     *
     * @see seng302.team18.test_mock.connection.ClientConnection#MAX_FAILURES
     */
    public synchronized void pruneConnections() {
        ListIterator<ClientConnection> iter = clients.listIterator();
        while (iter.hasNext()) {
            if (iter.next().getnFailures() > ClientConnection.MAX_FAILURES) {
                iter.remove();
            }
        }
    }
}
