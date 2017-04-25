package seng302.team18.data;

import seng302.team18.model.Coordinate;
import seng302.team18.util.ByteCheck;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jds112 on 19/04/17.
 */
public class AC35RaceStatusParser implements MessageBodyParser {



    @Override
    public MessageBody parse(byte[] bytes) { // more final declarations than actual code LOL
        final int CURRENT_TIME_INDEX = 1;
        final int CURRENT_TIME_LENGTH = 6;
        final int START_TIME_INDEX = 12;
        final int START_TIME_LENGTH = 6;
        final int BOAT_SOURCEID_INDEX =  24;
        final int BOAT_SOURCEID_LENGTH = 4;
        final int ESTIMATED_TIME_AT_NEXT_MARK_INDEX = 32;
        final int ESTIMATED_TIME_AT_NEXT_MARK_LENGTH = 6;

        final int BOAT_STATUS_LENGTH = 20;

        Map<Integer, Long> boatStatus = new HashMap<>();
        long currentTime = ByteCheck.byteToLongConverter(bytes, CURRENT_TIME_INDEX, CURRENT_TIME_LENGTH);
        long startTime = ByteCheck.byteToLongConverter(bytes, START_TIME_INDEX, START_TIME_LENGTH);
        int i = 0;
        while (BOAT_SOURCEID_INDEX + (BOAT_STATUS_LENGTH * i) < bytes.length) {
            int boatID = ByteCheck.byteToIntConverter(bytes, BOAT_SOURCEID_INDEX + (BOAT_STATUS_LENGTH * i), BOAT_SOURCEID_LENGTH);
            long estimatedTime = ByteCheck.byteToLongConverter(bytes, ESTIMATED_TIME_AT_NEXT_MARK_INDEX + (BOAT_STATUS_LENGTH * i), ESTIMATED_TIME_AT_NEXT_MARK_LENGTH);
            boatStatus.put(boatID, estimatedTime);
            i += 1;
        }
        return new AC35RaceStatusMessage(currentTime, startTime, boatStatus);
    }
}
