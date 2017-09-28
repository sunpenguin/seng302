package seng302.team18.racemodel.connection;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.ColourMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.parse.Receiver;

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
            try {
                MessageBody message = receiver.nextMessage(); // io exception
                if (hasCorrectId(message)) {
                    interpreter.interpret(message);
                }
            } catch (IOException e) {
                close();
            }
        }
    }


    private boolean hasCorrectId(MessageBody message) {
        if (message instanceof BoatActionMessage) {
            BoatActionMessage action = (BoatActionMessage) message;
            return action.getId() == id;
        } else if (message instanceof ColourMessage) {
            ColourMessage colourMessage = (ColourMessage) message;
            return colourMessage.getSourceID() == id;
        }
        return true;
    }


    public void close() {
        open = false;
        receiver.close();
    }


    public int getId() {
        return id;
    }
}
