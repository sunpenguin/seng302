package seng302.team18.test_mock.connection;

import seng302.team18.util.ByteCheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A class that generates the heartbeat for messages.
 */
public class HeartBeatMessageGenerator extends ScheduledMessageGenerator {

    private int seqNo = 0;

    public HeartBeatMessageGenerator() {
        super(2, 1);
    }

    /*
    Generate a heartbeat message which will contain a sequence number.
    The sequence number will increment by one every time it is called.
     */
    @Override
    public byte[] getPayload()  throws IOException {
        byte[] seqNum = ByteCheck.intToByteArray(seqNo);
        seqNo += 1;
        return seqNum;
    }
}