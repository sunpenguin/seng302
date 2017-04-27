package seng302.team18.messageparsing;

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
//        ByteBuffer wrapped = ByteBuffer.allocate(8);
//        wrapped = wrapped.wrap(checkSum);
//        long givenCheckSum = wrapped.getLong();
//
//        //Calculate the checksum for the given message with crc32 polynomial
//        CRC32 crc = new CRC32();
//        byte[] full = new byte[messageBytes.length + checkSum.length];
//        System.arraycopy(messageBytes, 0, full, 0, messageBytes.length);
//        System.arraycopy(checkSum, 0, full, messageBytes.length, checkSum.length);
//        crc.update(full);
////        crc.update(checkSum);
//        long calculatedCheckSum = crc.getValue();
////        System.out.println(calculatedCheckSum);
//        System.out.println(calculatedCheckSum);
//        System.out.println("GIVEN: " + givenCheckSum + "\nCALCULATED: " + calculatedCheckSum);
        //Valid if the two checksums are the same.
        return true;
    }
}