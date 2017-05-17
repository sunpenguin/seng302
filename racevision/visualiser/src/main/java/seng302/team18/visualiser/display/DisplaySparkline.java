package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
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
    private LineChart<?, ?> sparklinesChart;

    /**
     * Constructor, also creates the series
     *
     * @param dataQueue to to take sparkline data from.
     * @param boats
     */
    public DisplaySparkline(SparklineDataQueue dataQueue, List<Boat> boats, LineChart<?, ?> sparklinesChart) {
        this.dataQueue = dataQueue;
        this.boats = boats;
        setupSeries();
        this.sparklinesChart = sparklinesChart;
        sparklinesChart.getYAxis().setTickLabelGap(1);
        sparklinesChart.getYAxis();
        for (XYChart.Series series : sparklineSeries) {
            sparklinesChart.getData().addAll(series);
        }
    }

    private void setupSeries() {
        for (Boat boat : boats) {
            XYChart.Series boatSeries = new XYChart.Series();
            boatSeries.setName(boat.getBoatName());
            sparklineSeries.add(boatSeries);
        }
    }

    @Override
    public void handle(long currentTime) {
        //Dequeue
        SparklineDataPoint data = dataQueue.dequeue();
        if (data != null) {
            //Add data to relevant series
            editSingleSeries(data.getBoatName(), data);
        }
    }

    private void editSingleSeries(String boatName, SparklineDataPoint data) {
        for (XYChart.Series series : sparklineSeries) {
            if (series.getName().equals(boatName)) {
                String cat = data.getMarkPassedName();
                while (catInXofSeries(series, cat)) {
                    cat = cat + " "; // TODO: Doesn't display whitespace :)
                }
                series.getData().add(new XYChart.Data(cat, data.getBoatPlace()));
//                series.getData().add(new XYChart.Data(cat, String.valueOf(data.getBoatPlace())));
            }
        }
    }

    private boolean catInXofSeries(XYChart.Series series, String category) {
        ObservableList<XYChart.Data> seriesData = series.getData();
        for (XYChart.Data dataPoint : seriesData) {
            if (dataPoint.getXValue().equals(category)) {
                return true;
            }
        }
        return false;
    }
}
