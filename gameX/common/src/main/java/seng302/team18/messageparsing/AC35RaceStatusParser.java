package seng302.team18.messageparsing;

import com.google.common.io.ByteStreams;
import seng302.team18.message.AC35BoatStatusMessage;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.message.BoatStatus;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * A parser which reads information from a byte stream and creates message objects representing race status information.
 */
public class AC35RaceStatusParser implements MessageBodyParser {

    /**
     * Reads a stream and associates the information read with a boat location message.
     *
     * @param stream holds information about the race status.
     * @return A message holding information about the race status.
     */
    @Override
    public MessageBody parse(InputStream stream) {
        try {
            return parse(ByteStreams.toByteArray(stream));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Reads a byte array and associates the information read with a race status message.
     *
     * @param bytes A list of bytes holding information about the race status.
     * @return A message holding information about the race status.
     */
    @Override
    public MessageBody parse(byte[] bytes) {
        final double BYTE_HEADING_TO_DOUBLE = 360.0 / 65536.0;
        final int CURRENT_TIME_INDEX = 1;
        final int CURRENT_TIME_LENGTH = 6;
        final int RACE_STATUS_INDEX = 11;
        final int RACE_STATUS_LENGTH = 1;
        final int START_TIME_INDEX = 12;
        final int START_TIME_LENGTH = 6;
        final int WIND_DIRECTION_INDEX = 18;
        final int WIND_DIRECTION_LENGTH = 2;
        final int WIND_SPEED_INDEX = 20;
        final int WIND_SPEED_LENGTH = 2;
        final int BOAT_SOURCEID_INDEX =  24;
        final int BOAT_SOURCEID_LENGTH = 4;
        final int BOAT_STATUS_INDEX = 28;
        final int BOAT_STATUS_LENGTH = 1;
        final int ESTIMATED_TIME_AT_NEXT_MARK_INDEX = 32;
        final int ESTIMATED_TIME_AT_NEXT_MARK_LENGTH = 6;

        final int SINGLE_BOAT_STATUS_LENGTH = 20;

        List<AC35BoatStatusMessage> boatStates = new ArrayList<>();
        long currentTime = ByteCheck.byteToLong(bytes, CURRENT_TIME_INDEX, CURRENT_TIME_LENGTH);
        int raceStatus = ByteCheck.byteToInt(bytes, RACE_STATUS_INDEX, RACE_STATUS_LENGTH);
        long startTime = ByteCheck.byteToLong(bytes, START_TIME_INDEX, START_TIME_LENGTH);
        double windDirection = ByteCheck.byteToInt(bytes, WIND_DIRECTION_INDEX, WIND_DIRECTION_LENGTH) * BYTE_HEADING_TO_DOUBLE;
        double windSpeed = ByteCheck.byteToInt(bytes, WIND_SPEED_INDEX, WIND_SPEED_LENGTH);
        int i = 0;
        while (BOAT_SOURCEID_INDEX + (SINGLE_BOAT_STATUS_LENGTH * i) < bytes.length) {
            int boatID = ByteCheck.byteToInt(bytes, BOAT_SOURCEID_INDEX + (SINGLE_BOAT_STATUS_LENGTH * i), BOAT_SOURCEID_LENGTH);
            BoatStatus status = BoatStatus.from(
                    ByteCheck.byteToInt(bytes, BOAT_STATUS_INDEX + (SINGLE_BOAT_STATUS_LENGTH * i), BOAT_STATUS_LENGTH));

            int leg = ByteCheck.byteToInt(bytes, 29 + (SINGLE_BOAT_STATUS_LENGTH * i), 1);
            long estimatedTime = ByteCheck.byteToLong(bytes, ESTIMATED_TIME_AT_NEXT_MARK_INDEX + (SINGLE_BOAT_STATUS_LENGTH * i), ESTIMATED_TIME_AT_NEXT_MARK_LENGTH);
            AC35BoatStatusMessage boatStatusMessage = new AC35BoatStatusMessage(boatID, leg, status, estimatedTime);
            boatStates.add(boatStatusMessage);
            i += 1;
        }
        return new AC35RaceStatusMessage(currentTime, raceStatus, startTime, windDirection, windSpeed, boatStates);
    }
}
