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
    private String currentLine;
    private AbstractMessageParserFactory parserFactory;

    public LiveDataListener(int portNumber, AbstractMessageParserFactory parserFactory) throws IOException {
        this.parserFactory = parserFactory;
        // Create input and output streams for reading in data
        socket = new Socket("livedata.americascup.com", portNumber);
        inStream = socket.getInputStream();
    }



    public MessageBody nextMessage() throws IOException {
        MessageHead head = nextHead();
        MessageBodyParser bodyParser = parserFactory.makeBodyParser(head.type());
    }

    private MessageHead nextHead() throws IOException {
        MessageHeadParser headParser = parserFactory.makeHeadParser();
        if (inStream.available() <= headParser.headerSize()) {
            return null;
        }
        inStream.mark(headParser.headerSize() + 1);
        byte[] headerBytes = new byte[headParser.headerSize()];
        inStream.read(headerBytes);
        MessageHead head = headParser.parse(headerBytes);
        return head;
    }

}
