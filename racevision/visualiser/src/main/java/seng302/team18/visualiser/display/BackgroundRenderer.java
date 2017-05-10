package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng302.team18.model.Course;

import java.io.IOException;
import java.net.URL;

/**
 * Using the geographic course information, requests a map image from the google map API and renders it in the
 * background of the course area.
 */
public class BackgroundRenderer {
    private Course course;
    private ImageView imageView = new ImageView();

    private final String API_KEY = "AIzaSyBRLXKbFcgD00-3nUQoIut-8sALaFq4elg";
    private final String API_URL = "https://maps.googleapis.com/maps/api/staticmap";


    public BackgroundRenderer(Group group, Course course) {
        this.course = course;
        group.getChildren().add(0, imageView);
        imageView.toBack();

        renderBackground();
    }

    /**
     * Updates the map shown in the background of the race view, based on the current course.
     * <br />
     * If the map cannot be downloaded, the background image becomes transparent, and a message is written to stderr
     */
    public void renderBackground() {
        String urlString = getURL();
        try {
            URL url = new URL(urlString);

            imageView.setImage(new Image(url.openStream()));

        } catch (IOException e) {
            hideMap();
            System.err.println("Failed to download map from " + urlString.substring(0, urlString.length() - API_KEY.length()) + "****");
            return;
        }

    }

    /**
     * Constructs the API query, including all parameters.
     * This should be called each time the required map changes, to generate the respective URL.
     *
     * @return the URL for the API request
     */
    private String getURL() {
        String string = API_URL + "?center=40.714728,-73.998672&zoom=12&size=400x400&key=" + API_KEY;
        return string;
    }

    /**
     * Hides the map by setting the image to null.
     * Currently relies on the group providing a suitable default background.
     */
    private void hideMap() {
        imageView.setImage(null);
    }
}
