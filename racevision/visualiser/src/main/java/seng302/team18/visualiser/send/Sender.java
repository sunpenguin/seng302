package seng302.team18.visualiser.send;

import seng302.team18.message.MessageBody;
import seng302.team18.message.MessageHead;
import seng302.team18.messageparsing.MessageParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by David-chan on 2/07/17.
 */
public class Sender {

    private MessageComposerFactory factory;
    private OutputStream outStream;


    public Sender(String host, int portNumber, MessageComposerFactory factory) throws IOException {
        Socket socket = new Socket(host, portNumber);
        outStream = socket.getOutputStream();
        this.factory = factory;
    }


    public void send(MessageBody body) throws IOException {
        MessageComposer composer = factory.getComposer(body.getType());
        outStream.write(composer.compose(body));
    }
}
