package seng302.team18.test_mock.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Holds a connection to a client.
 *
 * @see seng302.team18.test_mock.connection
 */
public class ClientConnection {
    public final static int MAX_FAILURES = 5;
    private DataOutputStream out;
    private Socket client;
    private int nFailures;

    public ClientConnection(Socket socket) throws IOException {
        client = socket;
        out = new DataOutputStream(client.getOutputStream());
    }

    /**
     * Send a message to a client.
     * On failure, increments the number of failed attempts, on success, resets.
     *
     * @param message the message to send
     */
    public void sendMessage(byte[] message) {
        try {
            out.write(message);
            out.flush(); // TODO is this helpful?
            nFailures = 0;
        } catch (IOException e) {
            System.err.println("Unable to send message to client: " + client.getInetAddress().toString());
            ++nFailures;
        }
    }

    public Socket getClient() {
        return client;
    }

    public void close() throws IOException {
        out.close();
        client.close();
    }

    public int getnFailures() {
        return nFailures;
    }
}
