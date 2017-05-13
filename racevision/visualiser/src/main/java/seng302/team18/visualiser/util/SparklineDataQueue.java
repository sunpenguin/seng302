package seng302.team18.visualiser.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Class ac as a que made of sparkline data points
 * @see SparklineDataPoint
 */
public class SparklineDataQueue {

    private List<SparklineDataPoint> queue;

    /**
     * Constructor, creates the queue
     */
    public SparklineDataQueue(){
        queue = new ArrayList<>();
    }

    /**
     * Adds a data point to the end of the que
     * @param dataPoint sparklineDataPoint to be added to the que
     */
    public void enqueue(SparklineDataPoint dataPoint){
        queue.add(dataPoint);
    }

    /**
     * Dequeues the front data point of the queue and returns it
     * @return sparkline data point at the front of the queue
     */
    public SparklineDataPoint dequeue(){
        SparklineDataPoint data = queue.get(0);
        queue.remove(0);
        return data;
    }

}
