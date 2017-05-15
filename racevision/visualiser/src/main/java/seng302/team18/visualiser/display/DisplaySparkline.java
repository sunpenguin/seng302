package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.util.SparklineDataPoint;
import seng302.team18.visualiser.util.SparklineDataQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to display sparklines
 */
public class DisplaySparkline extends AnimationTimer {

    private SparklineDataQueue dataQueue;
    private List<Boat> boats;
    private List<XYChart.Series> sparklineSeries = new ArrayList<>();
    private LineChart<? ,?> sparklinesChart;

    /**
     * Constructor, also creates the series
     * @param dataQueue to to take sparkline data from.
     * @param boats
     */
    public DisplaySparkline(SparklineDataQueue dataQueue, List<Boat> boats,  LineChart<? ,?> sparklinesChart){
        this.dataQueue = dataQueue;
        this.boats = boats;
        setupSeries();
        this.sparklinesChart = sparklinesChart;
        for(XYChart.Series series: sparklineSeries){
            sparklinesChart.getData().addAll(series);
        }
    }

    private void setupSeries(){
        for(Boat boat : boats){
            XYChart.Series boatSeries = new XYChart.Series();
            boatSeries.setName(boat.getBoatName());
            sparklineSeries.add(boatSeries);
        }
    }

    @Override
    public void handle(long currentTime){
        //Dequeue
        SparklineDataPoint data = dataQueue.dequeue();
        if(data == null){
            return;
        }
        //Add data to relevant series
        editSingleSeries(data.getBoatName(), data);
    }

    private void editSingleSeries( String boatName, SparklineDataPoint data){
        for(XYChart.Series series : sparklineSeries){
            if(series.getName() == boatName){
                String cat = String.valueOf(data.getMarkPassedName());
                while(catInXofSeries(series, cat)){
                    cat = cat + " "; //Doesn't display whitespace :) TODO:why?
                }
                series.getData().add(new XYChart.Data(cat, String.valueOf(data.getBoatPlace())));
            }
        }
    }

    private boolean catInXofSeries(XYChart.Series series, String category){
        boolean answer = false;
        ObservableList<XYChart.Data<?,?>> seriesData = series.getData();
        for(XYChart.Data datapoint : seriesData) {
            if (datapoint.getXValue().equals(category)) {
                answer = true;
            }
        }
        return answer;
    }
}
