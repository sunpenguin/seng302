package seng302.team18.test_mock.connection;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.messageparsing.*;

import java.io.IOException;

/**
 * Class for reading and interpreting messages sent by Human controlled players
 */
public class PlayerControllerReader implements Runnable {

    private SocketMessageReceiver receiver;
    private MessageInterpreter interpreter;

    public PlayerControllerReader(SocketMessageReceiver receiver, MessageInterpreter interpreter) {
        this.receiver = receiver;
        this.interpreter = interpreter;
    }


    /**
     * Listen for and interpret incoming packets from human controlled players.
     */
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
