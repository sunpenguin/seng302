package seng302.team18.messageparsing;

import seng302.team18.util.ByteCheck;
import seng302.team18.model.Coordinate;

/**
 * Created by dhl25 on 10/04/17.
 */
public class AC35BoatLocationParser implements MessageBodyParser {


    @Override
    public MessageBody parse(byte[] bytes) { // more final declarations than actual code LOL
        final double MMPS_TO_KMPH = 36d / 10000d;
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
        final int SPEED_INDEX = 38;
        final int SPEED_LENGTH = 2;

        int sourceID = ByteCheck.byteToIntConverter(bytes, SOURCE_ID_INDEX, SOURCE_ID_LENGTH);
        double lat = ByteCheck.byteToIntConverter(bytes, LAT_INDEX, LAT_LENGTH) * BYTE_COORDINATE_TO_DOUBLE;
        double longitude = ByteCheck.byteToIntConverter(bytes, LONG_INDEX, LONG_LENGTH) * BYTE_COORDINATE_TO_DOUBLE;
        double heading = ByteCheck.byteToIntConverter(bytes, HEADING_INDEX, HEADING_LENGTH) * BYTE_HEADING_TO_DOUBLE;
        double speed = ByteCheck.byteToIntConverter(bytes, SPEED_INDEX, SPEED_LENGTH) * MMPS_TO_KMPH; // mm/s -> km/h
        return new AC35BoatLocationMessage(sourceID, new Coordinate(lat, longitude), heading, speed);
    }

}
