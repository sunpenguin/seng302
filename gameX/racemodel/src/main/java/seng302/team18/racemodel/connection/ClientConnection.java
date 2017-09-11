package seng302.team18.racemodel.connection;

import java.io.*;
import java.net.Socket;

/**
 * Holds a connection to a client.
 *
 * @see seng302.team18.racemodel.connection
 */
public class ClientConnection {
    private InputStream in;
    private OutputStream out;
    private Socket client;
    private Integer id;
    private boolean isSpectating;


    public ClientConnection(Socket socket) throws IOException {
        client = socket;
        out = new DataOutputStream(client.getOutputStream());
        in = socket.getInputStream();
        if (!in.markSupported()) {
            in = new BufferedInputStream(in);
        }
    }


    /**
     * Send a message to a client.
     * Returns false on failure to send the message. (Socket closed by remote)
     *
     * @param message the message to send.
     * @return if the message was sent.
     */
    public boolean sendMessage(byte[] message) {
        try {
            out.write(message);
            out.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public Socket getSocket() {
        return client;
    }


    public void close() throws IOException {
        in.close();
        out.close();
        client.close();
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * Returns true if the Socket has been closed.
     *
     * @return true if Socket is closed, false otherwise.
     */
    public boolean isClosed() {
        try {
            in.mark(1);
            boolean isClosed = in.read() == -1;
            in.reset();
            return isClosed;
        } catch (IOException e) {
            return true;
        }
    }

    public boolean isSpectaing() {
        return isSpectating == true;
    }

    public void setSpectating(boolean spectating) {
        isSpectating = spectating;
    }
}


