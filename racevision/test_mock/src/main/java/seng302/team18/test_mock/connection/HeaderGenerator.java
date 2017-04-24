package seng302.team18.test_mock.connection;

import seng302.team18.data.AC35MessageHeadParser;
import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

/**
 * Class to generate headers for messages to be sent via the server.
 */
public class HeaderGenerator {

    /**
     * A method used to generate the header for a message of a certain type.
     * @param type The type of the message the header is generated for.
     */
    public static byte[] generateHeader(int type, short lengthOfMessage) throws IOException {

        byte syncByte1 = 0x47;

        byte syncByte2 = (byte) 0x83;//TODO make sure -125 is okay, might need to be 131

        byte messageType = (byte) type;

        Timestamp time = new Timestamp(System.currentTimeMillis());
        long timestamp =  time.getTime(); //number of milliseconds since January 1, 1970, 00:00:00 GMT
        byte[] timestampBytes =  ByteCheck.longToByteArray(timestamp);
        timestampBytes = Arrays.copyOfRange(timestampBytes, 2, 8); //Shave the first 2 bytes off

        // TODO: How to make reasonable sourceID?
        byte[] sourceID = new byte[4];
        sourceID[0] = 58;
        sourceID[1] = 94;
        sourceID[2] = 123;
        sourceID[3] = 93;

        byte[] messageLen = ByteCheck.shortToByteArray(lengthOfMessage);

        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
        outputSteam.write(syncByte1);
        outputSteam.write(syncByte2);
        outputSteam.write(messageType);
        outputSteam.write(timestampBytes);
        outputSteam.write(sourceID);
        outputSteam.write(messageLen);

        byte header[] = outputSteam.toByteArray();

        return ByteCheck.convertToLittleEndian(header,15);
    }

}
