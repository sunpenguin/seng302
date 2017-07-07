package seng302.team18.test_mock.model;

import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.model.Boat;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to generate a new boat.xml model for AC35 XML BoatMessage.
 */
public class BoatsModel {
    private List<Boat> boats = new ArrayList<>();
    private AC35XMLBoatMessage boatMessage;


    /**
     * Method that create a AC35 XML BoatMessage.
     * @return a AC35XMLBoatMessage
     */
    public AC35XMLBoatMessage getBoatMessage() {
        Boat boat1 = new Boat("Emirates Team New Zealand", "TEAM New Zealand", 121);
        Boat boat2 = new Boat("Oracle Team USA", "TEAM USA", 122);
        Boat boat3 = new Boat("Artemis Racing", "TEAM SWE", 123);
        Boat boat4 = new Boat("Groupama Team France", "TEAM France", 124);
        Boat boat5 = new Boat("Groupama Team France", "TEAM France", 125);
        Boat boat6 = new Boat("Softbank Team Japan", "TEAM Japan", 126);

        boats.add(boat1);
        boats.add(boat2);
        boats.add(boat3);
        boats.add(boat4);
        boats.add(boat5);
        boats.add(boat6);

        boatMessage = new AC35XMLBoatMessage(boats);

        return boatMessage;
    }
}

