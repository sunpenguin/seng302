package seng302.team18.visualiser.util;

import seng302.team18.message.BoatStatus;
import seng302.team18.model.Boat;
import seng302.team18.model.Race;

import java.util.Queue;

/**
 * Created by sbe67 on 13/05/17.
 */
public class SparklineDataGetter {

    private Race race;
    private Queue<SparklineDataPoint> dataQueue;

    /**
     * Constructor
     *
     * @param race the race
     * @param dataQueue queue to add boat position data to.
     */
    public SparklineDataGetter(Queue<SparklineDataPoint> dataQueue, Race race) {
        this.race = race;
        this.dataQueue = dataQueue;
    }

    /**
     * Listen to the changes of the boats leg number
     */
    public void listenToBoat() {
        for (Boat boat : race.getStartingList()) {
            boat.legNumberProperty().addListener((observableValue, oldleg, newleg) -> addData(boat, oldleg.intValue()));
        }
    }

    /**
     * Adds data to the dataQueue
     *
     * @param boat to be added to dataQueue
     * @param legJustRounded the number of the leg that the boat just completed
     */
    private void addData(Boat boat, int legJustRounded) {
        dataQueue.add(new SparklineDataPoint(
                boat.getShortName(),
                boat.getPlace(),
                race.getCourse().getMarkSequence().get(legJustRounded).getCompoundMark().getName()
        ));
    }

}
