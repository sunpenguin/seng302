package seng302.team18.visualiser.interpret;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.MessageBody;
import seng302.team18.messageparsing.Receiver;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Observable;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Interprets messages.
 */
public class Interpreter extends Observable {

    private ZonedDateTime timeout;
    private ExecutorService executor;
    private Receiver receiver;
    private MessageInterpreter interpreter;

    public Interpreter(Receiver receiver) {
        this.receiver = receiver;
    }


    /**
     * starts interpret messages from the socket.
     */
    public void start() {

        timeout = ZonedDateTime.now().plusSeconds(5);
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
             while(true) {

                 if (ZonedDateTime.now().isAfter(timeout)) {
                     setChanged();
                     notifyObservers(true);
                     return;
                 } else {
                     MessageBody messageBody = null;
                     try {
                         messageBody = receiver.nextMessage();
                     } catch (IOException e1) {
                         close();
                         closeReceiver();
                     } catch (Exception e2){
                         e2.printStackTrace();
                         System.err.println("e2 interpreter start method");
                     }
                     if (messageBody != null) {
                         timeout = ZonedDateTime.now().plusSeconds(5);
                     }

                     interpreter.interpret(messageBody);
                 }
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
     */
    public void close() {
        executor.shutdownNow();

    }


    /**
     * Shuts down the receiver
     */
    public boolean closeReceiver(){
        return receiver.close();
    }
}
