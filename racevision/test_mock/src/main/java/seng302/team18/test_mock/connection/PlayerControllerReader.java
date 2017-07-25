package seng302.team18.test_mock.connection;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.messageparsing.*;

/**
 * Class for reading and interpreting messages sent by Human controlled players
 */
public class PlayerControllerReader implements Runnable {

    private Receiver receiver;
    private MessageInterpreter interpreter;
    boolean open = true;

    public PlayerControllerReader(Receiver receiver, MessageInterpreter interpreter) {
        this.receiver = receiver;
        this.interpreter = interpreter;
    }


    /**
     * Listen for and interpret incoming packets from human controlled players.
     */
    @Override
    public void run() {
        while (open) {
            try {
                MessageBody message = receiver.nextMessage();
                interpreter.interpret(message);
            } catch (Exception e) {}
        }
    }


    public void close() {
        open = false;
        receiver.close();
    }
}
