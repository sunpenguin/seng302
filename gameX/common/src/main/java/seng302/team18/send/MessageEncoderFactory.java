package seng302.team18.send;

/**
 * Factory for creating MessageEncoders.
 */
public interface MessageEncoderFactory {


    /**
     * Returns a MessageEncoder to convert messages to byte arrays.
     * The type of the composer depends on the id of the message.
     *
     * @param id of the message to be composed
     * @return Composer to convert message to bytes
     */
    MessageEncoder getEncoder(int id);


}
