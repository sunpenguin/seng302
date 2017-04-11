package seng302.team18.data;

/**
 * Created by david on 4/10/17.
 */
public class AC35XMLParserFactory implements MessageParserFactory {

    @Override
    public MessageHeadParser makeHeadParser() {
        return new AC35XMLHeadParser();
    }

    @Override
    public MessageBodyParser makeBodyParser(int type) {
        return null;
    }

    @Override
    public MessageErrorDetector makeDetector() {
        return null;
    }
}
