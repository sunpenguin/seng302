package seng302.team18.send;

import java.io.ByteArrayOutputStream;

import javafx.scene.paint.Color;
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
            final int DECIMAL_TO_HEX = 255;
            ColourMessage colourMessage = (ColourMessage) message;
            // convert string to byte[]
            ByteArrayOutputStream messageBytes = new ByteArrayOutputStream();
            byte[] sourceIDBytes = ByteCheck.intToByteArray(colourMessage.getSourceID());
            Color colour = colourMessage.getColour();
            byte red = (byte) (int) (colour.getRed() * DECIMAL_TO_HEX);
            byte green = (byte) (int) (colour.getGreen() * DECIMAL_TO_HEX);
            byte blue = (byte) (int) (colour.getBlue() * DECIMAL_TO_HEX);
            byte[] colourBytes = { red, green, blue };
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
