package seng302.team18.data;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
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

    InputStream bis;

    public LiveDataListener(int portNumber, MessageParserFactory parserFactory) throws IOException {
        this.parserFactory = parserFactory;
        // Create input and output streams for reading in data
        socket = new Socket("livedata.americascup.com", portNumber);
        inStream = socket.getInputStream();
        bis = new BufferedInputStream(inStream);
    }



    public MessageBody nextMessage() throws IOException {
//        System.out.println("nextMessage called");
        MessageHeadParser headParser = parserFactory.makeHeadParser();
        if (bis.available() <= headParser.headerSize()) {
//            System.out.println(inStream.available());
//            System.out.println("not enough for head\n");
            return null;
        }
        bis.mark(headParser.headerSize() + 1);
        byte[] headerBytes = new byte[headParser.headerSize()];
        bis.read(headerBytes);
        MessageHead head = headParser.parse(headerBytes);
        MessageErrorDetector detector = parserFactory.makeDetector();
        if (bis.available() < head.bodySize() + detector.errorCheckSize()) {
//            System.out.println("not enough");
//            System.out.println("bis.available: " + bis.available() + "/inStream.available: " + inStream.available());
//            int total = head.bodySize() + detector.errorCheckSize();
//            System.out.println("head.bodySize + detector.errorCheckSize: " + total);
//            System.out.println(" ");
            bis.reset();
            return null;
        }
//        System.out.println("We get a message now.");
//        System.out.println(head == null);
//        System.out.println(AC35MessageType.from(head.getType()));
//        System.out.println();
        MessageBodyParser bodyParser = parserFactory.makeBodyParser(head.getType());
        byte[] bodyBytes = new byte[head.bodySize()];
        byte[] checkBytes = new byte[detector.errorCheckSize()];
        byte[] headAndBody = new byte[headerBytes.length + bodyBytes.length];
        System.arraycopy(headerBytes, 0, headAndBody, 0, headerBytes.length);
        System.arraycopy(bodyBytes, 0, headAndBody, headerBytes.length, bodyBytes.length);

        bis.read(bodyBytes);
        bis.read(checkBytes);
//        System.out.println(AC35MessageType.from(head.getType()));
//        System.out.println();
        if (detector.isValid(checkBytes, headAndBody) && bodyParser != null) {
//            System.out.println("Pass");
//            System.out.println(AC35MessageType.from(head.getType()));
            return bodyParser.parse(bodyBytes);
        }
//        System.out.println("Fail");
//        System.out.println(AC35MessageType.from(head.getType()));
        return null;
//        return bodyParser.parse(bodyBytes);
    }


}
