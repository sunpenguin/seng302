package seng302.data;

import java.nio.ByteBuffer;

/**
 * Created by david on 4/10/17.
 */
public class AC35XMLHeadParser implements MessageHeadParser {

    @Override
    public MessageHead parse(byte[] header) {
        final int TYPE_INDEX = 9;
        final int LEN_INDEX = 12;
        final int LEN_LENGTH = 2;
        AC35XMLMessageType type = AC35XMLMessageType.from((int) header[TYPE_INDEX]);
        int len = ByteBuffer.wrap(header, LEN_INDEX, LEN_LENGTH).getInt();
        return new AC35XMLHead(type, len);
    }

    @Override
    public int headerSize() {
        final int HEADER_SIZE = 14;
        return HEADER_SIZE;
    }
}
