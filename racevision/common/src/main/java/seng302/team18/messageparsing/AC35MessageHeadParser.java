package seng302.team18.messageparsing;

import seng302.team18.message.AC35MessageHead;
import seng302.team18.message.AC35MessageType;
import seng302.team18.message.MessageHead;
import seng302.team18.util.ByteCheck;

/**
 * A parser which reads information from a byte stream and creates message objects representing header information.
 */
public class AC35MessageHeadParser implements MessageHeadParser {

    private final int HEADER_BYTE_SIZE = 15;
    private final int TYPE_INDEX = 2;
    private final int LEN_START_INDEX = 13;
    private final int LEN_LENGTH = 2;

    /**
     * Reads a byte array and associates the information read with a message head.
     *
     * @param header The byte array containing information from the header of a streamed message.
     * @return A message head object containing information read from the byte stream.
     */
    @Override
    public MessageHead parse(byte[] header) {
        int type = header[TYPE_INDEX];
        AC35MessageType messageType = AC35MessageType.from(type);

        int len = ByteCheck.byteToIntConverter(header, LEN_START_INDEX, LEN_LENGTH);

        return new AC35MessageHead(messageType, len);
    }

    /**
     * Gets the size of the header.
     *
     * @return the size of the header.
     */
    @Override
    public int headerSize() {
        return HEADER_BYTE_SIZE;
    }
}
