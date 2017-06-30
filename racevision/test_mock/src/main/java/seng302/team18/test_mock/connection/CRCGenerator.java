package seng302.team18.test_mock.connection;

import seng302.team18.util.ByteCheck;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CRCGenerator {

    /**
     * Calculate CRC32 checksum for header and message body.
     */
    public static byte[] generateCRC(byte[] message) throws IOException {

        // calculate the checksum for given message

        int crc  = 0xFFFFFFFF;       // initial contents of LFBSR
        int poly = 0xEDB88320;   // reverse polynomial

        for (byte b : message) {
            int temp = (crc ^ b) & 0xff;

            // read 8 bits one at a time
            for (int i = 0; i < 8; i++) {
                if ((temp & 1) == 1) {
                    temp = (temp >>> 1) ^ poly;
                } else {
                    temp = (temp >>> 1);
                }
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
        return ByteCheck.littleEndian(crcValue, 4);
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
