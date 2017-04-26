package seng302.team18.test_mock.connection;

import seng302.team18.util.ByteCheck;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A class that generates the heartbeat for messages.
 */
public class HeartBeatMessageGenerator extends ScheduledMessage{

    private static int seqNo = 0;

    public HeartBeatMessageGenerator() {
        super(2);
    }

    /**
     * Returns the heartbeat message with the header, payload and CRC.
     * @return byte[] of heartbeat message
     */
    @Override
    public byte[] getMessage(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] payload;
        try {
            payload = getPayload();
            byte[] header = HeaderGenerator.generateHeader(1, (short)payload.length);
            //TODO get CRC
            outputStream.write(header);
            outputStream.write(payload);
            //outputStream.write(CRC)
            byte[] message = outputStream.toByteArray();
            return message;
        } catch (IOException e) {
            e.printStackTrace();
            byte[] error = {0};
            return error;
        }
    }

    /*
    Generate a heartbeat message which will contain a sequence number.
    The sequence number will increment by one every time it is called.
     */
    private byte[] getPayload()  throws IOException {
        byte[] seqNum = ByteCheck.intToByteArray(seqNo);

        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();

        outputSteam.write(seqNum);

        byte[] heartBeat = outputSteam.toByteArray();

        seqNo += 1;
        return ByteCheck.convertToLittleEndian(heartBeat, 4);
    }
}