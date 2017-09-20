package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import seng302.team18.model.*;
import seng302.team18.visualiser.ClientRace;
import seng302.team18.visualiser.sound.SoundEffect;
import seng302.team18.visualiser.sound.SoundEffectPlayer;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that takes a Race and a Group and draws the Race on the Group.
 */
public class RaceRenderer implements Renderable {

    private Group group;
    private ClientRace race;
    private Map<String, DisplayBoat> displayBoats = new HashMap<>();
    private Map<String, DisplayTrail> trailMap = new HashMap<>();
    private Map<Integer, DisplayShark> sharksMap = new HashMap<>();
    private PixelMapper pixelMapper;
    private boolean drawTrails;
    private final SoundEffectPlayer soundPlayer;


    /**
     * Constructor for RaceRenderer, takes a Race, Group  and AnchorPane as parameters.
     *
     * @param pixelMapper for converting coordinates to pixel coordinates
     * @param race        the race containing the displayBoats to be drawn
     * @param group       the group to be drawn on
     * @param soundPlayer the manager for audio playback from this scene
     */
    public RaceRenderer(PixelMapper pixelMapper, ClientRace race, Group group, SoundEffectPlayer soundPlayer) {
        this.race = race;
        this.group = group;
        this.pixelMapper = pixelMapper;
        this.soundPlayer = soundPlayer;
        if (race.getMode() == RaceMode.CHALLENGE_MODE) {
            setChallengeModeCourse();
        }
    }


    /**
     * If the current race mode is challenge mode, the zoom level will be set to min zoom level.
     * <p>
     * The player's boat will be tracked.
     */
    private void setChallengeModeCourse() {
        Boat boat = race.getBoat(race.getPlayerId());
        if (boat.getId() == race.getPlayerId()) {
            pixelMapper.setZoomLevel(6);
            pixelMapper.setMinZoom(6);
            pixelMapper.track(boat);
            pixelMapper.setTracking(true);
        }
    }


    /**
     * Draws displayBoats in the Race on the Group as well as the visible annotations including the BoatSails in both
     * the luffing and powered up position
     */
    public void render() {
        drawBoats();
        renderShark();
        if (drawTrails) {
            drawTrails();
        }
    }


    private void drawBoats() {
        for (int i = 0; i < race.getStartingList().size(); i++) {
            Boat boat = race.getStartingList().get(i);
            DisplayBoat displayBoat = displayBoats.get(boat.getShortName());
            if (displayBoat == null && !BoatStatus.DSQ.equals(boat.getStatus())) {
                displayBoat = makeBoat(boat);
            }

            if (displayBoat != null && BoatStatus.DSQ.equals(boat.getStatus())) {
                remove(displayBoat);
            } else if (displayBoat != null && boat.getCoordinate() != null) {
                synchronise(displayBoat, boat);
            }
        }
    }


    /**
     * Checks if the projectile represented by each DisplayShark is still in bounds and removes the display shark from
     * the group if it is not
     */
    public void removeSharks() {
        for (DisplayShark shark : sharksMap.values()) {
            if (!race.getProjectiles().contains(shark.getProjectile())) {
                shark.removeFrom(group);
                sharksMap.remove(shark.getProjectile().getId());
            }
        }
    }


    /**
     * Renders the sharks on the group
     */
    public void renderShark() {
        removeSharks();

        for (int i = 0; i < race.getProjectiles().size(); i++) {
            Projectile projectile = race.getProjectiles().get(i);
            DisplayShark shark = sharksMap.get(projectile.getId());
            if (shark == null) {
                shark = new DisplayShark(projectile, pixelMapper);
                sharksMap.put(projectile.getId(), shark);
                shark.addToGroup(group);

            } else if (shark.getProjectile() != null) {
                shark.setScale(pixelMapper.mappingRatio());
                shark.setCoordinate(projectile.getLocation());
                shark.setHeading(projectile.getHeading());
            } else {
                shark.removeFrom(group);
                sharksMap.remove(shark.getProjectile().getId());
            }

        }
    }


