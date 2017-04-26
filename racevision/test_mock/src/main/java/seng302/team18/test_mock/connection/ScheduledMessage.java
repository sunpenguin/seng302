package seng302.team18.test_mock.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Abstract base class for messages to be sent on a regular schedule.
 */
public abstract class ScheduledMessage {
    private long lastSent;
    private final int frequency;
    private int type;

    ScheduledMessage(int frequency , int type) {
        this.frequency = frequency;
        this.type = type;
    }

    /**
     * Checks if it is time to send the message, delegating this if it is
     *
     * @param currTime the current time
     */
    public boolean isTimeToSend(long currTime) {
        if ((currTime - lastSent) > (1000 / frequency)) {
            lastSent = currTime;
            return true;
        }
        return false;
    }

    /**
     * Gets the message of a ScheduledMessage with header payload and CRC.
     * @return byte[] of the message
     */
    public byte[] getMessage(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] payload;
        try {
            payload = getPayload();
            byte[] header = HeaderGenerator.generateHeader(type, (short)payload.length);
            //TODO get CRC
            outputStream.write(header);
            outputStream.write(payload);
            //outputStream.write(CRC);
            byte[] message = outputStream.toByteArray();
            return message;
        } catch (IOException e) {
            e.printStackTrace();
            byte[] error = {0};
            return error;
        }
    }

    /**
     * Overridden by base classes to define behaviour when it is time to send a message/s
     */
    public abstract byte[] getPayload() throws IOException;

}
