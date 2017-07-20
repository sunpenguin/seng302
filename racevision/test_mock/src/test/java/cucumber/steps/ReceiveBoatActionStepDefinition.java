package cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.//Assert;
import seng302.team18.message.BoatActionMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.message.RequestMessage;
import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;
import seng302.team18.send.ControllerMessageFactory;
import seng302.team18.send.Sender;
import seng302.team18.test_mock.ConnectionListener;
import seng302.team18.test_mock.connection.Server;

import javax.net.SocketFactory;
import java.net.Socket;
import java.util.Collections;

/**
 * Created by dhl25 on 21/07/17.
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
        server = new Server(5647);
        socket = SocketFactory.getDefault().createSocket("localhost", 5647);
        sender = new Sender(socket, new ControllerMessageFactory());

        Race race = new Race();
        race.getCourse().setWindDirection(windDirection);
        race.getCourse().setWindSpeed(windSpeed);
        boat = new Boat("name", "short name", boatId);
        boat.setHeading(oldHeading);
        race.addParticipant(boat);

        listener = new ConnectionListener(race, Collections.singletonList(boatId), new AC35MessageParserFactory());

        server.addObserver(listener);
        server.openServer();
        server.stopAccepting();
        sender.send(new RequestMessage(true));
    }

    @When("^the mock gets sent an upwind message$")
    public void the_mock_gets_sent_an_upwind_message() throws Throwable {
        BoatActionMessage boatAction = new BoatActionMessage();
        boatAction.setUpwind(true);
        sender.send(boatAction);
    }

    @Then("^the players boat will head upwind\\.$")
    public void the_players_boat_will_head_upwind() throws Throwable {
        socket.close();
        server.close();
        double expected = 87d;
        //Assert.assertEquals(expected, boat.getHeading(), 0.1);
    }

    @When("^the mock gets sent a downwind message$")
    public void the_mock_gets_sent_a_downwind_message() throws Throwable {
        BoatActionMessage boatAction = new BoatActionMessage();
        boatAction.setDownwind(true);
        sender.send(boatAction);
    }

    @Then("^the players boat will head downwind\\.$")
    public void the_players_boat_will_head_downwind() throws Throwable {
        socket.close();
        server.close();
        double expected = 93d;
        //Assert.assertEquals(expected, boat.getHeading(), 0.1);
    }

    @When("^the mock gets sent a sail in message$")
    public void the_mock_gets_sent_a_sail_in_message() throws Throwable {
        BoatActionMessage boatAction = new BoatActionMessage();
        boatAction.setSailsIn(true);
        sender.send(boatAction);
    }

    @Then("^the players boat will have its sails in\\.$")
    public void the_players_boat_will_have_its_sails_in() throws Throwable {
        socket.close();
        server.close();
        ////Assert.assertFalse(boat.isSailOut());
    }

    @Given("^the boat is heading directly upwind\\.$")
    public void the_boat_is_heading_directly_upwind() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        boat.setHeading(windDirection);
    }

    @When("^the mock gets sent an autopilot message\\.$")
    public void the_mock_gets_sent_an_autopilot_message() throws Throwable {
        BoatActionMessage boatAction = new BoatActionMessage();
        boatAction.setAutopilot(true);
        sender.send(boatAction);
    }

    @Then("^the players boat will have its heading and speed changed to the optimal upwind vmg\\.$")
    public void the_players_boat_will_have_its_heading_and_speed_changed_to_the_optimal_upwind_vmg() throws Throwable {
        socket.close();
        server.close();
        double expected = 93d;
        //Assert.assertEquals(expected, boat.getHeading(), 0.1);
    }


}
