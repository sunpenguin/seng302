package seng302.team18.send;

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
     * Constructor for the Sender. Requires ip, port, and MessageEncoderFactory.
     *
     * @param socket socket to send messages over
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
     * Constructor for the Sender. Requires ip, port, and MessageEncoderFactory.
     *
     * @param host string representing the ip we want to send data to
     * @param portNumber port number of the application we want to send to
     * @param factory to convert messages to byte arrays.
     *
     * @throws IOException Thrown if socket::getOutputStream throws an IOException
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
     */
    public void send(MessageBody body) {
        try {
            MessageEncoder composer = factory.getEncoder(body.getType());
            outStream.write(composer.encode(body));
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Closes the socket data is coming from.
     *
     * @return if the socket successfully closed.
     */
    public boolean close() {
        try {
            outStream.close();
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
