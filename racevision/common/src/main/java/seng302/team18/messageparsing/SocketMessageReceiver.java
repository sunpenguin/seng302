package seng302.team18.messageparsing;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Sets up a client socket to read from the AC35 messageparsing stream.
 *
 */

public class SocketMessageReceiver {
    private Socket socket;
    private InputStream inStream;
    private MessageParserFactory parserFactory;


    public SocketMessageReceiver(int portNumber, MessageParserFactory parserFactory) throws IOException {
        this.parserFactory = parserFactory;
        // Create input and output streams for reading in messageparsing
//        socket = new Socket("livedata.americascup.com", portNumber);
        socket = new Socket("132.181.12.11", 5005);
        inStream = socket.getInputStream();
        if (!inStream.markSupported()) {
            inStream = new BufferedInputStream(inStream);
        }
    }



    public MessageBody nextMessage() throws IOException {
        System.out.println("Woooo");
        MessageHeadParser headParser = parserFactory.makeHeadParser();
        if (inStream.available() <= headParser.headerSize()) {
            return null;
        }
        inStream.mark(headParser.headerSize() + 1);
        byte[] headerBytes = new byte[headParser.headerSize()];
        inStream.read(headerBytes);
        MessageHead head = headParser.parse(headerBytes);
        MessageErrorDetector detector = parserFactory.makeDetector();
        if (inStream.available() < head.bodySize() + detector.errorCheckSize()) {
            inStream.reset();
            return null;
        }
        MessageBodyParser bodyParser = parserFactory.makeBodyParser(head.getType());
        byte[] bodyBytes = new byte[head.bodySize()];
        byte[] checkBytes = new byte[detector.errorCheckSize()];

        inStream.read(bodyBytes);
        inStream.read(checkBytes);
//        if (detector.isValid(checkBytes, bodyBytes, headerBytes) && bodyParser != null) {
//            return bodyParser.parse(bodyBytes);
//        }
//
//        return null;
        return bodyParser == null ? null : bodyParser.parse(bodyBytes);
    }


}
