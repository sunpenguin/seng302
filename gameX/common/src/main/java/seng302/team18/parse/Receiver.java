package seng302.team18.parse;

import seng302.team18.message.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Sets up a client socket to read from the AC35 parse stream.
 */

public class Receiver {
    private Socket socket;
    private InputStream inStream;
    private MessageParserFactory parserFactory;


    public Receiver(Socket socket, MessageParserFactory parserFactory) throws IOException {
        this.parserFactory = parserFactory;
        this.socket = socket;
        inStream = socket.getInputStream();
        if (!inStream.markSupported()) {
            inStream = new BufferedInputStream(inStream);
        }
    }


    /**
     * Reads the next message from the input stream. If it is of a type which the program can read then a parser and
     * message are created. Otherwise the message is ignored and null is returned.
     *
     * @return A message body whose type matches the type required by the stream.
     * @throws IOException if an error occurs with the input stream.
     */
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
        if (inStream.available() < head.bodySize() + detector.errorCheckSize()) {
            inStream.reset();
            return null;
        }
        MessageBodyParser bodyParser = parserFactory.makeBodyParser(head.getType());
        byte[] bodyBytes = new byte[head.bodySize()];
        byte[] checkBytes = new byte[detector.errorCheckSize()];

        inStream.read(bodyBytes);
        inStream.read(checkBytes);
        if (detector.isValid(checkBytes, bodyBytes, headerBytes) && bodyParser != null) {
//            System.out.println("Receiver::nextMessage");
//            System.out.println(AC35MessageType.from(head.getType()));
            return bodyParser.parse(bodyBytes);
        }
        return null;
    }


    /**
     * Closes the socket data is coming from.
     *
     * @return if the socket successfully closed.
     */
    public boolean close() {
        try {
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public Socket getSocket() {
        return socket;
    }
}
