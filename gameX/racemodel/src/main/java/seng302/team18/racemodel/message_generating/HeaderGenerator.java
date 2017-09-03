package seng302.team18.racemodel.message_generating;

import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class to generate headers for messages to be sent via the server.
 */
public class HeaderGenerator {

    /**
     * A method used to generate the header for a message of a certain type.
     *
     * @param type            The type of the message the header is generated for.
     * @param lengthOfMessage length of the message the header in generated for
     * @return header as a byte array.
     * @throws IOException if an I/O exception occurs is thrown by {@link java.io.OutputStream#write(byte[]) java.io.OutputStream#write(byte[])}
     */
    public static byte[] generateHeader(int type, short lengthOfMessage) throws IOException {

        byte syncByte1 = 0x47;

        byte syncByte2 = (byte) 0x83;//TODO make sure -125 is okay, might need to be 131

        byte messageType = (byte) type;

        byte[] timestampBytes = ByteCheck.getCurrentTime6Bytes();

        // TODO: How to make reasonable sourceID?
        byte[] sourceID = new byte[4];
        sourceID[0] = 58;
        sourceID[1] = 94;
        sourceID[2] = 123;
        sourceID[3] = 93;

        byte[] messageLen = ByteCheck.shortToByteArray(lengthOfMessage);

        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
        outputSteam.write(syncByte1);
        outputSteam.write(syncByte2);
        outputSteam.write(messageType);
        outputSteam.write(timestampBytes);
        outputSteam.write(sourceID);
        outputSteam.write(messageLen);

        return outputSteam.toByteArray();
    }
}
