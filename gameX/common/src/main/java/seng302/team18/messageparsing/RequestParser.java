package seng302.team18.messageparsing;

import com.google.common.io.ByteStreams;
import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.io.InputStream;

/**
 * A parser which reads information from a byte stream and creates message objects representing a request message.
 */
public class RequestParser implements MessageBodyParser {

    /**
     * Reads a stream and associates the information read with a request message
     *
     * @param stream holds information about a client's request to join a race
     * @return a request message representing the information from the stream
     */
    @Override
    public MessageBody parse(InputStream stream) {
        try {
            return parse(ByteStreams.toByteArray(stream));
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * Reads a byte array and associates the information read with a request message
     *
     * @param bytes A list of bytes represent information about a client's request to join a race
     * @return A location message holding a client's request to join a race
     */
    @Override
    public MessageBody parse(byte[] bytes) {
        final int FIELD_INDEX = 0;
        final int FIELD_LENGTH = 4;

        boolean field = ByteCheck.byteToInt(bytes, FIELD_INDEX, FIELD_LENGTH) == 1;

        return new RequestMessage(field);
    }
}
