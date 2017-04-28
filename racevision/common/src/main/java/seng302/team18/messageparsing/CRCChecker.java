package seng302.team18.data;

import seng302.team18.util.ByteCheck;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.CRC32;

/**
 * Created by dhl25 on 10/04/17.
 */
public class CRCChecker implements MessageErrorDetector {


    @Override
    public int errorCheckSize() {
        return 4;
    }

    @Override
    public Boolean isValid(byte[] checkSum, byte[] messageBytes, byte[] headerBytes) {
        CRC32 actualCRC = new CRC32();
        actualCRC.update(headerBytes);
        actualCRC.update(messageBytes);
        long expectedCRCValue = Integer.toUnsignedLong(ByteCheck.byteToIntConverter(checkSum, 0, 4));
        return expectedCRCValue == actualCRC.getValue();
    }
}