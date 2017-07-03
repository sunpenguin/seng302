package seng302.team18.visualiser.util;

import seng302.team18.message.BoatStatus;
import seng302.team18.model.Yacht;
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
        for (Yacht yacht : race.getStartingList()) {
            yacht.boatLegNumberProperty().addListener((observableValue, oldleg, newleg) -> {
                addData(yacht);
            });
        }
    }

    /**
     * Adds data to the dataQueue
     *
     * @param yacht to be added to dataQueue
     */
    private void addData(Yacht yacht) {
        if (yacht.getStatus() == BoatStatus.FINISHED.code()) {
            String finishLine = "Finsihline";
            SparklineDataPoint data = new SparklineDataPoint(yacht.getNameShort(), yacht.getPlace(), finishLine);
            dataQueue.add(data);
        } else {
            if (yacht.getLegNumber() == 2) { // assumed that the second leg always has legno of 2 TODO:check
                String startLine = "Startline";
                SparklineDataPoint data = new SparklineDataPoint(yacht.getNameShort(), yacht.getPlace(), startLine);
                dataQueue.add(data);
            }
            String markPassedName = race.getCourse().getLeg(yacht.getLegNumber()).getDeparture().getName();
            SparklineDataPoint data = new SparklineDataPoint(yacht.getNameShort(), yacht.getPlace(), markPassedName);
            dataQueue.add(data);

        }
    }

}
