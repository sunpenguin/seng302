package seng302.team18.send;

import com.google.common.base.Charsets;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import seng302.team18.message.MessageBody;
import seng302.team18.message.NameMessage;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Class to encode name request messages
 */
public class NameEncoder extends MessageEncoder {

    @Override
    protected byte[] generateBody(MessageBody message) throws IOException {
        if (message instanceof NameMessage) {
            NameMessage nameMessage = (NameMessage) message;
            // convert string to byte[]
            ByteOutputStream messageBytes = new ByteOutputStream();
            byte[] sourceIDBytes = ByteCheck.intToByteArray(nameMessage.getSourceID());
            byte[] miniNameBytes = nameMessage.getMiniName().getBytes(Charsets.UTF_8);
            System.out.println("miniNameBytes length: " + miniNameBytes.length);
            byte[] nameBytes = nameMessage.getMiniName().getBytes(Charsets.UTF_8);
            messageBytes.write(sourceIDBytes);
            messageBytes.write(miniNameBytes);
            messageBytes.write(nameBytes);
            messageBytes.write(new byte[30 - nameBytes.length]);


            return messageBytes.getBytes();
        }
        return null;
    }

    @Override
    protected short messageLength() {
        return 37;
    }
}
