package seng302.team18.messageparsing;

import seng302.team18.util.ByteCheck;

import java.util.zip.CRC32;

/**
 * An error detector which checks the validity of a byte stream using a checksum.
 *
 * Created by dhl25 on 10/04/17.
 */
public class CRCChecker implements MessageErrorDetector {


    @Override
    public int errorCheckSize() {
        return 4;
    }

    /**
     * Takes the checksum, message and header from a message as byte array and checks that the given checksum is correct
     * by calculating a new checksum and comparing the two. An error has occurred if they are not equal.
     * @param checkSum the checksum given in the input stream
     * @param messageBytes the message being streamed
     * @param headerBytes the header of the streamed message
     * @return true if no errors have occurred and the checksums are equal, false otherwise
     */
    @Override
    public Boolean isValid(byte[] checkSum, byte[] messageBytes, byte[] headerBytes) {
        CRC32 actualCRC = new CRC32();
        actualCRC.update(headerBytes);
        actualCRC.update(messageBytes);
        long expectedCRCValue = Integer.toUnsignedLong(ByteCheck.byteToIntConverter(checkSum, 0, 4));
        return expectedCRCValue == actualCRC.getValue();
    }
}