package seng302.team18.visualiser.display;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.Group;
import javafx.scene.web.WebEngine;
import seng302.team18.model.BoundaryMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;

import java.util.stream.Collectors;

/**
 * Using the geographic course information, requests a map image from the google map API and renders it in the
 * background of the course area.
 */
public class BackgroundRenderer {

    private final WebEngine webEngine;
    private final String API_KEY = "AIzaSyBRLXKbFcgD00-3nUQoIut-8sALaFq4elg";
    private final String API_URL = "https://maps.googleapis.com/maps/api/staticmap";
    private String mapURL = getClass().getResource("/googlemaps.html").toExternalForm();

    private final Course course;
    private Coordinate center;


    public BackgroundRenderer(Group group, Course course, WebEngine webEngine) {
        this.course = course;
        center = new Coordinate(0, 0);
        this.webEngine = webEngine;
        webEngine.load(mapURL);
        webEngine.setJavaScriptEnabled(true);
//        webEngine.getLoadWorker().stateProperty().addListener(
//                new ChangeListener<Worker.State>() {
//                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
//                        if (newState == Worker.State.SUCCEEDED) {
//                            Coordinate center = course.getCentralCoordinate();
//                            webEngine.executeScript("plspls(" + center.getLatitude() + ", " + center.getLongitude() + ");");
//                            System.out.println("I am called");
//                            webEngine.executeScript("map.setCenter({lat:" + center.getLatitude() + ", lng:" + center.getLongitude() + "});");
//                            webEngine.executeScript("console.log(\"loggedd\")");
//                        }
//                    }
//                });

    }

    /**
     * Updates the map shown in the background of the race view, based on the current course.
     * <br />
     * @return true if the map was updated false otherwise.
     */
    public boolean renderBackground() {
        Coordinate newCenter = course.getCentralCoordinate();
        if (!center.equals(newCenter)) {
            try {
//            webEngine.executeScript("map.setCenter({lat:" + center.getLatitude() + ", lng:" + center.getLongitude() + "});");
                webEngine.executeScript("setCenter(" + newCenter.getLatitude() + ", " + newCenter.getLongitude() + ");");
                center = newCenter;
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Constructs the API query, including all parameters.
     * This should be called each time the required map changes, to generate the respective URL.
     *
     * @return the URL for the API request
     */
    private String getURL() {
        String coordinates = course.getBoundaries()
                .stream()
                .map(BoundaryMark::getCoordinate)
                .map(coordinate -> "" + coordinate.getLatitude() + "," + coordinate.getLongitude())
                .collect(Collectors.joining("|"));
//        Coordinate centerCoordinate = gps.getCentralCoordinate(coordinates);
//        System.out.println(centerCoordinate);
//        gps.get
//        String string = API_URL + "?center=40.714728,-73.998672&zoom=12&size=400x400&key=" + API_KEY;
//        double centerLat = centerCoordinate.getLatitude();
//        double centerLong = centerCoordinate.getLongitude();
        String string = API_URL + "?markers=" + coordinates +"&size=400x400&key=" + API_KEY;
//        String string = API_URL + "?visible=" + coordinates +"&size=600x600&scale=10&key=" + API_KEY; // uncomment to make markers invisible
        return string;
    }

}
