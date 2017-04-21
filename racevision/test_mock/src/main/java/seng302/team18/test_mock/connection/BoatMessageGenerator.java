package seng302.team18.test_mock.connection;

import seng302.team18.model.Boat;
import seng302.team18.model.Coordinate;

import java.util.List;

/**
 * Generator for boat messages (see #4.9 in the AC35 Streaming protocol spec.)
 */
public class BoatMessageGenerator extends ScheduledMessage {
    private List<Boat> boats;
    private String message;

    public BoatMessageGenerator(List<Boat> boats) {
        super(5); //TODO magic number: fix this
        this.boats = boats;
    }

    @Override
    public String getMessage() {
        // TODO encode the message. Remember to check each boat to see that sending a message is appropriate for its situation
        for(Boat b: boats){
            //make message
            int sourceID = b.getId();
            Coordinate coordinate = b.getCoordinate();
            double lat = coordinate.getLatitude();
            double lon = coordinate.getLongitude();
            double heading = b.getHeading();
            double speed = b.getSpeed();
            message = String.valueOf(sourceID) + String.valueOf(lat) + String.valueOf(lon) + String.valueOf(heading) + String.valueOf(speed);
        }
        return message;
    }
}