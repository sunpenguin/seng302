package seng302.team18.visualiser.send;

import seng302.team18.message.MessageBody;
import seng302.team18.util.ByteCheck;
import seng302.team18.util.CRCGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Abstract class for encoding MessageBodies into byte arrays
 */
public abstract class MessageEncoder {

    /**
     * Encodes a MessageBody to a byte array.
     *
     * @param message to be encoded
     * @return the encoded message
     * @throws IOException
     */
    public byte[] encode(MessageBody message) throws IOException {
        byte[] head = generateHead(message);
        byte[] body = generateBody(message);
        byte[] checksum = generateChecksum(head, body);
        byte[] combined = new byte[head.length + body.length + checksum.length];

        System.arraycopy(head, 0, combined, 0, head.length);
        System.arraycopy(body, 0, combined, head.length, body.length);
        System.arraycopy(checksum, 0, combined, head.length + body.length, checksum.length);

        return combined;
    }


    /**
     * Creates to the header of the message as a byte array.
     *
     * @param message to create header from
     * @return message header.
     * @throws IOException
     */
    protected byte[] generateHead(MessageBody message) throws IOException {

        byte syncByte1 = 0x47;
        byte syncByte2 = (byte) 0x83;
        byte messageType = (byte) message.getType();
        byte[] timestampBytes = ByteCheck.getCurrentTime6Bytes();

        //TODO just like HeaderGenerate from test_mock, there needs to be a better way to generate source id
        byte[] sourceID = new byte[4];
        sourceID[0] = 11;
        sourceID[1] = 22;
        sourceID[2] = 33;
        sourceID[3] = 99;

        byte[] messageLen = ByteCheck.shortToByteArray(messageLength());

        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
        outputSteam.write(syncByte1);
        outputSteam.write(syncByte2);
        outputSteam.write(messageType);
        outputSteam.write(timestampBytes);
        outputSteam.write(sourceID);
        outputSteam.write(messageLen);

        return outputSteam.toByteArray();
    }


    /**
     * Creates to the body of the message as a byte array
     *
     * @param message to create the body of the message from.
     * @return the message as a byte array
     */
    protected abstract byte[] generateBody(MessageBody message);


    /**
     * Creates a checksum for the given message
     *
     * @param head of message to create checksum for
     * @param body of message to create checksum for
     * @return the checksum as a byte array
     */
    protected byte[] generateChecksum(byte[] head, byte[] body) throws IOException {
//        byte[] combined = new byte[head.length + body.length];
//
//        System.arraycopy(head, 0, combined, 0, head.length);
//        System.arraycopy(body, 0, combined, head.length, body.length);
//        return CRCGenerator.generateCRC(combined);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(head);
        outputStream.write(body);
        return CRCGenerator.generateCRC(outputStream.toByteArray());
    }


    /**
     * Returns the message length.
     *
     * @return length of the message
     */
    abstract protected short messageLength();
}
