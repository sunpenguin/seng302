package seng302.team18.data;

import seng302.team18.util.ByteCheck;

/**
 * Created by dhl25 on 10/04/17.
 */
public class AC35MessageHeadParser implements MessageHeadParser {

    private final int HEADER_BYTE_SIZE = 15;
//    private final byte FIRST_SYNC_BYTE = 0x47;
//    private final byte SECOND_SYNC_BYTE = 0x83;
    private final int TYPE_INDEX = 2;
    private final int LEN_START_INDEX = 13;
    private final int LEN_LENGTH = 2;

    @Override
    public MessageHead parse(byte[] header) {
        int type = header[TYPE_INDEX];
        AC35MessageType messageType = AC35MessageType.from(type);
        int len = ByteCheck.ByteToIntConverter(header, LEN_START_INDEX, LEN_LENGTH);
//        Old Conversion here: ((header[13] & 0xff) << 8) | (header[14] & 0xff);
        return new AC35MessageHead(messageType, len);
    }

    @Override
    public int headerSize() {
        return HEADER_BYTE_SIZE;
    }
}
