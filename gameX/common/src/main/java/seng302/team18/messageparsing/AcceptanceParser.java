package seng302.team18.messageparsing;

import com.google.common.io.ByteStreams;
import seng302.team18.message.AcceptanceMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestType;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.io.InputStream;

/**
 * Parser for RegistrationMessages
 */
public class AcceptanceParser implements MessageBodyParser {


    public AcceptanceParser() {}

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
        final int SOURCE_ID_INDEX = 0;
        final int SOURCE_ID_LENGTH = 4;
        final int STATUS_INDEX = 4;
        final int STATUS_LENGTH = 1;
        int sourceID = ByteCheck.byteToInt(bytes, SOURCE_ID_INDEX, SOURCE_ID_LENGTH);
        RequestType status = RequestType.from(ByteCheck.byteToInt(bytes, STATUS_INDEX, STATUS_LENGTH));

        return new AcceptanceMessage(sourceID, status);
    }
}
