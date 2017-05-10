package seng302.team18.messageparsing;

import seng302.team18.message.AC35MessageHead;
import seng302.team18.message.AC35MessageType;
import seng302.team18.message.MessageHead;
import seng302.team18.util.ByteCheck;

/**
 * A parser which reads information from a byte stream and creates message objects representing header information.
 *
 * Created by dhl25 on 10/04/17.
 */
public class AC35MessageHeadParser implements MessageHeadParser {

    private final int HEADER_BYTE_SIZE = 15;
//    private final byte FIRST_SYNC_BYTE = 0x47;
//    private final byte SECOND_SYNC_BYTE = 0x83;
    private final int TYPE_INDEX = 2;
    private final int LEN_START_INDEX = 13;
    private final int LEN_LENGTH = 2;

    /**
     * @param header The byte array containing information from the header of a streamed message.
     * @return A message head object containing information read from the byte stream.
     */
    @Override
    public MessageHead parse(byte[] header) {
        int type = header[TYPE_INDEX];
        int sourceID = ByteCheck.byteToIntConverter(header, 9, 4);
        AC35MessageType messageType = AC35MessageType.from(type);

        int len = ByteCheck.byteToIntConverter(header, LEN_START_INDEX, LEN_LENGTH);

        return new AC35MessageHead(messageType, len);
    }

    @Override
    public int headerSize() {
        return HEADER_BYTE_SIZE;
    }
}
