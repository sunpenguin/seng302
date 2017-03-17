package seng302;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Class that takes a Race and a Group and draws the Race on the Group.
 */
public class RaceRenderer {

    private Group group;
    private Race race;

    private HashMap<String, Circle> boats;

    /**
     * Constructor for RaceRenderer, takes a Race and Group as parameters.
     *
     * @param race the race containing the boats to be drawn
     * @param group the group to be drawn on
     */
    public RaceRenderer(Race race, Group group) {
        this.race = race;
        this.group = group;
        boats = new HashMap<>();
        final double BOAT_RADIUS = 10.0;
        final ArrayList<Color> BOAT_COLOURS = new ArrayList<>(
                Arrays.asList(Color.BLACK, Color.BEIGE, Color.GREEN, Color.YELLOW, Color.RED, Color.BROWN));
        for (int i = 0; i < race.getStartingList().size(); i++) {
            boats.put(race.getStartingList().get(i).getBoatName(), new Circle(BOAT_RADIUS, BOAT_COLOURS.get(i)));
        }
    }

    /**
     * Draws boats in the Race on the Group.
     */
    public void renderBoats() {
        for (int i = 0; i < race.getStartingList().size(); i++) {
            Boat boat = race.getStartingList().get(i);
            Coordinate boatCoordinates = boat.getBoatCoordinates();
            ArrayList<Double> pixels = convertCoordPixel(boatCoordinates);
            Circle boatImage = boats.get(boat.getBoatName());
            boatImage.setCenterX(pixels.get(0));
            boatImage.setCenterY(pixels.get(1));
        }
    }

    /**
     * Converts the latitude / longitude coordinates to pixel coordinates.
     * @param coord Coordinates to be converted
     * @return x and y pixel coordinates of the given coordinates
     */
    private ArrayList<Double> convertCoordPixel(Coordinate coord) {
        ArrayList<Double> pixels = new ArrayList<>();
        double pixelWidth = group.getLayoutX();
        double pixelHeight = group.getLayoutY();
        double courseWidth =
                GPSCalculations.GPSxy(race.getCourse().getMaxX()) - GPSCalculations.GPSxy(race.getCourse().getMinX());
        double courseHeight =
                GPSCalculations.GPSxy(race.getCourse().getMaxY()) - GPSCalculations.GPSxy(race.getCourse().getMinY());
        double widthRatio = pixelWidth / courseWidth;
        double heightRatio = pixelHeight / courseHeight;
        double pixelX = coord.getLatitude() / widthRatio;
        double pixelY = coord.getLongitude() / heightRatio;
        pixels.add(pixelX);
        pixels.add(pixelY);
        return pixels;
    }

}
