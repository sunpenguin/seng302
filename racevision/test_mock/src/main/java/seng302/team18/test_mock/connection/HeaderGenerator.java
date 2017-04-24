package seng302.team18.test_mock.connection;

import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
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
    public static void generateHeader(byte type, int lengthOfMessage) throws IOException {

        byte syncByte1 = 0x47;

        byte syncByte2 = (byte)0x83;//TODO make sure -125 is okay, might need to be 131

        byte messageType = type;

        Timestamp time = new Timestamp(System.currentTimeMillis());
        long timestamp =  time.getTime(); //number of milliseconds since January 1, 1970, 00:00:00 GMT
        byte[] timestampBytes =  ByteCheck.longToByteArray(timestamp);
        timestampBytes = Arrays.copyOfRange(timestampBytes, 2, 8); //Shave the first 2 bytes off



        byte[] messageLen = ByteCheck.intToByteArray(lengthOfMessage);
        messageLen =  Arrays.copyOfRange(messageLen, 2, 4);

        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
        outputSteam.write(syncByte1);
        outputSteam.write(syncByte2);
        outputSteam.write(messageType);
        outputSteam.write(timestampBytes);
        outputSteam.write(messageLen);

        byte header[] = outputSteam.toByteArray();
        //String s = new String(header);
        //System.out.println(s);


    }

    public static int unsignedToBytes(int b) {
        return b & 0xFF;
    }


}
