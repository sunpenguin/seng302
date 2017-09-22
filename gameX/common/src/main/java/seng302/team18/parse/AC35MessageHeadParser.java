package seng302.team18.parse;

import seng302.team18.message.AC35MessageHead;
import seng302.team18.message.AC35MessageType;
import seng302.team18.message.MessageHead;
import seng302.team18.util.ByteCheck;

/**
 * A parser which reads information from a byte stream and creates message objects representing header information.
 */
public class AC35MessageHeadParser implements MessageHeadParser {


    /**
     * Reads a byte array and associates the information read with a message head.
     *
     * @param header The byte array containing information from the header of a streamed message.
     * @return A message head object containing information read from the byte stream.
     */
    @Override
    public MessageHead parse(byte[] header) {
        final int TYPE_INDEX = 2;
        final int LEN_START_INDEX = 13;
        final int LEN_LENGTH = 2;
        int type = header[TYPE_INDEX];
        int len = ByteCheck.byteToInt(header, LEN_START_INDEX, LEN_LENGTH);
        AC35MessageType messageType = AC35MessageType.from(type);

        return new AC35MessageHead(messageType, len);
    }


    /**
     * Gets the size of the header.
     *
     * @return the size of the header.
     */
    @Override
    public int headerSize() {
        return 15;
    }
}
