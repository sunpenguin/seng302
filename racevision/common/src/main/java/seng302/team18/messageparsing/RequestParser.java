package seng302.team18.messageparsing;

import com.google.common.io.ByteStreams;
import seng302.team18.message.AC35BoatLocationMessage;
import seng302.team18.message.AcceptanceMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.model.Coordinate;
import seng302.team18.util.ByteCheck;
import seng302.team18.util.SpeedConverter;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dhl25 on 7/07/17.
 */
public class RequestParser implements MessageBodyParser {

    @Override
    public MessageBody parse(InputStream stream) {
        try {
            return parse(ByteStreams.toByteArray(stream));
        } catch (IOException e) {
            return null;
        }
    }

    public MessageBody parse(byte[] bytes) {
        final int FIELD_INDEX = 0;
        final int FIELD_LENGTH = 4;

        boolean field = ByteCheck.byteToInt(bytes, FIELD_INDEX, FIELD_LENGTH) == 1;

        return new RequestMessage(field);
    }
}
