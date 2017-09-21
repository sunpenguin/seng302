package seng302.team18.visualiser.interpret;

import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AcceptanceMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestType;
import seng302.team18.encode.Sender;
import seng302.team18.visualiser.interpret.unique.ColourResponder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dhl25 on 16/08/17.
 */
public class ColourResponderTest {


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

        responseInterpreter = new ColourResponder(Color.AZURE, sender);
    }


    @Test
    public void interpretTest() {
        responseInterpreter.interpret(new AcceptanceMessage(1, RequestType.RACING));
        Assert.assertTrue(hasSent);
    }



}
