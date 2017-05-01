package seng302.team18.test_mock.connection;

import seng302.team18.util.ByteCheck;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class CRCGenerator {

    /*
    Calculate CRC32 checksum for header and message body.
     */
    public static byte[] generateCRC(byte[] message) throws IOException {

        // calculate the checksum for given message
//        Checksum checksum = new CRC32();
//        checksum.update(message, 0, message.length);
//        long checksumValue = checksum.getValue();

        int crc  = 0xFFFFFFFF;       // initial contents of LFBSR
        int poly = 0xEDB88320;   // reverse polynomial

        for (byte b : message) {
            int temp = (crc ^ b) & 0xff;

            // read 8 bits one at a time
            for (int i = 0; i < 8; i++) {
                if ((temp & 1) == 1) temp = (temp >>> 1) ^ poly;
                else                 temp = (temp >>> 1);
            }
            crc = (crc >>> 8) ^ temp;
        }

        // flip bits
        crc = ~crc;
//        System.out.println(crc);
        byte[] value = ByteCheck.intToByteArray(crc);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(value);
        byte[] crcValue = outputStream.toByteArray();
//        System.out.println(ByteCheck.byteToIntConverter(value, 0, 4));
//
//        System.out.println("CRC32 (via direct calculation) = " + Integer.toHexString(crc));

//        String s = "";
//        for (Byte b: message) {
//            s += b.toString();
//        }
//        System.out.println(s);

//        byte[] bytes = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(checksumValue).array();
//        System.out.println(bytes+" length:8");

//        String check = Long.toHexString(checksumValue);
//        byte[] crc = check.getBytes();
//        System.out.println(check);
//        byte[] value = new byte[4];
//        value[0] = (byte) (crc[0] + crc[1]);
//        value[1] = (byte) (crc[2] + crc[3]);
//        value[2] = (byte) (crc[4] + crc[5]);
//        value[3] = (byte) (crc[6] + crc[7]);
//        System.out.println(value);

//        System.out.println(c);
//        System.out.println(crcValue);
//        ByteCheck.byteToLongConverter(crcValue,0,)

//        byte[] value = toByteArray(checksumValue);
//        System.out.println(value+" length: "+value.length);

//        String va = "";
//        for (Byte v: value) {
//            va += v.toString();
//            System.out.println(v);
//        }
//        System.out.println(va);
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
        return ByteCheck.convertToLittleEndian(crcValue, 4);
    }

//    /*
//    Return a byte array of size 4 given a long.
//     */
//    public static byte[] toByteArray(long value)
//    {
//        byte[] ret = new byte[4];
//        ret[3] = (byte) ((value >> (0*8) & 0xFF));
//        ret[2] = (byte) ((value >> (1*8) & 0xFF));
//        ret[1] = (byte) ((value >> ((2)*8) & 0xFF));
//        ret[0] = (byte) ((value >> ((3)*8) & 0xFF));
//        return ret;
//    }
}
