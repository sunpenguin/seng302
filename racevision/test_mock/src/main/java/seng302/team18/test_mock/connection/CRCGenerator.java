package seng302.team18.test_mock.connection;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Created by lenovo on 2017/4/26.
 */
public class CRCGenerator {

    /*
    Calculate CRC32 checksum for header and message body.
     */
    public long generateCRC() {

        String input = "Java Code Geeks - Java Examples";
        byte bytes[] = input.getBytes();
        Checksum checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);
        long checksumValue = checksum.getValue();
        System.out.println("CRC32 checksum for input string is: " + checksumValue);


//        Checksum checksum = new CRC32();
//        checksum.update(message, 0, 4);
//        long checksumValue = checksum.getValue();
//        System.out.println(checksum);
//        System.out.println(checksumValue);
        return checksumValue;
    }
}
