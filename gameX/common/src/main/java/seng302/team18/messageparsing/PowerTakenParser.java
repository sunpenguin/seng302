package seng302.team18.messageparsing;

import com.google.common.io.ByteStreams;
import seng302.team18.message.MessageBody;
import seng302.team18.message.PowerTakenMessage;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dhl25 on 3/09/17.
 */
public class PowerTakenParser implements MessageBodyParser {
    
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
        final int BOAT_ID_INDEX = 0;
        final int BOAT_ID_LENGTH = 4;
        final int POWER_ID_INDEX = 4;
        final int POWER_ID_LENGTH = 4;
        final int DURATION_INDEX = 8;
        final int DURATION_LENGTH = 4;

        int boatId = ByteCheck.byteToInt(bytes, BOAT_ID_INDEX, BOAT_ID_LENGTH);
        int powerId = ByteCheck.byteToInt(bytes, POWER_ID_INDEX, POWER_ID_LENGTH);
        double duration = ByteCheck.byteToInt(bytes, DURATION_INDEX, DURATION_LENGTH);

        return new PowerTakenMessage(boatId, powerId, duration);
    }
}
