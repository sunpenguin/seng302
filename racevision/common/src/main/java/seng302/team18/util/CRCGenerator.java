package seng302.team18.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CRCGenerator {

    /**
     * Calculate CRC32 checksum for header and message body.
     *
     * @param message Byte array containing the message to generate the CRC for.
     * @return 4 Byte array of the calculated CRC.
     *
     * @throws IOException if error occurs when writing to a ByteArrayOutputStream.
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
        byte[] value = ByteCheck.intToByteArray(crc);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(value);
        byte[] crcValue = outputStream.toByteArray();
        return ByteCheck.littleEndian(crcValue, 4);
    }
}
