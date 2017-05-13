package seng302.team18.visualiser.display;

import seng302.team18.model.Boat;
import seng302.team18.visualiser.util.SparklineDataPoint;
import seng302.team18.visualiser.util.SparklineDataQueue;

import java.util.List;
import java.util.Observable;

/**
 * Created by sbe67 on 13/05/17.
 */
public class SparklineDataGetter {

    private List<Boat> boats;
    private SparklineDataQueue dataQueue;

    /**
     * Constructor
     * @param boats
     * @param dataQueue
     */
    public SparklineDataGetter(List<Boat> boats , SparklineDataQueue dataQueue){
        this.boats = boats;
        this.dataQueue = dataQueue;
    }

    /**
     * Listen to thhe changes of the boats leg number
     */
    public void ListenToBoat() {
        for (Boat boat : boats) {
            boat.boatLegNumberProperty().addListener((observableValue, oldleg, newleg) -> {
                addData(boat);
            });
        }
    }

    /**
     * Adds data to the dataQueue
     * @param boat
     */
    private void addData(Boat boat){
        SparklineDataPoint data = new SparklineDataPoint(boat);
        dataQueue.enqueue(data);
    }

}
