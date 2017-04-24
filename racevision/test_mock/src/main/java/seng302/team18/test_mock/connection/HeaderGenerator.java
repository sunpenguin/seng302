package seng302.team18.test_mock.connection;

import seng302.team18.data.AC35MessageHeadParser;
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


        byte[] sourceID = new byte[4];
        sourceID[0] = 58;
        sourceID[1] = 94;
        sourceID[2] = 123;
        sourceID[3] = 93;

        byte[] messageLen = ByteCheck.intToByteArray(1505131556);
        String s = new String(messageLen);

        int i = ByteCheck.byteToIntConverter(messageLen, 0, 4);

        messageLen =  Arrays.copyOfRange(messageLen, 2, 4);
        i = ByteCheck.byteToIntConverter(messageLen, 0, 2);


        int j = 10;
        byte[] byteArray = ByteCheck.intToByteArray(j);
        int k = ByteCheck.byteToIntConverter(byteArray, 0, 4);
        System.out.println(k);


        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
        outputSteam.write(syncByte1);
        outputSteam.write(syncByte2);
        outputSteam.write(messageType);
        outputSteam.write(timestampBytes);
        outputSteam.write(sourceID);
        outputSteam.write(messageLen);

        byte header[] = outputSteam.toByteArray();

        header = ByteCheck.convertToLittleEndian(header,15);

        AC35MessageHeadParser p = new AC35MessageHeadParser();
        p.parse(header);


    }

}
