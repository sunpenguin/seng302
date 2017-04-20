package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import seng302.team18.model.Boat;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Race;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.*;

/**
 * Class that takes a Race and a Group and draws the Race on the Group.
 */
public class RaceRenderer {

    private Group group;
    private Race race;
    private Map<String, DisplayBoat> displayBoats = new HashMap<>();
    private Map<String, List<Circle>> trailMap = new HashMap<>();
    private Map<Circle, Coordinate> circleCoordMap = new HashMap<>();
    private Pane raceViewPane;
    private final double PADDING = 20.0;
    private int numBoats;
    final List<Color> BOAT_COLOURS = new ArrayList<>(
            Arrays.asList(Color.VIOLET, Color.BEIGE, Color.GREEN, Color.YELLOW, Color.RED, Color.BROWN));


    /**
     * Constructor for RaceRenderer, takes a Race, Group  and AnchorPane as parameters.
     *
     * @param race the race containing the displayBoats to be drawn
     * @param group the group to be drawn on
     * @param raceViewPane The AnchorPane the group is on
     */
    public RaceRenderer(Race race, Group group, Pane raceViewPane) {
        this.race = race;
        this.group = group;
        this.raceViewPane = raceViewPane;
    }


    /**
     * Draws displayBoats in the Race on the Group as well as the visible annotations
     */
    public void renderBoats() {
        for (int i = 0; i < race.getStartingList().size(); i++) {
            Boat boat = race.getStartingList().get(i);

            DisplayBoat displayBoat = displayBoats.get(boat.getShortName());
            if (displayBoat == null) {
                displayBoat = new DisplayBoat
                        (boat.getShortName(), boat.getHeading(), boat.getSpeed(), BOAT_COLOURS.get(numBoats++), boat.getEstimatedTimeNextMark());
                displayBoat.addToGroup(group);
                displayBoats.put(boat.getShortName(), displayBoat);
            }

            Coordinate boatCoordinates = boat.getCoordinate();
            if (boatCoordinates != null) {
                XYPair pixels = PixelMapper.convertCoordPixel(boatCoordinates, PADDING, raceViewPane, race.getCourse());
                displayBoat.toFront();
                displayBoat.moveBoat(pixels);
                displayBoat.setSpeed(boat.getSpeed());
                displayBoat.setHeading(boat.getHeading());
                displayBoat.setEstimatedTime(boat.getEstimatedTimeNextMark());
            }
        }
    }


    public void drawTrails() {
        for (int i = 0; i < race.getStartingList().size(); i++) {
            Boat boat = race.getStartingList().get(i);
            Coordinate boatCoordinates = boat.getCoordinate();
            if (boatCoordinates != null) {
                XYPair pixels = PixelMapper.convertCoordPixel(boatCoordinates, PADDING, raceViewPane, race.getCourse());
                drawTrail(boat, pixels);
            }
        }
    }


    /**
     * Drop a circle on the raceView to visualise the boat's route through the race.
     * Add the circle to the group and map the circle to it's coordinates.
     * @param boat Boat to put circle behind.
     * @param pixels point to place the circle at.
     */
    private void drawTrail(Boat boat, XYPair pixels) {
        Circle circle = new Circle();
        circleCoordMap.put(circle, boat.getCoordinate());

        circle.setCenterX(pixels.getX());
        circle.setCenterY(pixels.getY());
        circle.setRadius(0.6);
        circle.setFill(displayBoats.get(boat.getShortName()).getBoatColor());

        group.getChildren().add(circle);
        List<Circle> circles = trailMap.get(boat.getShortName());
        if (circles == null) {
            circles = new ArrayList<>();
            trailMap.put(boat.getShortName(), circles);
        }
        circles.add(circle);
    }


    /**
     * Redraw the the trails behind each boat by looking into the Map of circles and coordinates and
     * resetting the points.
     * @param boats Collection of displayBoats racing.
     */
    public void reDrawTrail(Collection<Boat> boats) {
        for (Boat boat : boats) {
            for (Circle circle : trailMap.get(boat.getShortName())) {
                XYPair newPosition = PixelMapper.convertCoordPixel
                        (circleCoordMap.get(circle), PADDING, raceViewPane, race.getCourse());
                circle.setCenterX(newPosition.getX());
                circle.setCenterY(newPosition.getY());
            }
        }
    }

    public Group getGroup() {
        return group;
    }

//    public Map<String, Boolean> getVisibleAnnotations() {
//        Map visibleAnnotations = new HashMap();
//        DisplayBoat boat = displayBoats.values().iterator().next(); // get a boat from the map LOL
//        for (AnnotationType type : AnnotationType.values()) {
//            visibleAnnotations.put(type, boat.getAnnotationVisible(type));
//        }
//        return visibleAnnotations;
//    }

    public void setVisibleAnnotations(AnnotationType type, Boolean isVisible) {
        for (DisplayBoat boat : displayBoats.values()) {
            boat.setAnnotationVisible(type, isVisible);
        }
    }
}