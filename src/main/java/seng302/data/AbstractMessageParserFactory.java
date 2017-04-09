package seng302.data;

/**
 * Created by dhl25 on 10/04/17.
 */
public abstract class AbstractMessageParserFactory {

    public abstract MessageHeadParser makeHeadParser();

    public abstract MessageBodyParser makeBodyParser(MessageType type);

    public abstract MessageErrorDetector makeDetector();
}
