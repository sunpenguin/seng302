package seng302.team18.message;

/**
 * Created by David-chan on 10/04/17.
 */
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
