package seng302.team18.visualiser.util;

import seng302.team18.model.Boat;
import seng302.team18.model.Course;

import java.util.List;

/**
 * Created by sbe67 on 13/05/17.
 */
public class SparklineDataGetter {

    private List<Boat> boats;
    private SparklineDataQueue dataQueue;
    private Course course;

    /**
     * Constructor
     *
     * @param boats
     * @param dataQueue
     */
    public SparklineDataGetter(List<Boat> boats, SparklineDataQueue dataQueue, Course course) {
        this.boats = boats;
        this.dataQueue = dataQueue;
        this.course = course;
    }

    /**
     * Listen to thhe changes of the boats leg number
     */
    public void listenToBoat() {
        for (Boat boat : boats) {
            boat.boatLegNumberProperty().addListener((observableValue, oldleg, newleg) -> {
                addData(boat);
            });
        }
    }

    /**
     * Adds data to the dataQueue
     *
     * @param boat
     */
    private void addData(Boat boat) {
        if (boat.getBoatLegNumber() == -1) {
            String finishLine = "Finsihline";
            SparklineDataPoint data = new SparklineDataPoint(boat, finishLine);
            dataQueue.enqueue(data);
        } else {
            if (boat.getBoatLegNumber() == 2) { // assumed that the second leg always has legno of 2 TODO:check
                String startLine = "Startline";
                SparklineDataPoint data = new SparklineDataPoint(boat, startLine);
                dataQueue.enqueue(data);
            }
            String markPassedName = course.getLeg(boat.getBoatLegNumber()).getDeparture().getName();
            SparklineDataPoint data = new SparklineDataPoint(boat, markPassedName);
            dataQueue.enqueue(data);
        }
    }

}
