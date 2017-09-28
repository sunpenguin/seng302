package seng302.team18.racemodel.connection;

import seng302.team18.message.RequestType;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Holds a connection to a socket.
 *
 * @see seng302.team18.racemodel.connection
 */
public class ClientConnection {
    private InputStream in;
    private OutputStream out;
    private Socket socket;
    private Integer id;
    private RequestType requestType;


    public ClientConnection(Socket socket) throws IOException {
        this.socket = socket;
        out = socket.getOutputStream();
        in = socket.getInputStream();
        if (!in.markSupported()) {
            in = new BufferedInputStream(in);
        }
    }


    /**
     * Send a message to a socket.
     * Returns false on failure to encode the message. (Socket closed by remote)
     *
     * @param message the message to encode.
     * @return if the message was sent.
     */
    public boolean send(byte[] message) {
        try {
            out.write(message);
            out.flush();
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    public Socket getSocket() {
        return socket;
    }


    /**
     * Closes the client connection
     *
     */
    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            // pass
        }
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
        return socket.isClosed();
    }


    public RequestType getRequestType() {
        return requestType;
    }


    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }


    public boolean isPlayer() {
        return requestType.isRaceType();
    }


    @Override
    public String toString() {
        return "ClientConnection{" +
                "id=" + id +
                ", out=" + out +
                ", socket=" + socket +
                ", in=" + in +
                ", requestType=" + requestType +
                '}';
    }
}


