package seng302.data;

/**
 * Created by dhl25 on 10/04/17.
 */
public interface MessageParserFactory {

    public MessageHeadParser makeHeadParser();

    public MessageBodyParser makeBodyParser(MessageType type);

    public MessageErrorDetector makeDetector();
}
