package seng302.team18.messageparsing;

import java.io.InputStream;

/**
 * Created by dhl25 on 10/04/17.
 */
public interface MessageBodyParser {

    public MessageBody parse(InputStream stream);

    public MessageBody parse(byte[] bytes);
}
