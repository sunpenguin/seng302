package seng302.team18.test_mock.connection;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.messageparsing.*;

import java.io.IOException;

/**
 * Created by jth102 on 4/07/17.
 */
public class PlayerControllerReader implements Runnable {

    private SocketMessageReceiver receiver;
    private MessageInterpreter interpreter;

    public PlayerControllerReader(SocketMessageReceiver receiver, MessageInterpreter interpreter) {
        this.receiver = receiver;
        this.interpreter = interpreter;
    }

    @Override
    public void run() {
        while (true) {
            try {
                MessageBody message = receiver.nextMessage();
                interpreter.interpret(message);
            } catch (IOException e) {
            }
        }
    }
}
