package seng302.team18.parse;

import com.google.common.io.ByteStreams;
import seng302.team18.message.AC35YachtEventMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.YachtEventCode;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.io.InputStream;

/**
 * A parser which reads information from a stream and creates messages representing information about the yacht event.
 */
public class AC35YachtEventParser implements MessageBodyParser {

    @Override
    public MessageBody parse(InputStream stream) {
        try {
            return parse(ByteStreams.toByteArray(stream));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public MessageBody parse(byte[] bytes) {
        final int TIME_INDEX = 1;
        final int TIME_LENGTH = 6;
        final int ID_INDEX = 13;
        final int ID_LENGTH = 4;
        final int EVENT_INDEX = 21;

        long time = ByteCheck.byteToLong(bytes, TIME_INDEX, TIME_LENGTH);
        int boatId = ByteCheck.byteToInt(bytes, ID_INDEX, ID_LENGTH);
        YachtEventCode code = YachtEventCode.ofCode(bytes[EVENT_INDEX]);
        return new AC35YachtEventMessage(time, boatId, code);
    }
}
