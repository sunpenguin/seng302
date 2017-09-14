package seng302.team18.racemodel.connection;

import seng302.team18.message.RequestType;

import java.io.*;
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
        out = new DataOutputStream(this.socket.getOutputStream());
        in = socket.getInputStream();
        if (!in.markSupported()) {
            in = new BufferedInputStream(in);
        }
    }


    /**
     * Send a message to a socket.
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
//            e.printStackTrace();
            return false;
        }
    }


    public Socket getSocket() {
        return socket;
    }


    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
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
//        try {
//            in.mark(1);
//            boolean isClosed = in.read() == -1;
//            in.reset();
//            return isClosed;
//        } catch (IOException e) {
//            return true;
//        }
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
}


