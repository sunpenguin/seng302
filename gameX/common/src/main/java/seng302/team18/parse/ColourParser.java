package seng302.team18.parse;

import com.google.common.io.ByteStreams;
import javafx.scene.paint.Color;
import seng302.team18.message.ColourMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.io.InputStream;

public class ColourParser implements MessageBodyParser {


    @Override
    public MessageBody parse(InputStream stream) {
        try {
            return parse(ByteStreams.toByteArray(stream));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public MessageBody parse(byte[] bytes) {
        final int SOURCE_ID_INDEX = 0;
        final int SOURCE_ID_LENGTH = 4;
        final int RED_INDEX = 4;
        final int GREEN_INDEX = 5;
        final int BLUE_INDEX = 6;

        int sourceID = ByteCheck.byteToInt(bytes, SOURCE_ID_INDEX, SOURCE_ID_LENGTH);
        int red = bytes[RED_INDEX] & 0xFF;
        int green = bytes[GREEN_INDEX] & 0xFF;
        int blue = bytes[BLUE_INDEX] & 0xFF;
        Color colour = Color.rgb(red, green, blue);

        return new ColourMessage(colour, sourceID);
    }


}
