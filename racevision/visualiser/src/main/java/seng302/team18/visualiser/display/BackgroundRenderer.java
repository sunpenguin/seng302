package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng302.team18.model.BoundaryMark;
import seng302.team18.model.Course;

import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;

/**
 * Using the geographic course information, requests a map image from the google map API and renders it in the
 * background of the course area.
 */
public class BackgroundRenderer {
    private final Course course;
    private final WebView map;
    private final WebEngine webEngine;

    private final String API_KEY = "AIzaSyBRLXKbFcgD00-3nUQoIut-8sALaFq4elg";
    private final String API_URL = "https://maps.googleapis.com/maps/api/staticmap";
    private final String mapURL = getClass().getResource("/googlemaps.html").toExternalForm();


    public BackgroundRenderer(Group group, Course course, WebView map) {
        this.course = course;
        this.map = map;
        webEngine = map.getEngine();
    }

    /**
     * Updates the map shown in the background of the race view, based on the current course.
     * <br />
     * If the map cannot be downloaded, the background image becomes transparent, and a message is written to stderr
     */
    public void renderBackground() throws IOException {
//        String urlString = getURL();
//        URL url = new URL(urlString);
        webEngine.load(mapURL);
//        imageView.setImage(new Image(url.openStream()));
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

//    /**
//     * Hides the map by setting the image to null.
//     * Currently relies on the group providing a suitable default background.
//     */
//    public void hideMap() {
//        imageView.setImage(null);
//    }
}
