package seng302.team18.send;

import java.io.ByteArrayOutputStream;
import seng302.team18.message.ColourMessage;
import seng302.team18.message.MessageBody;
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
            ByteArrayOutputStream messageBytes = new ByteArrayOutputStream();
            byte[] sourceIDBytes = ByteCheck.intToByteArray(colourMessage.getSourceID());
            byte[] colourBytes = colourMessage.getColor();
            messageBytes.write(sourceIDBytes);
            messageBytes.write(colourBytes);

            return messageBytes.toByteArray();
        }
        return null;
    }

    @Override
    protected short messageLength() {
        return 7;
    }
}
