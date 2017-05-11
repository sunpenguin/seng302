package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
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
    private Map<String, DisplayTrail> trailMap = new HashMap<>();
    private Map<String, Double> headingMap;
    private Pane raceViewPane;
    private final double PADDING = 20.0;
    private int numBoats;
    private final List<Color> BOAT_COLOURS = new ArrayList<>(
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
        headingMap = new HashMap<>();
    }


    /**
     * Draws displayBoats in the Race on the Group as well as the visible annotations
     */
    public void renderBoats() {
        PixelMapper pixelMapper = new PixelMapper(race.getCourse(), raceViewPane, PADDING);
        for (int i = 0; i < race.getStartingList().size(); i++) {
            Boat boat = race.getStartingList().get(i);

            DisplayBoat displayBoat = displayBoats.get(boat.getShortName());
            if (displayBoat == null) {
                displayBoat = new DisplayBoat
                        (boat.getShortName(), boat.getHeading(), boat.getSpeed(), BOAT_COLOURS.get(numBoats++), boat.getTimeTilNextMark());
                displayBoat.addToGroup(group);
                displayBoats.put(boat.getShortName(), displayBoat);
            }

            Coordinate boatCoordinates = boat.getCoordinate();
            if (boatCoordinates != null) {
                XYPair pixels = pixelMapper.convertCoordPixel(boatCoordinates);
                displayBoat.moveBoat(pixels);
                displayBoat.setSpeed(boat.getSpeed());
                displayBoat.setHeading(boat.getHeading());
                displayBoat.setEstimatedTime(boat.getTimeTilNextMark());
                displayBoat.setTimeSinceLastMark(boat.getTimeSinceLastMark());
                for (DisplayBoat dBoat : displayBoats.values()) {
                    dBoat.toFront();
                }
            }
        }
    }


    public void drawTrails() {
        PixelMapper pixelMapper = new PixelMapper(race.getCourse(), raceViewPane, PADDING);
        for (int i = 0; i < race.getStartingList().size(); i++) {
            Boat boat = race.getStartingList().get(i);
            Coordinate boatCoordinates = boat.getCoordinate();
            if (boatCoordinates != null) {
                drawTrail(boat, pixelMapper);
            }
        }
    }


//    /**
//     * Drop a circle on the raceView to visualise the boat's route through the race.
//     * Add the circle to the group and map the circle to it's coordinates.
//     * @param boat Boat to put circle behind.
//     * @param pixels point to place the circle at.
//     */
    private void drawTrail(Boat boat, PixelMapper pixelMapper) {
        final double MAX_HEADING_DIFFERENCE = 0.75d; // smaller => smoother trail, higher => more fps
        DisplayTrail trail = trailMap.get(boat.getShortName());
        if (trail == null) {
            final int MAX_TRAIL_LENGTH = 100;
            DisplayBoat displayBoat = displayBoats.get(boat.getShortName());
            trail = new DisplayTrail(boat.getShortName(), displayBoat.getBoatColor(), MAX_HEADING_DIFFERENCE, MAX_TRAIL_LENGTH);
            trailMap.put(boat.getShortName(), trail);
            trail.addToGroup(group);
        }
        headingMap.put(boat.getShortName(), boat.getHeading());
        trail.addPoint(boat.getCoordinate(), boat.getHeading(), pixelMapper);
    }


//    /**
//     * Redraw the the trails behind each boat by looking into the Map of circles and coordinates and
//     * resetting the points.
//     * @param boats Collection of displayBoats racing.
//     */
    public void reDrawTrails(Collection<Boat> boats) {
        PixelMapper pixelMapper = new PixelMapper(race.getCourse(), raceViewPane, PADDING);
        for (Boat boat : boats) {
            DisplayTrail trail = trailMap.get(boat.getShortName());
            if (trail != null) {
                trail.reDraw(pixelMapper);
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