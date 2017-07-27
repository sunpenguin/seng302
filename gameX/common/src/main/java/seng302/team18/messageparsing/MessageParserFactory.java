package seng302.team18.messageparsing;

/**
 * Interface for MessageParserFactory.
 */
public interface MessageParserFactory {

    MessageHeadParser makeHeadParser();

    MessageBodyParser makeBodyParser(int type);

    MessageErrorDetector makeDetector();
}
