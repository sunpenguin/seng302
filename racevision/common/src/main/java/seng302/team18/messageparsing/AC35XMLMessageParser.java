package seng302.team18.messageparsing;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dhl25 on 10/04/17.
 */
public class AC35XMLMessageParser implements MessageBodyParser {

    private MessageParserFactory parserFactory;

    public AC35XMLMessageParser(MessageParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }


    @Override
    public MessageBody parse(InputStream stream) { // wrapper
        try {
            return parse(ByteStreams.toByteArray(stream));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public MessageBody parse(byte[] bytes) {
        MessageHeadParser headParser = parserFactory.makeHeadParser();

        byte[] headerBytes = new byte[headParser.headerSize()];
        System.arraycopy(bytes, 0, headerBytes, 0, headerBytes.length);

        byte[] bodyBytes = new byte[bytes.length - headParser.headerSize()];
        System.arraycopy(bytes, headerBytes.length, bodyBytes, 0, bodyBytes.length);

        MessageHead head = headParser.parse(headerBytes);
        MessageBodyParser bodyParser = parserFactory.makeBodyParser(head.getType());

        return bodyParser == null ? null : bodyParser. parse(bodyBytes);
    }

}
