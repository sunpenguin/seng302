package cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.messageparsing.*;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.send.ControllerMessageFactory;
import seng302.team18.visualiser.send.Sender;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by sbe67 on 21/07/17.
 */
public class ControlSail {
    private Boat boat;
    private BoatActionMessage boatActionMessage = new BoatActionMessage();
    private ServerSocket serverSocket;
    private Socket socket;
    private Sender sender;
    private Receiver receiver;
    private MessageParserFactory parserFactory;


    @Given("^I am controling a boat$")
    public void i_am_controling_a_boat() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        boat = new Boat("Team NZ", "Team New Zealand", 123);

        serverSocket = ServerSocketFactory.getDefault().createServerSocket(6423);
        socket = SocketFactory.getDefault().createSocket("localhost", 6423);
        serverSocket.accept();
        sender = new Sender(socket, new ControllerMessageFactory());
        receiver = new Receiver(socket, parserFactory);
        throw new PendingException();
    }

    @Given("^I press the shift key$")
    public void i_press_the_shift_key() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        boatActionMessage.setSailsIn(true);
        throw new PendingException();
    }

    @Then("^a BoatAction message should be sent$")
    public void a_BoatAction_message_should_be_sent() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try {
            sender.send(boatActionMessage);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.assertTrue(false);
            e.printStackTrace();
        }

        try {
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new PendingException();
    }

    @Then("^sailIn should be false$")
    public void sailin_should_be_false() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        BoatActionMessage message= (BoatActionMessage) receiver.nextMessage();
        Assert.assertFalse(message.isSailsIn());
        throw new PendingException();
    }

    @Then("^sailIn should be true$")
    public void sailin_should_be_true() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("^the sail is in$")
    public void the_sail_is_in() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("^the sail is out$")
    public void the_sail_is_out() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

}
