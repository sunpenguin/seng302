package seng302.team18.racemodel.generate;

import seng302.team18.util.CRCGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * An abstract base class for message generators
 */
public abstract class MessageGenerator {
    private int type;


    /**
     * Construct a MessageGenerator with a type indicating what type of message is required.
     *
     * @param type Integer representing the message type
     */
    public MessageGenerator(int type) {
        this.type = type;
    }

    /**
     * Gets the message of a ScheduledMessageGenerator with header, payload and CRC.
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
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            byte[] error = {0};
            return error; //return failed message
        }
    }

    /**
     * Overridden by base classes to define behaviour when it is time to encode a message/s
     *
     * @return A byte array of the payload of the message
     *
     * @throws IOException Thrown if error occurs when writing to a ByteArrayOutputStream
     */
    protected abstract byte[] getPayload() throws IOException;
}
