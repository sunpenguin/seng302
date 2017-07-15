package seng302.team18.messageparsing;

import com.google.common.io.ByteStreams;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dhl25 on 15/07/17.
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
        BoatActionMessage message = new BoatActionMessage();
        switch (bytes[0]) {
            case 1:
                message.setAutopilot(true);
                break;
            case 2:
                message.setSailsIn(true);
                break;
            case 3:
                message.setSailsIn(false);
                break;
            case 4:
                message.setTackGybe(true);
                break;
            case 5:
                message.setUpwind(true);
                break;
            case 6:
                message.setDownwind(true);
                break;
        }

        return message;
    }

}
