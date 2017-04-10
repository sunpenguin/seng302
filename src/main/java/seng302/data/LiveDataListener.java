package seng302.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Sets up a client socket to read from the AC35 data stream.
 *
 */

public class LiveDataListener {
    private Socket socket;
    private InputStream inStream;
    private MessageParserFactory parserFactory;

    public LiveDataListener(int portNumber, MessageParserFactory parserFactory) throws IOException {
        this.parserFactory = parserFactory;
        // Create input and output streams for reading in data
        socket = new Socket("livedata.americascup.com", portNumber);
        inStream = socket.getInputStream();
    }



    public MessageBody nextMessage() throws IOException {
        MessageHeadParser headParser = parserFactory.makeHeadParser();
        if (inStream.available() <= headParser.headerSize()) {
            return null;
        }
        inStream.mark(headParser.headerSize() + 1);
        byte[] headerBytes = new byte[headParser.headerSize()];
        inStream.read(headerBytes);
        MessageHead head = headParser.parse(headerBytes);
        MessageErrorDetector detector = parserFactory.makeDetector();
        if (inStream.available() <= head.bodySize() + detector.errorCheckSize()) {
            inStream.reset();
            return null;
        }
        MessageBodyParser bodyParser = parserFactory.makeBodyParser(head.getType());
        byte[] bodyBytes = new byte[head.bodySize()];
        byte[] checkBytes = new byte[detector.errorCheckSize()];
        inStream.read(bodyBytes);
        inStream.read(checkBytes);
        if (detector.isValid(checkBytes)) {
            return bodyParser.parse(bodyBytes);
        }
        return null;
    }


}
