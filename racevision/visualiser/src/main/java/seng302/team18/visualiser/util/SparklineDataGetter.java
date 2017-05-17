package seng302.team18.visualiser.util;

import seng302.team18.model.Boat;
import seng302.team18.model.Course;
import seng302.team18.model.Race;

import java.util.List;

/**
 * Created by sbe67 on 13/05/17.
 */
public class SparklineDataGetter {

    private Race race;
    private SparklineDataQueue dataQueue;

    /**
     * Constructor
     *
     * @param race
     * @param dataQueue
     */
    public SparklineDataGetter(SparklineDataQueue dataQueue, Race race) {
        this.race = race;
        this.dataQueue = dataQueue;
    }

    /**
     * Listen to thhe changes of the boats leg number
     */
    public void listenToBoat() {
        for (Boat boat : race.getStartingList()) {
            boat.boatLegNumberProperty().addListener((observableValue, oldleg, newleg) -> {
                System.out.println("called" + boat.getBoatName());
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
//        System.out.println(race.getFinishedList());
//        if (race.getFinishedList().contains(boat)) {
//            System.out.println("finished");
//        System.out.println("boat status = " + boat.getStatus());
        if (boat.getStatus() == 3) {
            String finishLine = "Finsihline";
            SparklineDataPoint data = new SparklineDataPoint(boat, finishLine);
            dataQueue.enqueue(data);
        } else {
            if (boat.getLegNumber() == 2) { // assumed that the second leg always has legno of 2 TODO:check
                String startLine = "Startline";
                SparklineDataPoint data = new SparklineDataPoint(boat, startLine);
                dataQueue.enqueue(data);
            }
            String markPassedName = race.getCourse().getLeg(boat.getLegNumber()).getDeparture().getName();
            SparklineDataPoint data = new SparklineDataPoint(boat, markPassedName);
            dataQueue.enqueue(data);

        }
    }

}
