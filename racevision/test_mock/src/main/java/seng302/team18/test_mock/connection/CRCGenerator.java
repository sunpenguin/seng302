package seng302.team18.test_mock.connection;

import seng302.team18.util.ByteCheck;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Created by lenovo on 2017/4/26.
 */
public class CRCGenerator {

    /*
    Calculate CRC32 checksum for header and message body.
     */
    public static byte[] generateCRC(byte[] message) {

        // calculate the checksum for given message
        Checksum checksum = new CRC32();
        checksum.update(message, 0, message.length);
        long checksumValue = checksum.getValue();

//        System.out.println(checksum);
        //System.out.println(checksumValue);

//        byte[] check = ByteCheck.convertLongTo6ByteArray(checksumValue);
//        System.out.println(check);
//        System.out.println(ByteCheck.convertToLittleEndian(check, 4));
//        byte[] result = new byte[4];
//        System.arraycopy(string.getBytes(), 0, result, 100 - string.length(), string.length());
//        byte[] bytes = ByteBuffer.allocate(4).putLong(checksumValue).array();
//        System.out.println(bytes);

        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(checksumValue);

        byte[] result = new byte[4];
        System.arraycopy(buffer.array(), 0, result, 0, 4);

        for(int i = 0; i < result.length / 2; i++)
        {
            byte temp = result[i];
            result[i] = result[result.length - i - 1];
            result[result.length - i - 1] = temp;
        }
        //System.out.println(result);
        return result;
    }
}
