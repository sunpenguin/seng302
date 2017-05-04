package seng302.team18.messageparsing;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;

/**
 * A parser which reads information from an XML stream and creates parsers of the type corresponding to the stream
 * information.
 *
 * Created by dhl25 on 10/04/17.
 */
public class AC35XMLMessageParser implements MessageBodyParser {

    private MessageParserFactory parserFactory;

    public AC35XMLMessageParser(MessageParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }


    /**
     * Converts the input stream to a byte array which is then passed to a parse method to read in which type of
     * message body needs to be created.
     * @param stream A data stream
     * @return A message body created by the other parse method.
     */
    @Override
    public MessageBody parse(InputStream stream) { // wrapper
        try {
            return parse(ByteStreams.toByteArray(stream));
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * Takes a byte array and creates and returns a message body of the type indicated by the body bytes.
     * Uses the body parser to parse all of the body bytes.
     * @param bytes A list of bytes
     * @return A message body with the body bytes parsed into it
     */
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
