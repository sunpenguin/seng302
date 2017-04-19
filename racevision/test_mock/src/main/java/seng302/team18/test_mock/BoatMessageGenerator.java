package seng302.team18.test_mock;

import seng302.team18.model.Boat;

import java.util.List;

/**
 * Generator for boat messages (see #4.9 in the AC35 Streaming protocol spec.)
 */
public class BoatMessageGenerator extends ScheduledMessage {
    private List<Boat> boats;

    public BoatMessageGenerator(List<Boat> boats) {
        super(5); //TODO magic number: fix this
        this.boats = boats;
    }

    @Override
    protected void generateMessage() {
        // TODO encode the message. Remember to check each boat to see that sending a message is appropriate for its situation
    }
}
