package seng302.team18.data;

import seng302.team18.model.Coordinate;
import seng302.team18.util.ByteCheck;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

/**
 * Created by jds112 on 19/04/17.
 */
public class AC35RaceStatusParser implements MessageBodyParser {



    @Override
    public MessageBody parse(byte[] bytes) { // more final declarations than actual code LOL
        final double BYTE_HEADING_TO_DOUBLE = 360.0 / 65536.0;
        final int CURRENT_TIME_INDEX = 1;
        final int CURRENT_TIME_LENGTH = 6;
        final int RACE_STATUS_INDEX = 11;
        final int RACE_STATUS_LENGTH = 1;
        final int START_TIME_INDEX = 12;
        final int START_TIME_LENGTH = 6;
        final int WIND_DIRECTION_INDEX = 18;
        final int WIND_DIRECTION_LENGTH = 2;
        final int BOAT_SOURCEID_INDEX =  24;
        final int BOAT_SOURCEID_LENGTH = 4;
        final int BOAT_STATUS_INDEX = 28;
        final int BOAT_STATUS_LENGTH = 1;
        final int ESTIMATED_TIME_AT_NEXT_MARK_INDEX = 32;
        final int ESTIMATED_TIME_AT_NEXT_MARK_LENGTH = 6;

        final int SINGLE_BOAT_STATUS_LENGTH = 20;

        Map<Integer, List> allBoatStatus = new HashMap<>();
        long currentTime = ByteCheck.byteToLongConverter(bytes, CURRENT_TIME_INDEX, CURRENT_TIME_LENGTH);
        int raceStatus = ByteCheck.byteToIntConverter(bytes, RACE_STATUS_INDEX, RACE_STATUS_LENGTH);
        long startTime = ByteCheck.byteToLongConverter(bytes, START_TIME_INDEX, START_TIME_LENGTH);
        double windDirection = ByteCheck.byteToIntConverter(bytes, WIND_DIRECTION_INDEX, WIND_DIRECTION_LENGTH) * BYTE_HEADING_TO_DOUBLE;
        int i = 0;
        while (BOAT_SOURCEID_INDEX + (SINGLE_BOAT_STATUS_LENGTH * i) < bytes.length) {
            List boatList = new ArrayList();
            int boatID = ByteCheck.byteToIntConverter(bytes, BOAT_SOURCEID_INDEX + (SINGLE_BOAT_STATUS_LENGTH * i), BOAT_SOURCEID_LENGTH);
            int boatStatus = ByteCheck.byteToIntConverter(bytes, BOAT_STATUS_INDEX + (SINGLE_BOAT_STATUS_LENGTH * i), BOAT_STATUS_LENGTH);
            int leg = ByteCheck.byteToIntConverter(bytes, 29 + (SINGLE_BOAT_STATUS_LENGTH * i), 1);
//            System.out.println("Boat Status for " + boatID + ": " + boatStatus + " at leg " + leg);
            long estimatedTime = ByteCheck.byteToLongConverter(bytes, ESTIMATED_TIME_AT_NEXT_MARK_INDEX + (SINGLE_BOAT_STATUS_LENGTH * i), ESTIMATED_TIME_AT_NEXT_MARK_LENGTH);
            boatList.add(boatStatus);
            boatList.add(leg);
            boatList.add(estimatedTime);
            allBoatStatus.put(boatID, boatList);
            i += 1;
        }
        return new AC35RaceStatusMessage(currentTime, raceStatus, startTime, windDirection, allBoatStatus);
    }
}
