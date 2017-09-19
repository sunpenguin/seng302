package seng302.team18.parse;

/**
 * Interface for MessageParserFactory.
 */
public interface MessageParserFactory {

    MessageHeadParser makeHeadParser();

    MessageBodyParser makeBodyParser(int type);

    MessageErrorDetector makeDetector();
}
