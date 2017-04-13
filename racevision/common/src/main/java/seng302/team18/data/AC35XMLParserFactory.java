package seng302.team18.data;

import java.util.HashMap;
import java.util.Map;

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
        AC35MessageType xmlType = AC35MessageType.from(type);
        switch (xmlType) {
            case XML_REGATTA:
                return null;
            case XML_RACE:
                return null;
            case XML_BOATS:
                return null;
            default:
                return null;
        }
    }


    @Override
    public MessageErrorDetector makeDetector() {
        return new MessageErrorDetector() {
            @Override
            public int errorCheckSize() {
                return 0;
            }

            @Override
            public Boolean isValid(byte[] checkSum, byte[] messageBytes) {
                return true;
            }
        };
    }


}
