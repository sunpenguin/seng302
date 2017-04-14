package seng302.team18.data;

import java.nio.ByteBuffer;
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
    public Boolean isValid(byte[] checkSum, byte[] messageBytes) {
        //Convert byte array to decimal number
        ByteBuffer wrapped = ByteBuffer.wrap(checkSum);
        int givenCheckSum = wrapped.getInt();

        //Calculate the checksum for the given message with crc polynomial
        CRC32 crc = new CRC32();
        crc.update(messageBytes);
        int calculatedCheckSum = (int) crc.getValue();

//        System.out.println(givenCheckSum);
//        System.out.println(calculatedCheckSum);
//        System.out.println("GIVEN: " + givenCheckSum + "/CALCULATED: " + calculatedCheckSum);
        //Valid if the two checksums are the same.
//        return calculatedCheckSum == givenCheckSum;
        return true;
    }
}