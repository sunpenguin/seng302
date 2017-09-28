package seng302.team18.message;

public interface MessageHead {

    /**
     * Gets the getCode of the message type.
     *
     * @return the getCode of the message type.
     */
    int getType();

    /**
     * Gets the size of the message body.
     *
     * @return the size of the message body.
     */
    int bodySize();

}
