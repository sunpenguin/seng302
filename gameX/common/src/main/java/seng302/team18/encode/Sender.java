package seng302.team18.encode;


import seng302.team18.message.MessageBody;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Sends MessageBodies over a tcp connection after encoding them
 */
public class Sender {

    private MessageEncoderFactory factory;
    private OutputStream outStream;
    private Socket socket;

    /**
     * Constructor for the Sender. Requires a socket and MessageEncoderFactory.
     *
     * @param socket socket to encode messages over
     * @param factory to convert messages to byte arrays.
     *
     * @throws IOException Thrown if socket::getOutputStream throws an IOException
     */
    public Sender(Socket socket, MessageEncoderFactory factory) throws IOException {
        outStream = socket.getOutputStream();
        this.factory = factory;
        this.socket = socket;
    }


    /**
     * Constructor for the Sender. Requires OutputStream and MessageEncoderFactory.
     *
     * @param outputStream to write data to.
     * @param factory to convert messages to byte arrays.
     */
    public Sender(OutputStream outputStream, MessageEncoderFactory factory) {
        outStream = outputStream;
        this.factory = factory;
    }

    /**
     * Sends a message after encoding it with the provided encoder.
     *
     * @param body of the message to be sent
     */
    public void send(MessageBody body) throws IOException {
        MessageEncoder composer = factory.getEncoder(body.getType());
        outStream.write(composer.encode(body));
        outStream.flush();
    }


    /**
     * Closes OutputStream used to encode data. Blocks until it closes.
     */
    public void close() {
        boolean isOpen = true;
        while (isOpen) {
            try {
                outStream.close();
                socket.close();
                isOpen = false;
            } catch (IOException e) {}
        }
    }



//    /**
//     * Closes the socket data is coming from.
//     *
//     * @return if the socket successfully closed.
//     */
//    public boolean close() {
//        try {
//            outStream.close();
//            socket.close();
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//    }
}
