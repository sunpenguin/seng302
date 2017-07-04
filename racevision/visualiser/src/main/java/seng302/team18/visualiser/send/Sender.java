package seng302.team18.visualiser.send;

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


    /**
     * Constructor for the Sender. Requires ip, port, and MessageEncoderFactory.
     *
     * @param host string representing the ip we want to send data to
     * @param portNumber port number of the application we want to send to
     * @param factory to convert messages to byte arrays.
     * @throws IOException
     */
    public Sender(String host, int portNumber, MessageEncoderFactory factory) throws IOException {
        Socket socket = new Socket(host, portNumber);
        outStream = socket.getOutputStream();
        this.factory = factory;
    }


    /**
     * Sends a message after encoding it with the provided encoder.
     *
     * @param body of the message to be sent
     * @throws IOException
     */
    public void send(MessageBody body) throws IOException {
        MessageEncoder composer = factory.getComposer(body.getType());
        outStream.write(composer.compose(body));
    }


}
