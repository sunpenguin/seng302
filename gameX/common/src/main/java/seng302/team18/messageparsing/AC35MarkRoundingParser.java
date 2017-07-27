package seng302.team18.messageparsing;

import com.google.common.io.ByteStreams;
import seng302.team18.message.AC35MarkRoundingMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.io.InputStream;

/**
 * A parser which reads information from a byte stream and creates message objects representing mark rounding information.
 */
public class AC35MarkRoundingParser implements MessageBodyParser {

    /**
     * Reads a stream and associates the information read with a mark rounding message.
     *
     * @param stream holds information about a mark rounding.
     * @return A mark rounding message holding holding information about a mark rounding.
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
     * Reads a byte array and associates the information read with a mark rounding message.
     *
     * @param bytes A list of bytes holding information about a mark rounding.
     * @return A mark rounding message holding holding information about a mark rounding.
     */
    @Override
    public MessageBody parse(byte[] bytes) {
        final int TIME_INDEX = 1;
        final int TIME_LENGTH = 6;
        final int ID_INDEX = 13;
        final int ID_LENGTH = 4;

        int boatId = ByteCheck.byteToInt(bytes, ID_INDEX, ID_LENGTH);
        long time = ByteCheck.byteToLong(bytes, TIME_INDEX, TIME_LENGTH);
        return new AC35MarkRoundingMessage(boatId, time);
    }

}
