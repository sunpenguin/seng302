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

//        int value = Math.toIntExact(checksumValue);
//        byte b[] = new byte[4];
//
//        ByteBuffer buf = ByteBuffer.wrap(b);
//        buf.putInt(value);
//        System.out.println(b);

//        System.out.println(message);
        //System.out.println(checksumValue);

//        byte[] bytes = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(checksumValue).array();
//        System.out.println(bytes+" length:8");

        byte[] value = toByteArray(checksumValue);
        //System.out.println(value+" length: "+value.length);
//        System.out.println(ByteCheck.byteToLongConverter(bytes, 0, 4));

//        byte[] check = ByteCheck.convertLongTo6ByteArray(checksumValue);
//        System.out.println(check);
//        System.out.println(ByteCheck.convertToLittleEndian(check, 4));
//        byte[] result = new byte[4];
//        System.arraycopy(string.getBytes(), 0, result, 100 - string.length(), string.length());
//        byte[] bytes = ByteBuffer.allocate(4).putLong(checksumValue).array();
//        System.out.println(bytes);


        // return a byte array
//        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
//        buffer.putLong(checksumValue);
//
//        byte[] result = new byte[4];
//        System.arraycopy(buffer.array(), 0, result, 0, 4);
//
//        for(int i = 0; i < result.length / 2; i++)
//        {
//            byte temp = result[i];
//            result[i] = result[result.length - i - 1];
//            result[result.length - i - 1] = temp;
//        }
//        System.out.println(result);
        return value;
    }

    /*
    Return a byte array of size 4.
     */
    public static byte[] toByteArray(long value)
    {
        byte[] ret = new byte[4];
        ret[3] = (byte) ((value >> (0*8) & 0xFF));
        ret[2] = (byte) ((value >> (1*8) & 0xFF));
        ret[1] = (byte) ((value >> ((2)*8) & 0xFF));
        ret[0] = (byte) ((value >> ((3)*8) & 0xFF));
        return ret;
    }
}
