package seng302.team18.racemodel.generate;

import seng302.team18.message.AC35MessageType;
import seng302.team18.util.ByteCheck;

import java.io.IOException;

/**
 * A class that generates the heartbeat for messages.
 */
public class HeartBeatMessageGenerator extends ScheduledMessageGenerator {

    private int seqNo = 0;

    /**
     * Constructs a new instance of HeartBeatMessageGenerator.
     */
    public HeartBeatMessageGenerator() {
        super(2, AC35MessageType.HEARTBEAT.getCode());
    }

    /**
     * Generate a heartbeat message which will contain a sequence number.
     * The sequence number will increment by one every time it is called.
     */
    @Override
    public byte[] getPayload() throws IOException {
        byte[] seqNum = ByteCheck.intToByteArray(seqNo);
        seqNo += 1;
        return seqNum;
    }
}