package seng302.team18.send;

import com.google.common.base.Charsets;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import seng302.team18.message.ColourMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.message.NameMessage;
import seng302.team18.util.ByteCheck;

import java.io.IOException;

/**
 * Created by dhl25 on 16/08/17.
 */
public class ColourEncoder extends MessageEncoder {
    @Override
    protected byte[] generateBody(MessageBody message) throws IOException {
        if (message instanceof ColourMessage) {
            ColourMessage colourMessage = (ColourMessage) message;
            // convert string to byte[]
            ByteOutputStream messageBytes = new ByteOutputStream();
            byte[] sourceIDBytes = ByteCheck.intToByteArray(colourMessage.getSourceID());
            byte[] colourBytes = colourMessage.getColor();
            messageBytes.write(sourceIDBytes);
            messageBytes.write(colourBytes);

            return messageBytes.getBytes();
        }
        return null;
    }

    @Override
    protected short messageLength() {
        return 7;
    }
}
