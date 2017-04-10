package seng302.data;

/**
 * Created by dhl25 on 10/04/17.
 */
public interface MessageParserFactory {

//    public MessageHeadParser makeDefaultHeadParser();
//
//    public MessageHeadParser makeHeadParser(int type);

    public MessageHeadParser makeHeadParser();

    public MessageBodyParser makeBodyParser(int type);

    public MessageErrorDetector makeDetector();
}
