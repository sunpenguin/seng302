package seng302.team18.test_mock.connection;

import org.junit.Test;
import seng302.team18.util.ByteCheck;
import seng302.team18.util.CRCGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static org.junit.Assert.assertEquals;

public class CRCGeneratorTest {

    @Test
    public void generateCRCTest() throws IOException {
        // generate a message to be calculated
        byte[] message = new byte[4];
        message[0] = 0;
        message[1] = 1;
        message[2] = 2;
        message[3] = 3;

        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();

        try {
            outputSteam.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] check = outputSteam.toByteArray();

        // get the CRC value
        CRCGenerator crcGenerator = new CRCGenerator();
        byte[] calculatedCRC = crcGenerator.generateCRC(check);
        int crc = ByteCheck.byteToInt(calculatedCRC, 0, 4);
        String actualCRC = Integer.toHexString(crc);

        // generate the expected CRC value
        Checksum checksum = new CRC32();
        checksum.update(message, 0, message.length);
        long checksumValue = checksum.getValue();
        String expectedCRC = Long.toHexString(checksumValue);

        assertEquals(expectedCRC, actualCRC);
    }
}
