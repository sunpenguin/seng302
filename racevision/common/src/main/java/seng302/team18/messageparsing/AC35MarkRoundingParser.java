package seng302.team18.messageparsing;

import com.google.common.io.ByteStreams;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dhl25 on 25/04/17.
 */
public class AC35MarkRoundingParser implements MessageBodyParser {

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

        int boatId = ByteCheck.byteToIntConverter(bytes, ID_INDEX, ID_LENGTH);
        long time = ByteCheck.byteToLongConverter(bytes, TIME_INDEX, TIME_LENGTH);
        return new AC35MarkRoundingMessage(boatId, time);
    }

}
