package seng302.team18.test_mock.connection;

import seng302.team18.util.ByteCheck;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A class that generates the heartbeat for messages.
 */
public class HeartBeatMessageGenerator{

    private static int seqNo = 0;

    /*
    Generate a heartbeat message which will contain a sequence number.
    The sequence number will increment by one every time it is called.
     */
    public static byte[] generateHeartBeat() throws IOException {

        byte[] seqNum = ByteCheck.intToByteArray(seqNo);

        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();

        outputSteam.write(seqNum);

        byte[] heartBeat = outputSteam.toByteArray();

        seqNo += 1;
        return ByteCheck.convertToLittleEndian(heartBeat, 4);
    }
}