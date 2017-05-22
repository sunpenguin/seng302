package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.util.SparklineDataPoint;

import java.util.*;

/**
 * Class to display sparklines
 */
public class DisplaySparkline extends AnimationTimer {

    private Queue<SparklineDataPoint> dataQueue;
    private LineChart<String, String> sparklinesChart;
    private Map<String, Integer> seriesIndex = new HashMap<>();
    private List<String> seriesStyle = new ArrayList<>();


    /**
     * Constructor.
     * @param dataQueue a queue of SparklineDataPoints
     * @param boats a list of Boats to create sparklines for
     * @param sparklinesChart A linechart to display the sparklines on
     */
    public DisplaySparkline(Queue<SparklineDataPoint> dataQueue, Map<Boat, Color> boats, LineChart<String, String> sparklinesChart) {
        sparklinesChart.setCreateSymbols(false);
        sparklinesChart.setLegendVisible(false);
        this.dataQueue = dataQueue;
        this.sparklinesChart = sparklinesChart;

        sparklinesChart.getYAxis().setTickLabelGap(1);


        setupSeries(boats);
    }

    /**
     * Called in constructor.
     * Creates a  XYChart.Series<String, String> for each boat.
     * Sets name of series to boat name and colour of series to the boat colour.
     * @param boats HashMap in form <Boat, Color>
     */
    private void setupSeries(Map<Boat, Color> boats) {
        sparklinesChart.applyCss();
        int i = 0;
        for (Boat boat : boats.keySet()) {
            seriesIndex.put(boat.getName(), i);

            int j = i;
            String colorString = getHex(boats.get(boat));
            String  style = String.format("-fx-stroke: %s; -fx-background-color: %s, white; ", colorString, colorString);
            seriesStyle.add(style);

            XYChart.Series<String, String> boatSeries = new XYChart.Series<>();
//            boatSeries.getData().addListener(new ListChangeListener<XYChart.Data<String, String>>() {
//                int seriesNo = j;
//
//                @Override
//                public void onChanged(Change<? extends XYChart.Data<String, String>> c) {
//                    boatSeries.getNode().applyCss();
//                    Set<Node> nodes = sparklinesChart.lookupAll(".series" + seriesNo);
//                    for (Node n : nodes) {
//                        n.setStyle(style);
//                    }
//                    boatSeries.getNode().applyCss();
//                }
//            });

            boatSeries.setName(boat.getName());
            sparklinesChart.getData().add(boatSeries);

            Set<Node> nodes = sparklinesChart.lookupAll(".series" + i);
            for (Node n : nodes) {
                n.setStyle(String.format("-fx-stroke: %s; -fx-background-color: %s, white; ", colorString, colorString));
            }

            i++;
        }

//        sparklinesChart.applyCss();
//        Set<Node> nodes = sparklinesChart.lookupAll(".series" + 0);
//        for (Node n : nodes) {
//            n.setStyle("-fx-stroke: blue; -fx-background-color: blue, white; ");
//        }
    }

    private String getHex(Color color) {
        return String.format("#%02x%02x%02x", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }

    /**
     * Reads data from the data queue
     * @param currentTime
     */
    @Override
    public void handle(long currentTime) {
        SparklineDataPoint data = dataQueue.poll();
        if (data != null) {
            //Add data to relevant series
            editSingleSeries(data.getBoatName(), data);
        }
    }

    /**
     * Adds data to the correct series from the line chart.
     * @param boatName Name of the boat the data is for (also name of the series).
     * @param data SparklineDataPoint to be added to the series
     */
    private void editSingleSeries(String boatName, SparklineDataPoint data) {
        for (XYChart.Series<String, String> series : sparklinesChart.getData()) {
            if (series.getName().equals(boatName)) {
                String cat = data.getMarkPassedName();
                while (catInXofSeries(series, cat)) {
                    cat = cat + " "; // TODO: Doesn't display whitespace :)
                }
                series.getData().add(new XYChart.Data<>(cat, String.valueOf(data.getBoatPlace())));

//                series.getNode().applyCss();
//                int i = seriesIndex.get(boatName);
//                Set<Node> nodes = sparklinesChart.lookupAll(".series" + i);
//                for (Node n : nodes) {
//                    n.setStyle(seriesStyle.get(i));
//                }
//                series.getNode().applyCss();
            }
        }
    }

    /**
     * Checks if a category is already an X value in a series.
     * @param series Series to be checked against
     * @param category Category to check
     * @return boolean, true if the category is an X value in the series.
     */
    private boolean catInXofSeries(XYChart.Series<String, String> series, String category) {
        for (XYChart.Data<String, String> dataPoint : series.getData()) {
            if (dataPoint.getXValue().equals(category)) {
                return true;
            }
        }
        return false;
    }
}
