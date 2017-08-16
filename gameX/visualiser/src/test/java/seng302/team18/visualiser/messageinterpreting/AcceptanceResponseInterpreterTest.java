package seng302.team18.visualiser.messageinterpreting;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AcceptanceMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.message.RequestType;
import seng302.team18.send.Sender;

import javax.net.SocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collections;

/**
 * Created by dhl25 on 16/08/17.
 */
public class AcceptanceResponseInterpreterTest {


    private MessageInterpreter responseInterpreter;
    private boolean hasSent;


    @Before
    public void setUp() throws IOException {
        hasSent = false;
        Sender sender = new Sender(new ByteArrayOutputStream(), null) {
            @Override
            public void send(MessageBody message) {
                hasSent = true;
            }
        };

        responseInterpreter = new AcceptanceResponseInterpreter(Collections.singletonList(new RequestMessage(RequestType.RACING)), sender);
    }


    @Test
    public void interpretTest() {
        responseInterpreter.interpret(new AcceptanceMessage(1, RequestType.RACING));
        Assert.assertTrue(hasSent);
    }



}
