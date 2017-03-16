package seng302;

import com.sun.xml.internal.bind.v2.runtime.Coordinator;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by dhl25 on 16/03/17.
 */
public class RaceRenderer {

    private Group group;
    private Race race;

    private HashMap<String, Circle> boats = new HashMap<>();
    private final ArrayList<Color> BOAT_COLOURS = new ArrayList<>(
            Arrays.asList(Color.BLACK, Color.BEIGE, Color.BLUE, Color.YELLOW, Color.RED, Color.HONEYDEW));

    public RaceRenderer(Race race, Group group) {
        final double BOAT_RADIUS = 10.0;
        this.race = race;
        this.group = group;
        for (int i = 0; i < race.getStartingList().size(); i++) {
            boats.put(race.getStartingList().get(i).getBoatName(), new Circle(BOAT_RADIUS, BOAT_COLOURS.get(i)));
        }
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

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

    private ArrayList<Double> convertCoordPixel(Coordinate coord) {
        ArrayList<Double> pixels = new ArrayList<>();
        double pixelWidth = group.getLayoutX();
        double pixelHeight = group.getLayoutY();
        Coordinate maxCoord = race.getCourse().getMaxCoord();
        Coordinate minCoord = race.getCourse().getMinCoord();
        double coordWidth = (maxCoord.getLatitude() - minCoord.getLatitude());
        double coordHeight = (maxCoord.getLongitude() - minCoord.getLongitude());
        double widthRatio = (pixelWidth / coordWidth);
        double heigtRatio = (pixelHeight / coordHeight);
        double pixelX = coord.getLatitude / widthRatio;
        double pixelY = coord.getLongitude / heigtRatio;
        pixels.add(pixelX);
        pixels.add(pixelY);
        return pixels;
    }

}
