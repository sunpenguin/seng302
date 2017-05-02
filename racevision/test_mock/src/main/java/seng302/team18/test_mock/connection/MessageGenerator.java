package seng302.team18.test_mock.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by afj19 on 28/04/17.
 */
public abstract class MessageGenerator {
    private int type;

    public MessageGenerator(int type) {
        this.type = type;
    }

    /**
     * Gets the message of a ScheduledMessageGenerator with header payload and CRC.
     *
     * @return byte[] of the message
     */
    public byte[] getMessage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] payload;
        try {
            payload = getPayload();
            byte[] header = HeaderGenerator.generateHeader(type, (short) payload.length);
            outputStream.write(header);
            outputStream.write(payload);
            byte[] crc = CRCGenerator.generateCRC(outputStream.toByteArray());
            outputStream.write(crc);
            byte[] message = outputStream.toByteArray();
            return message;
        } catch (IOException e) {
            e.printStackTrace();
            byte[] error = {0};
            return error; //return failed message
        }
    }

    /**
     * Overridden by base classes to define behaviour when it is time to send a message/s
     */
    protected abstract byte[] getPayload() throws IOException;
}
