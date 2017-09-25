package seng302.team18.parse;

import com.google.common.io.ByteStreams;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.util.ByteCheck;

import java.io.IOException;
import java.io.InputStream;

/**
 * Parser for BoatActionMessages.
 */
public class BoatActionParser implements MessageBodyParser {


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

        final int ID_INDEX = 0;
        final int ID_LENGTH = 4;

        int id = ByteCheck.byteToInt(bytes, ID_INDEX, ID_LENGTH);
        BoatActionMessage message = new BoatActionMessage(id);

        switch (bytes[4]) {
            case 1:
                message.setAutoPilot();
                break;
            case 2:
                message.setSailIn();
                break;
            case 3:
                message.setSailOut();
                break;
            case 4:
                message.setTackGybe();
                break;
            case 5:
                message.setUpwind();
                break;
            case 6:
                message.setDownwind();
                break;
            case 7:
                message.setConsume();
                break;
        }

        return message;
    }

}
