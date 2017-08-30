package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import seng302.team18.model.*;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.*;

/**
 * Class that takes a Race and a Group and draws the Race on the Group.
 */
public class RaceRenderer {

    private Group group;
    private DisplayRace race;
    private Map<String, DisplayBoat> displayBoats = new HashMap<>();
    private Map<String, DisplayTrail> trailMap = new HashMap<>();
    private Map<String, Double> headingMap;
    private int numBoats;
    //TODO: remove these colours sbe67 17/8
    private final List<Color> BOAT_COLOURS = Arrays.asList(
            Color.VIOLET,
            Color.DARKVIOLET,
            Color.GREEN,
            Color.TOMATO,
            Color.YELLOWGREEN,
            Color.BROWN,
            Color.CHOCOLATE,
            Color.DARKGREEN,
            Color.GOLDENROD,
            Color.DARKORCHID,
            Color.DARKRED,
            Color.INDIANRED,
            Color.MEDIUMAQUAMARINE,
            Color.MEDIUMSPRINGGREEN,
            Color.SEAGREEN,
            Color.YELLOW,
            Color.ORANGERED,
            Color.OLIVEDRAB,
            Color.LAWNGREEN,
            Color.KHAKI
    );
    private PixelMapper pixelMapper;


    /**
     * Constructor for RaceRenderer, takes a Race, Group  and AnchorPane as parameters.
     *
     * @param pixelMapper for converting coordinates to pixel coordinates
     * @param race        the race containing the displayBoats to be drawn
     * @param group       the group to be drawn on
     */
    public RaceRenderer(PixelMapper pixelMapper, DisplayRace race, Group group) {
        this.race = race;
        this.group = group;
        this.pixelMapper = pixelMapper;
    }

    /**
     * Draws displayBoats in the Race on the Group as well as the visible annotations including the BoatSails in both
     * the luffing and powered up position
     */
    public void renderBoats() {
        for (int i = 0; i < race.getStartingList().size(); i++) {
                Boat boat = race.getStartingList().get(i);
                DisplayBoat displayBoat = displayBoats.get(boat.getShortName());

                if (displayBoat == null && !BoatStatus.DSQ.equals(boat.getStatus())) {
                    double boatPixelLength = boat.getLength() * pixelMapper.mappingRatio();

                    //Wake
                    displayBoat = new DisplayWake(pixelMapper,
                            new DisplayBoat(pixelMapper, boat.getShortName(), boat.getColour(), boatPixelLength));
                    //Highlight
                    if (boat.isControlled() && race.getMode() != RaceMode.CONTROLS_TUTORIAL) {
                        displayBoat = new BoatHighlight(pixelMapper, displayBoat);
                        displayBoat = new BoatGuide(pixelMapper, displayBoat);
                    }
                    displayBoat = new DisplaySail(pixelMapper, displayBoat);
                    displayBoat.addToGroup(group);
                    displayBoats.put(boat.getShortName(), displayBoat);
                }

                if (displayBoat != null && BoatStatus.DSQ.equals(boat.getStatus())) {
                    displayBoat.removeFrom(group);
                    displayBoats.remove(boat.getShortName());
                    DisplayTrail trail = trailMap.remove(boat.getShortName());
                    trail.removeFrom(group);

                } else if (displayBoat != null && boat.getCoordinate() != null) {
                    displayBoat.setCoordinate(boat.getCoordinate());
                    displayBoat.setSpeed(boat.getSpeed());
                    displayBoat.setHeading(boat.getHeading());
                    displayBoat.setEstimatedTime(boat.getTimeTilNextMark());
                    displayBoat.setTimeSinceLastMark(boat.getTimeSinceLastMark());
                    displayBoat.setScale(pixelMapper.mappingRatio());
                    displayBoat.setApparentWindDirection(race.getCourse().getWindDirection());
                    displayBoat.setSailOut(boat.isSailOut());
                    displayBoat.setBoatStatus(boat.getStatus());
                    displayBoat.setColour(boat.getColour());
                    if (boat.getLegNumber() < race.getCourse().getMarkSequence().size()) {
                        displayBoat.setDestination(race.getCourse().getMarkSequence().get(boat.getLegNumber()).getCompoundMark().getCoordinate());
                    } else {
                        displayBoat.setDestination(null);
                    }
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
            if (boatCoordinates != null && !(boat.getStatus().equals(BoatStatus.DSQ))) {
                drawTrail(boat, pixelMapper);
            }
        }
    }


    /**
     * Update a single boats trail.
     *
     * @param boat        the trail belongs to.
     * @param pixelMapper used to map a coordinate to a point on the screen.
     */
    private void drawTrail(Boat boat, PixelMapper pixelMapper) {
        if(boat.getStatus().equals(BoatStatus.DSQ)){
            group.getChildren().remove(trailMap.get(boat.getShortName()));
        }else {
            final double MAX_HEADING_DIFFERENCE = 0.5d; // smaller => smoother trail, higher => more fps
            DisplayTrail trail = trailMap.get(boat.getShortName());

            if (trail == null && !BoatStatus.DSQ.equals(boat.getStatus())) {
                final int MAX_TRAIL_LENGTH = 150;
                DisplayBoat displayBoat = displayBoats.get(boat.getShortName());
                trail = new DisplayTrail(displayBoat.getColor(), MAX_HEADING_DIFFERENCE, MAX_TRAIL_LENGTH);
                trailMap.put(boat.getShortName(), trail);
                trail.addToGroup(group);
            }
            if (trail != null) {
                trail.addPoint(boat.getCoordinate(), boat.getHeading(), pixelMapper);
            }
        }
    }


    /**
     * Redraw the the trails behind each boat by looking into the Map of circles and coordinates and
     * resetting the points.
     */
    public void reDrawTrails() {
        for (Boat boat : race.getStartingList()) {
            DisplayTrail trail = trailMap.get(boat.getShortName());
            if (trail != null) {
                trail.reDraw(pixelMapper);
            }
        }
    }

    public Group getGroup() {
        return group;
    }


    /**
     * Sets the annotation types that are visible.
     *
     * @param type      , AnnotationType, the type of annotiation.
     * @param isVisible , Boolean, if the type is visible.
     */
    public void setVisibleAnnotations(AnnotationType type, Boolean isVisible) {
        for (DisplayBoat boat : displayBoats.values()) {
            boat.setAnnotationVisible(type, isVisible);
        }
    }

    /**
     * @return a map from boat short names to colors.
     */
    public Map<String, Color> boatColors() {
        Map<String, Color> boatColors = new HashMap<>();
        for (Map.Entry<String, DisplayBoat> entry : displayBoats.entrySet()) {
            boatColors.put(entry.getKey(), entry.getValue().getColor());
        }

        return boatColors;
    }
}