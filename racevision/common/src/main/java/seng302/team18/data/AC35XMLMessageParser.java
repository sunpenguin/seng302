package seng302.team18.data;

import java.util.Arrays;

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

//        bytes[] bod
//        System.out.println(new String(Arrays.copyOfRange(bytes, 14, bytes.length), "UTF-8"));

//        System.out.println("AC35XMLMessageParser");
//        System.out.println("Total size = " + bytes.length);
//        System.out.println("head size = " + headerBytes.length);
//        System.out.println("body size = " + bodyBytes.length);
//        System.out.println();

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
