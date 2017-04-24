package seng302.team18.test_mock.connection;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Class to generate headers for messages to be sent via the server.
 */
public class HeaderGenerator {

    /**
     * A method used to generate the header for a message of a certain type.
     * @param type The type of the message the header is generated for.
     */
    public static void generateHeader(int type, int lengthOfMessage){

        int syncByte1 = 0x47;

        int syncByte2 = 0x83;

        int messageType = type;

        Timestamp time = new Timestamp(System.currentTimeMillis());
        long timestamp=  time.getTime(); //number of milliseconds since January 1, 1970, 00:00:00 GMT
        System.out.println(timestamp);
        System.out.println(longToBytes(timestamp));


        int messageLen = lengthOfMessage;

        //byte[] header =

    }

    private static byte[] longToBytes(long longg) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(longg);
        return buffer.array();
    }

}
