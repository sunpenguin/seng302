package seng302.team18.racemodel.connection;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.parse.*;

import java.io.IOException;

/**
 * Class for reading and interpret messages sent by Human controlled players
 */
public class PlayerControlsReader implements Runnable {

    private int id;
    private Receiver receiver;
    private MessageInterpreter interpreter;
    private boolean open = true;

    public PlayerControlsReader(int id, Receiver receiver, MessageInterpreter interpreter) {
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
//            System.out.println("PlayerControlsReader::run");
            try {
                MessageBody message = receiver.nextMessage(); // io exception
                interpreter.interpret(message);
            } catch (IOException e) {
//                System.out.println("PlayerControlsReader::run closed");
                close();
            }
        }
    }


    public void close() {
        open = false;
        receiver.close();
//        System.out.println("PlayerControlsReader::close " + receiver.close());
    }


    public int getId() {
        return id;
    }
}
