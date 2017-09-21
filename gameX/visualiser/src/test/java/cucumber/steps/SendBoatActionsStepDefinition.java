package cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.encode.ControllerMessageFactory;
import seng302.team18.encode.Sender;


import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Cucumber step definitions for sending BoatActionMessage.
 */
public class SendBoatActionsStepDefinition {

    private BoatActionMessage message;
    private Sender sender;
    private Socket socket;
    private ServerSocket serverSocket;

    @Given("^the user has connected to the game$")
    public void the_user_has_connected_to_the_game() throws Throwable {
        serverSocket = ServerSocketFactory.getDefault().createServerSocket(6423);
        socket = SocketFactory.getDefault().createSocket("localhost", 6423);
        serverSocket.accept();
        sender = new Sender(socket, new ControllerMessageFactory());
    }

    @When("^the upwind key is pressed$")
    public void the_upwind_key_is_pressed() throws Throwable {
        message = new BoatActionMessage(420);
        message.setUpwind();
    }

    @Then("^the message will be sent$")
    public void the_message_will_be_sent() {
        try {
            sender.send(message);
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
    }

    @When("^the downwind key is pressed$")
    public void the_downwind_key_is_pressed() throws Throwable {
        message = new BoatActionMessage(420);
        message.setDownwind();
    }

    @When("^the sail in / out key is pressed$")
    public void the_sail_in_out_key_is_pressed() throws Throwable {
        message = new BoatActionMessage(420);
        message.setSailIn();
    }

    @When("^the autopilot key is pressed$")
    public void the_autopilot_key_is_pressed() throws Throwable {
        message = new BoatActionMessage(420);
        message.setAutoPilot();
    }

    @When("^the tack / gybe key is pressed$")
    public void the_tack_gybe_key_is_pressed() throws Throwable {
        message = new BoatActionMessage(420);
        message.setTackGybe();
    }
}
