package seng302.team18.racemodel.connection;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.messageparsing.*;

import java.io.IOException;

/**
 * Class for reading and interpret messages sent by Human controlled players
 */
public class PlayerControllerReader implements Runnable {

    private int id;
    private Receiver receiver;
    private MessageInterpreter interpreter;
    private boolean open = true;

    public PlayerControllerReader(int id, Receiver receiver, MessageInterpreter interpreter) {
        this.id = id;
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
                MessageBody message = receiver.nextMessage(); // io exception
                interpreter.interpret(message);
            } catch (IOException e) {
                close();
            }
        }
    }


    public void close() {
        open = false;
        receiver.close();
    }
}
