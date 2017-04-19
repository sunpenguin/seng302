package seng302.team18.data;

import seng302.team18.model.Coordinate;
import seng302.team18.util.ByteCheck;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by jds112 on 19/04/17.
 */
public class AC35RaceStatusParser implements MessageBodyParser {

    final int CURRENT_TIME_INDEX = 1;
    final int CURRENT_TIME_LENGTH = 6;
    final int START_TIME_INDEX = 12;
    final int START_TIME_LENGTH = 6;

    @Override
    public MessageBody parse(byte[] bytes) { // more final declarations than actual code LOL

        long currentTime = ByteCheck.byteToLongConverter(bytes, CURRENT_TIME_INDEX, CURRENT_TIME_LENGTH);
        long startTime = ByteCheck.byteToLongConverter(bytes, START_TIME_INDEX, START_TIME_LENGTH);
        return new AC35RaceStatusMessage(currentTime, startTime);
    }
}
