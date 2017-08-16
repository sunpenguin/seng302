package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.messageparsing.Receiver;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Interprets messages.
 */
public class Interpreter {

    private ExecutorService executor;
    private Receiver receiver;
    private MessageInterpreter interpreter;

    public Interpreter(Receiver receiver) {
        this.receiver = receiver;
    }


    /**
     * starts interpreting messages from the socket.
     */
    public void start() {
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while(true) {
                MessageBody messageBody = null;
                try {
                    messageBody = receiver.nextMessage();
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                interpreter.interpret(messageBody);
            }
        });
    }


    public void setInterpreter(MessageInterpreter interpreter) {
        this.interpreter = interpreter;
    }


    public Socket getSocket() {
        return receiver.getSocket();
    }


    public MessageInterpreter getInterpreter() {
        return interpreter;
    }


    /**
     * Shuts down the interpreter
     *
     */
    public boolean close() {
        executor.shutdownNow();
        return receiver.close();
    }

}
