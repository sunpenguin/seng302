package seng302.team18.messageparsing;

import seng302.team18.util.ByteCheck;

/**
 * A parser which reads information from an XML stream and creates message objects representing header information.
 *
 * Created by david on 4/10/17.
 */
public class AC35XMLHeadParser implements MessageHeadParser {

    /**
     *
     * @param header A list of bytes representing the header of an incoming message
     * @return A message head object holding information from the byte stream
     */
    @Override
    public MessageHead parse(byte[] header) {
        final int TYPE_INDEX = 9;
        final int LEN_INDEX = 12;
        final int LEN_LENGTH = 2;
        AC35MessageType type = AC35MessageType.from((int) header[TYPE_INDEX]);
        int len = ByteCheck.byteToIntConverter(header, LEN_INDEX, LEN_LENGTH);

        return new AC35XMLHead(type, len);
    }

    @Override
    public int headerSize() {
        final int HEADER_SIZE = 14;
        return HEADER_SIZE;
    }
}