    /**
     * Creates a new display boat corresponding to the given boat.
     *
     * @param boat to mirror
     * @return visual representation of the given boat.
     */
    private DisplayBoat makeBoat(Boat boat) {
        //Wake
        DisplayBoat displayBoat = new DisplayWake(pixelMapper, new DisplayBoat(pixelMapper, boat.getShortName(), boat.getColour(), boat.getLength()));
        //Highlight
        if (boat.isControlled()) {
            if (race.getMode() != RaceMode.CONTROLS_TUTORIAL) {
                displayBoat = new BoatHighlight(pixelMapper, displayBoat);
                if (race.getMode() != RaceMode.BUMPER_BOATS) {
                    displayBoat = new BoatGuide(pixelMapper, displayBoat);
                }
            }
        }
        displayBoat = new DisplaySail(pixelMapper, displayBoat);
        displayBoat = new DisplayCollision(pixelMapper, displayBoat, () -> soundPlayer.playEffect(SoundEffect.COLLISION));
        displayBoat.addToGroup(group);
        displayBoats.put(boat.getShortName(), displayBoat);
        return displayBoat;
    }


    /**
     * Removes a display boat from the screen.
     *
     * @param displayBoat to remove
     */
    private void remove(DisplayBoat displayBoat) {
        displayBoat.removeFrom(group);
        displayBoats.remove(displayBoat.getShortName());
        DisplayTrail trail = trailMap.remove(displayBoat.getShortName());
        trail.removeFrom(group);
    }


    /**
     * Makes a given display boat consistent with the boat
     *
     * @param displayBoat to update.
     * @param boat        containing updated information.
     */
    private void synchronise(DisplayBoat displayBoat, Boat boat) {
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
        if (boat.getLegNumber() < race.numSequences()) {
            displayBoat.setDestination(race.getDestination(boat.getLegNumber()));
        } else {
            displayBoat.setDestination(null);
        }
        if (boat.getHasCollided()) {
            displayBoat.setHasCollided(true);
            boat.setHasCollided(false);
        }
    }


    /**
     * Draws all the boats trails.
     */
    private void drawTrails() {
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
        if (boat.getStatus().equals(BoatStatus.DSQ)) {
            trailMap.get(boat.getShortName()).removeFrom(group);
        } else {
            final double MAX_HEADING_DIFFERENCE = 0.5d; // smaller => smoother trail, higher => more fps
            DisplayTrail trail = trailMap.get(boat.getShortName());

            if (trail == null && !BoatStatus.DSQ.equals(boat.getStatus())) {
                final int MAX_TRAIL_LENGTH = 750;
                DisplayBoat displayBoat = displayBoats.get(boat.getShortName());
                trail = new DisplayTrail(displayBoat.getColor(), MAX_TRAIL_LENGTH);
                trailMap.put(boat.getShortName(), trail);
                trail.addToGroup(group);
            }

            if (trail != null && boat.getSpeed() > 0.001) {
                trail.addPoint(boat.getCoordinate(), pixelMapper);
            }
        }
    }


    /**
     * Redraw the the trails behind each boat by looking into the Map of circles and coordinates and
     * resetting the points.
     */
    public void refresh() {
        if (drawTrails) {
            for (Boat boat : race.getStartingList()) {
                DisplayTrail trail = trailMap.get(boat.getShortName());
                if (trail != null) {
                    trail.reDraw(pixelMapper);
                }
            }
        }
    }


    public Group getGroup() {
        return group;
    }


    /**
     * Sets the visibility of an annotation type
     *
     * @param type      the type of annotation
     * @param isVisible whether the type is to be visible
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


    public void clearCollisions() {
        displayBoats.values().forEach(displayBoat -> displayBoat.setHasCollided(false));
    }


    public void setDrawTrails(boolean drawTrails) {
        this.drawTrails = drawTrails;
    }
}