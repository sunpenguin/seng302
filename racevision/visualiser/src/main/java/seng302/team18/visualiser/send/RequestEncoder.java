package seng302.team18.visualiser.send;

import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.util.ByteCheck;
import seng302.team18.util.CRCGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by David-chan on 2/07/17.
 */
public class RequestEncoder extends MessageEncoder {


    @Override
    protected byte[] generateBody(MessageBody message) {
        if (message instanceof RequestMessage) {
            RequestMessage requestMessage = (RequestMessage) message;
            return ByteCheck.intToByteArray(requestMessage.getRequestType());
        }
        return null;
    }

    @Override
    protected byte[] generateChecksum(byte[] head, byte[] body) throws IOException{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        outStream.write(head);
        outStream.write(body);
        byte[] crc = CRCGenerator.generateCRC(outStream.toByteArray());
        return crc;
    }

    @Override
    protected short messageLength() {
        return 4;
    }

}
