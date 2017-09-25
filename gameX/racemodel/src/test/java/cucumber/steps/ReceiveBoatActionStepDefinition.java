package cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.RequestMessage;
import seng302.team18.message.RequestType;
import seng302.team18.parse.AC35MessageParserFactory;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.racemodel.connection.ConnectionListener;
import seng302.team18.racemodel.connection.Server;
import seng302.team18.encode.ControllerMessageFactory;
import seng302.team18.encode.Sender;

import javax.net.SocketFactory;
import java.net.Socket;
import java.util.Collections;

/**
 * Cucumber step definitions for receiving BoatActionMessage.
 */
public class ReceiveBoatActionStepDefinition {

    private Socket socket;
    private Sender sender;
    private Server server;
    private ConnectionListener listener;
    private Boat boat;
    private double oldHeading = 90d;
    private double windDirection = 0d;
    private double windSpeed = 8d;

    @Given("^a player has connected to the mock$")
    public void a_player_has_connected_to_the_mock() throws Throwable {
        int boatId = 1337;
        server = new Server(5649);
        socket = SocketFactory.getDefault().createSocket("localhost", 5649);
        sender = new Sender(socket, new ControllerMessageFactory());

        Race race = new Race();
        race.getCourse().setWindDirection(windDirection);
        race.getCourse().setWindSpeed(windSpeed);
        boat = new Boat("name", "short name", boatId, 10);
        boat.setHeading(oldHeading);
        race.addParticipant(boat);

        listener = new ConnectionListener(race, Collections.singletonList(boatId), new AC35MessageParserFactory());

        server.addObserver(listener);
        server.open();
        sender.send(new RequestMessage(RequestType.RACING));
        server.stopAcceptingConnections();
    }

    @When("^the mock gets sent an upwind message$")
    public void the_mock_gets_sent_an_upwind_message() throws Throwable {
        BoatActionMessage boatAction = new BoatActionMessage(1337);
        boatAction.setUpwind();
        sender.send(boatAction);
    }

    @Then("^the players boat will head upwind$")
    public void the_players_boat_will_head_upwind() throws Throwable {
        Thread.sleep(1000); // Wait for the server to receive and interpret sent message
        socket.close();
        server.close();
        double expected = 87d;
        Assert.assertEquals(expected, boat.getHeading(), 0.1);
    }

    @When("^the mock gets sent a downwind message$")
    public void the_mock_gets_sent_a_downwind_message() throws Throwable {
        BoatActionMessage boatAction = new BoatActionMessage(1337);
        boatAction.setDownwind();
        sender.send(boatAction);
    }

    @Then("^the players boat will head downwind$")
    public void the_players_boat_will_head_downwind() throws Throwable {
        Thread.sleep(1000);
        socket.close();
        server.close();
        double expected = 93d;
        Assert.assertEquals(expected, boat.getHeading(), 0.1);
    }

    @When("^the mock gets sent a sail in message$")
    public void the_mock_gets_sent_a_sail_in_message() throws Throwable {
        BoatActionMessage boatAction = new BoatActionMessage(1337);
        boatAction.setSailIn();
        sender.send(boatAction);
    }

    @Then("^the players boat will have its sails in$")
    public void the_players_boat_will_have_its_sails_in() throws Throwable {
        Thread.sleep(1000);
        socket.close();
        server.close();
        Assert.assertFalse(boat.isSailOut());
    }

    @Given("^the boat is heading directly upwind$")
    public void the_boat_is_heading_directly_upwind() throws Throwable {
        boat.setHeading(windDirection);
    }

    @When("^the mock gets sent an autopilot message$")
    public void the_mock_gets_sent_an_autopilot_message() throws Throwable {
        BoatActionMessage boatAction = new BoatActionMessage(1337);
        boatAction.setAutoPilot();
        sender.send(boatAction);
    }

    @Then("^the players boat will have its heading and speed changed to the optimal upwind vmg$")
    public void the_players_boat_will_have_its_heading_and_speed_changed_to_the_optimal_upwind_vmg() throws Throwable {
        Thread.sleep(1000);
        socket.close();
        server.close();
        double expected = 43.0d;
        Assert.assertEquals(expected, boat.getHeading(), 0.1);
    }

    @Given("^the boat is heading upwind")
    public void the_boat_is_heading_upwind() throws Throwable {
        boat.setHeading(45);
    }

    @When("^the mock gets sent a tack / gybe message$")
    public void the_mock_gets_sent_a_tack_gybe_message() throws Throwable {
        BoatActionMessage boatAction = new BoatActionMessage(1337);
        boatAction.setTackGybe();
        sender.send(boatAction);
    }

    @Then("^the players boat will tack$")
    public void the_players_boat_will_tack() throws Throwable {
        Thread.sleep(1000);
        socket.close();
        server.close();
        double expected = 315.0d;
        Assert.assertEquals(expected, boat.getHeading(), 0.1);
    }

    @Given("^the boat is heading downwind$")
    public void the_boat_is_heading_downwind() throws Throwable {
        boat.setHeading(225);
    }

    @Then("^the players boat will gybe$")
    public void the_players_boat_will_gybe() throws Throwable {
        Thread.sleep(1000);
        socket.close();
        server.close();
        double expected = 135.0d;
        Assert.assertEquals(expected, boat.getHeading(), 0.1);
    }
}
