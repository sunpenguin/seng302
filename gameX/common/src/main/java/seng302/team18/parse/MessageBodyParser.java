package seng302.team18.parse;

import seng302.team18.message.MessageBody;

import java.io.InputStream;

/**
 * Interface for MessageBodyParser.
 */
public interface MessageBodyParser {

    MessageBody parse(InputStream stream);

    MessageBody parse(byte[] bytes);
}
