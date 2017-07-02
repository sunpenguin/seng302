package seng302.team18.visualiser.send;

import seng302.team18.message.MessageBody;

/**
 * Created by dhl25 on 2/07/17.
 */
public abstract class MessageComposer {


    // TODO write javadoc
    public byte[] compose(MessageBody message) {
        byte[] head = generateHead(message);
        byte[] body = generateBody(message);
        byte[] checksum = generateChecksum(message);

        byte[] combined = new byte[head.length + body.length + checksum.length];
        System.arraycopy(head, 0, combined, 0, head.length);
        System.arraycopy(body, 0, combined, head.length, body.length);
        System.arraycopy(checksum, 0, combined, head.length + body.length, checksum.length);

        return combined;
    }


    abstract protected byte[] generateHead(MessageBody message);

    protected abstract byte[] generateBody(MessageBody message);

    protected abstract byte[] generateChecksum(MessageBody message);
}
