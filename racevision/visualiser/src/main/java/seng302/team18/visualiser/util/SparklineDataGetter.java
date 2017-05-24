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
     * @param race
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
            boat.boatLegNumberProperty().addListener((observableValue, oldleg, newleg) -> {
                addData(boat);
            });
        }
    }

    /**
     * Adds data to the dataQueue
     *
     * @param boat to be added to dataQueue
     */
    private void addData(Boat boat) {
        if (boat.getStatus() == BoatStatus.FINISHED.code()) {
            String finishLine = "Finsihline";
            SparklineDataPoint data = new SparklineDataPoint(boat.getShortName(), boat.getPlace(), finishLine);
            dataQueue.add(data);
        } else {
            if (boat.getLegNumber() == 2) { // assumed that the second leg always has legno of 2 TODO:check
                String startLine = "Startline";
                SparklineDataPoint data = new SparklineDataPoint(boat.getShortName(), boat.getPlace(), startLine);
                dataQueue.add(data);
            }
            String markPassedName = race.getCourse().getLeg(boat.getLegNumber()).getDeparture().getName();
            SparklineDataPoint data = new SparklineDataPoint(boat.getShortName(), boat.getPlace(), markPassedName);
            dataQueue.add(data);

        }
    }

}
