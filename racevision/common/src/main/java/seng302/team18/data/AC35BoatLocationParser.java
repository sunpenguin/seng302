package seng302.team18.data;

import seng302.team18.model.Coordinate;

import java.nio.ByteBuffer;

/**
 * Created by dhl25 on 10/04/17.
 */
public class AC35BoatLocationParser implements MessageBodyParser {
    final double MMPS_TO_KMPH = 277.778;
    final double BYTE_COORDINATE_TO_DOUBLE = 180.0 / 2147483648.0;
    final double BYTE_HEADING_TO_DOUBLE = 360.0 / 65536.0;
    final int SOURCE_ID_INDEX = 7;
    final int SOURCE_ID_LENGTH = 4;
    final int LAT_INDEX = 16;
    final int LAT_LENGTH = 4;
    final int LONG_INDEX = 20;
    final int LONG_LENGTH = 4;
    final int HEADING_INDEX = 28;
    final int HEADING_LENGTH = 2;
    final int SPEED_INDEX = 34;
    final int SPEED_LENGTH = 2;

    @Override
    public MessageBody parse(byte[] bytes) { // more final declarations than actual code LOL
        // TODO Change these to use ByteCheck method
        int sourceID = ByteBuffer.wrap(bytes, SOURCE_ID_INDEX, SOURCE_ID_LENGTH).getInt();
        double lat = ByteBuffer.wrap(bytes, LAT_INDEX, LAT_LENGTH).getInt() * BYTE_COORDINATE_TO_DOUBLE;
        double longitude = ByteBuffer.wrap(bytes, LONG_INDEX, LONG_LENGTH).getInt() * BYTE_COORDINATE_TO_DOUBLE;
        double heading = ByteBuffer.wrap(bytes, HEADING_INDEX, HEADING_LENGTH).getInt() * BYTE_HEADING_TO_DOUBLE;
        double speed = ByteBuffer.wrap(bytes, SPEED_INDEX, SPEED_LENGTH).getInt() * MMPS_TO_KMPH; // mm/s -> km/h
        return new AC35BoatLocationMessage(sourceID, new Coordinate(lat, longitude), heading, speed);
    }

}
