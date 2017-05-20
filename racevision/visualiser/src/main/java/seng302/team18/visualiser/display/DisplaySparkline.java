package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.util.SparklineDataPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Class to display sparklines
 */
public class DisplaySparkline extends AnimationTimer {

    private Queue<SparklineDataPoint> dataQueue;
    private List<Boat> boats;
    private List<XYChart.Series> sparklineSeries = new ArrayList<>();
    private LineChart sparklinesChart;


    public DisplaySparkline(Queue<SparklineDataPoint> dataQueue, List<Boat> boats, LineChart sparklinesChart) {
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
            boatSeries.setName(boat.getName());
            sparklineSeries.add(boatSeries);
        }
    }

    @Override
    public void handle(long currentTime) {
        SparklineDataPoint data = dataQueue.poll();
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
                series.getData().add(new XYChart.Data(cat, String.valueOf(data.getBoatPlace())));
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
