package seng302.team18.messageparsing;

import com.google.common.io.ByteStreams;
import seng302.team18.message.MessageBody;
import seng302.team18.message.ProjectileLocationMessage;
import seng302.team18.util.ByteCheck;
import seng302.team18.util.SpeedConverter;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by csl62 on 7/09/17.
 */
public class ProjectileParser implements MessageBodyParser{



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
        final double BYTE_COORDINATE_TO_DOUBLE = 180.0 / 2147483648.0;
        final double BYTE_HEADING_TO_DOUBLE = 360.0 / 65536.0;

        final int SOURCE_ID_INDEX = 0;
        final int SOURCE_ID_LENGTH = 4;
        final int LAT_INDEX = 4;
        final int LAT_LENGTH = 4;
        final int LONG_INDEX = 8;
        final int LONG_LENGTH = 4;
        final int HEADING_INDEX = 12;
        final int HEADING_LENGTH = 2;
        final int SPEED_INDEX = 14;
        final int SPEED_LENGTH = 4;

        int sourceID = ByteCheck.byteToInt(bytes, SOURCE_ID_INDEX, SOURCE_ID_LENGTH);
        double latitude = ByteCheck.byteToInt(bytes, LAT_INDEX, LAT_LENGTH) * BYTE_COORDINATE_TO_DOUBLE;
        double longitude = ByteCheck.byteToInt(bytes, LONG_INDEX, LONG_LENGTH) * BYTE_COORDINATE_TO_DOUBLE;
        double heading = ByteCheck.byteToInt(bytes, HEADING_INDEX, HEADING_LENGTH) * BYTE_HEADING_TO_DOUBLE;
        double speed = ByteCheck.byteToInt(bytes, SPEED_INDEX, SPEED_LENGTH);
        speed = new SpeedConverter().mmsToKnots(speed);

        return new ProjectileLocationMessage(sourceID, latitude, longitude, heading, speed);
    }



}
