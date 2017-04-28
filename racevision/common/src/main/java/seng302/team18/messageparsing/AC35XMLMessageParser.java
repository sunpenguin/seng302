package seng302.team18.messageparsing;

/**
 * Created by dhl25 on 10/04/17.
 */
public class AC35XMLMessageParser implements MessageBodyParser {

    private MessageParserFactory parserFactory;
    private Boolean firstBoat = true;

    public AC35XMLMessageParser(MessageParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

    @Override
    public MessageBody parse(byte[] bytes) {
        MessageHeadParser headParser = parserFactory.makeHeadParser();

        byte[] headerBytes = new byte[headParser.headerSize()];
        System.arraycopy(bytes, 0, headerBytes, 0, headerBytes.length);

        byte[] bodyBytes = new byte[bytes.length - headParser.headerSize()];
        System.arraycopy(bytes, headerBytes.length, bodyBytes, 0, bodyBytes.length);

        MessageHead head = headParser.parse(headerBytes);
        if (AC35MessageType.from(head.getType()).equals(AC35MessageType.XML_BOATS) && firstBoat.equals(true)) {
            bodyBytes = new byte[bytes.length - headParser.headerSize() - 2];
            System.arraycopy(bytes, headerBytes.length, bodyBytes, 0, bodyBytes.length);
            firstBoat = false;
        }

        MessageBodyParser bodyParser = parserFactory.makeBodyParser(head.getType());
        return bodyParser == null ? null : bodyParser. parse(bodyBytes);
    }

}
