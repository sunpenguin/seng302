package seng302.team18.racemodel.connection;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.messageparsing.*;

/**
 * Class for reading and interpreting messages sent by Human controlled players
 */
public class PlayerControllerReader implements Runnable {

    private int id;
    private Receiver receiver;
    private MessageInterpreter interpreter;
    boolean open = true;

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
                MessageBody message = receiver.nextMessage();

                // For future extension of this, we should generify player sent messages so we can have access to the id
                // without having to cast to a specific message e.g. BoatActionMessage / ChatMessage
                if (message instanceof BoatActionMessage) {
                    BoatActionMessage boatAction = (BoatActionMessage) message;

                    if (boatAction.getId() == id) {
                        interpreter.interpret(message);
                    }
                }
//                Thread.sleep(1000L);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }


    public void close() {
        open = false;
        receiver.close();
    }
}
