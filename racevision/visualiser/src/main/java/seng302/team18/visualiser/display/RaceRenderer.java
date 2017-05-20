package seng302.team18.visualiser.display;

import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import seng302.team18.model.Boat;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Race;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.*;

/**
 * Class that takes a Race and a Group and draws the Race on the Group.
 */
public class RaceRenderer {

    private Group group;
    private Race race;
    // Boat::getShortName is used as the key for all Maps
    private Map<Boat, DisplayBoat> displayBoats = new HashMap<>();
    private Map<Boat, DisplayTrail> trailMap = new HashMap<>();
    private Map<Boat, Double> headingMap;
    private final double PADDING = 20.0;
    private int numBoats;
    private final List<Color> BOAT_COLOURS = new ArrayList<>(
            Arrays.asList(Color.VIOLET, Color.DARKVIOLET, Color.GREEN, Color.TOMATO, Color.YELLOWGREEN, Color.BROWN));
    private PixelMapper pixelMapper;



    /**
     * Constructor for RaceRenderer, takes a Race, Group  and AnchorPane as parameters.
     *
     * @param pixelMapper for converting coordinates to pixel coordinates
     * @param race the race containing the displayBoats to be drawn
     * @param group the group to be drawn on
     */
    public RaceRenderer(PixelMapper pixelMapper, Race race, Group group) {
        this.race = race;
        this.group = group;
        this.pixelMapper = pixelMapper;
        headingMap = new HashMap<>();
    }


    /**
     * Draws displayBoats in the Race on the Group as well as the visible annotations
     */
    public void renderBoats() {
        for (int i = 0; i < race.getStartingList().size(); i++) {
            Boat boat = race.getStartingList().get(i);

            DisplayBoat displayBoat = displayBoats.get(boat);
            if (displayBoat == null) {
                displayBoat = new DisplayBoat
                        (pixelMapper, boat.getShortName(), boat.getHeading(), boat.getKnotsSpeed(), BOAT_COLOURS.get(numBoats++), boat.getTimeTilNextMark());
                displayBoat.addToGroup(group);
                displayBoats.put(boat, displayBoat);
            }

            Coordinate boatCoordinates = boat.getCoordinate();
            if (boatCoordinates != null) {
                displayBoat.setDisplayOrder();
                displayBoat.moveBoat(boatCoordinates);
                displayBoat.setSpeed(boat.getKnotsSpeed());
                displayBoat.setHeading(boat.getHeading());
                displayBoat.setEstimatedTime(boat.getTimeTilNextMark());
                displayBoat.setTimeSinceLastMark(boat.getTimeSinceLastMark());
                displayBoat.setScale(pixelMapper.getZoomFactor());
            }
        }
    }


    /**
     * Draws all the boats trails.
     */
    public void drawTrails() {
        for (int i = 0; i < race.getStartingList().size(); i++) {
            Boat boat = race.getStartingList().get(i);
            Coordinate boatCoordinates = boat.getCoordinate();
            if (boatCoordinates != null) {
                drawTrail(boat, pixelMapper);
            }
        }
    }


    /**
     * Update a single boats trail.
     * @param boat the trail belongs to.
     * @param pixelMapper used to map a coordinate to a point on the screen.
     */
    private void drawTrail(Boat boat, PixelMapper pixelMapper) {
        final double MAX_HEADING_DIFFERENCE = 0.75d; // smaller => smoother trail, higher => more fps
        DisplayTrail trail = trailMap.get(boat);
        if (trail == null) {
            final int MAX_TRAIL_LENGTH = 100;
            DisplayBoat displayBoat = displayBoats.get(boat);
            trail = new DisplayTrail(boat.getShortName(), displayBoat.getBoatColor(), MAX_HEADING_DIFFERENCE, MAX_TRAIL_LENGTH);
            trailMap.put(boat, trail);
            trail.addToGroup(group);
        }
        headingMap.put(boat, boat.getHeading());
        trail.addPoint(boat.getCoordinate(), boat.getHeading(), pixelMapper);
    }


    /**
     * Redraw the the trails behind each boat.
     * @param boats Collection of displayBoats racing.
     */
    public void reDrawTrails(Collection<Boat> boats) {
        for (Boat boat : boats) {
            DisplayTrail trail = trailMap.get(boat);
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

    public Map<Boat, Color> boatColors() {
        Map<Boat, Color> boatColors = new HashMap<>();
        for (Map.Entry<Boat, DisplayBoat> entry : displayBoats.entrySet()){
            boatColors.put(entry.getKey(), entry.getValue().getBoatColor());
        }
        return boatColors;
    }
}