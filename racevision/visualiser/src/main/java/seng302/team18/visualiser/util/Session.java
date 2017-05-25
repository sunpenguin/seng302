package seng302.team18.visualiser.util;

import seng302.team18.messageparsing.SocketMessageReceiver;

/**
 * The session class to hold variables between controller switching.
 */
public class Session {

    private SocketMessageReceiver receiver;

    /**
     * Constructs a single instance of the session.
     * Static method.
     */
    private static Session instance = new Session();

    /**
     * Private constructor to construct the singleton session class.
     */
    private Session() {
    }

    /**
     * Getter to return the current instance of the session class.
     * Static method.
     * @return the current instance.
     */
    public static Session getInstance() {
        return instance;
    }

    public SocketMessageReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(SocketMessageReceiver receiver) {
        this.receiver = receiver;
    }
}
