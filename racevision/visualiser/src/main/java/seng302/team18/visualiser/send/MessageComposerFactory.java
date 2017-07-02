package seng302.team18.visualiser.send;

import seng302.team18.message.MessageBody;

/**
 * Created by David-chan on 2/07/17.
 */
public interface MessageComposerFactory {


//    /**
//     * Creates a message header to be sent from a MessageBody
//     *
//     * @param message to be encoded
//     * @return header of a message as a byte array
//     */
    MessageComposer getComposer(int id);


}
