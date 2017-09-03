package seng302.team18.messageparsing;

import com.google.common.io.ByteStreams;
import seng302.team18.message.MessageBody;
import seng302.team18.message.PowerType;
import seng302.team18.message.PowerUpMessage;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dhl25 on 3/09/17.
 */
public class PowerUpParser implements MessageBodyParser {

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
        final int LAT_INDEX = 0;
        final int LAT_LENGTH = 4;
        final int LONG_INDEX = 4;
        final int LONG_LENGTH = 4;
        final int RADIUS_INDEX = 8;
        final int RADIUS_LENGTH = 2;
        final int TIMEOUT_INDEX = 10;
        final int TIMEOUT_LENGTH = 6;
        final int TYPE_INDEX = 16;
        // final int TYPE_LENGTH = 1;
        final int DURATION_INDEX = 17;
        final int DURATION_LENGTH = 4;

        double lat = ByteCheck.byteToInt(bytes, LAT_INDEX, LAT_LENGTH) * BYTE_COORDINATE_TO_DOUBLE;
        double lon = ByteCheck.byteToInt(bytes, LONG_INDEX, LONG_LENGTH) * BYTE_COORDINATE_TO_DOUBLE;
        double radius = ByteCheck.byteToShort(bytes, RADIUS_INDEX, RADIUS_LENGTH) / 1000d;
        long timeout = ByteCheck.byteToLong(bytes, TIMEOUT_INDEX, TIMEOUT_LENGTH);
        PowerType type = PowerType.from((int) bytes[TYPE_INDEX]);
        double duration = ByteCheck.byteToInt(bytes, DURATION_INDEX, DURATION_LENGTH) * 1000d;

        return new PowerUpMessage(lat, lon, radius, timeout, type, duration);
    }

}
