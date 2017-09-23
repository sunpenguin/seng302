package seng302.team18.visualiser.display.render;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import seng302.team18.message.PowerType;
import seng302.team18.model.*;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A class which renders the given course on the group so it can be displayed to the user.
 */
public class CourseRenderer implements Renderable {

    private final Color MARK_COLOR = Color.color(0.502, 0.4078, 0.4);
    private final Color BOUNDARY_FILL_COLOR = Color.ALICEBLUE;
    private Polyline border = new Polyline();
    private Map<Integer, Circle> marks = new HashMap<>();
    private Map<String, Line> gates = new HashMap<>();
    private Map<Integer, ImageView> pickUps = new HashMap<>();
    private Course course;
    private Group group;
    private PixelMapper pixelMapper;
    private RaceMode mode;

    private static final Image SPEED_POWER_UP = new Image("/images/race_view/Arrow2_no_back.gif");
    private static final Image SHARK_POWER_UP = new Image("/images/race_view/reefShark_no_back.gif");


    public CourseRenderer(PixelMapper pixelMapper, Course course, Group group, RaceMode mode) {
        this.course = course;
        this.group = group;
        this.pixelMapper = pixelMapper;
        this.mode = mode;
    }


    public void refresh() {
        render();
    }


    /**
     * Renders the course.
     */
    public void render() {
        List<CompoundMark> compoundMarks = course.getCompoundMarks();
        // Renders CompoundMarks
        for (CompoundMark compoundMark : compoundMarks) {
            if (compoundMark != null &&
                    compoundMark.isGate() && (
                    compoundMark.getId().equals(course.getStartLineId()) ||
                            compoundMark.getId().equals(course.getFinishLineId()))) { // draw a line between the gate if its a start or finish
                renderGate(compoundMark);
            } else {
                renderCompoundMark(compoundMark);
            }
        }

        if (!mode.equals(RaceMode.CONTROLS_TUTORIAL)) {
            renderBoundaries();
        }
        renderPickUps();
    }


    /**
     * Renders all of the course boundaries again due to resizing
     */
    private void renderBoundaries() {
        // Renders Boundaries
        group.getChildren().remove(border);
        border = new Polyline();

        for (Coordinate boundaryMark : course.getLimits()) {
            if (boundaryMark != null) {
                renderBoundary(border, boundaryMark);
            }
        }

        if (!course.getLimits().isEmpty() && !group.getChildren().contains(border)) {
            try {
                renderBoundary(border, course.getLimits().get(0));
                group.getChildren().add(border);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("course limit = " + course.getLimits());
                System.out.println("course limit size = " + course.getLimits().size());
                e.printStackTrace();
            }
        }

        border.setFill(BOUNDARY_FILL_COLOR);
        double BOUNDARY_OPACITY = 0.3;
        border.setOpacity(BOUNDARY_OPACITY);
        border.toBack();
    }


    /**
     * Reset a point on the border due to resizing
     *
     * @param border   Polyline for the border
     * @param boundary the point to reset
     */
    private void renderBoundary(Polyline border, Coordinate boundary) {
        XYPair boundaryPixels = pixelMapper.mapToPane(boundary);
        border.getPoints().addAll(boundaryPixels.getX(), boundaryPixels.getY());
    }


    /**
     * Reset a point for a mark due to resizing
     *
     * @param mark Mark to reset
     * @return the circle on screen
     */
    private Circle renderMark(Mark mark) {
        Circle circle = marks.get(mark.getId());
        if (circle == null) {
            circle = makeMark(mark);
        }

        circle.setRadius(mark.getLength() * pixelMapper.mappingRatio() / 2);
        Coordinate coordinate = mark.getCoordinate();
        XYPair pixelCoordinates = pixelMapper.mapToPane(coordinate);
        circle.setCenterX(pixelCoordinates.getX());
        circle.setCenterY(pixelCoordinates.getY());

        return circle;
    }


    private Circle makeMark(Mark mark) {
        Circle circle = new Circle(mark.getLength() * pixelMapper.mappingRatio() / 2, MARK_COLOR);

        circle.setOnMouseClicked((event) -> {
            pixelMapper.setZoomLevel(4);
            pixelMapper.setViewPortCenter(mark.getCoordinate());
        });

        marks.put(mark.getId(), circle);
        group.getChildren().addAll(circle);
        circle.toBack();

        return circle;
    }


    /**
     * Call each Mark in a CompoundMark to reset it's point due to resizing
     *
     * @param compoundMark CompoundMark to reset.
     */
    private void renderCompoundMark(CompoundMark compoundMark) {
        try {
            if (compoundMark.getMarks() != null) {
                for (int i = 0; i < compoundMark.getMarks().size(); i++) {
                    Mark mark = compoundMark.getMarks().get(i);
                    renderMark(mark);
                }
            }
        } catch (NullPointerException e) {
            // TODO david 14/09
        }
    }


    /**
     * Reset the points and line for the endpoints of a gate
     *
     * @param compoundMark CompundMark to reset (Start/Finish only)
     */
    private void renderGate(CompoundMark compoundMark) {
        List<XYPair> endPoints = new ArrayList<>();
        for (int i = 0; i < compoundMark.getMarks().size(); i++) {
            Mark mark = compoundMark.getMarks().get(i);
            Circle circle = renderMark(mark);
            XYPair pixelCoordinates = new XYPair(circle.getCenterX(), circle.getCenterY());
            endPoints.add(pixelCoordinates);
        }
        renderGateConnection(endPoints, compoundMark);
    }


    /**
     * Reset the line between the endpoints of a gate
     *
     * @param endPoints    List of XYPairs that are the end points of the gate
     * @param compoundMark CompundMark to reset (Start/Finish only)
     */
    private void renderGateConnection(List<XYPair> endPoints, CompoundMark compoundMark) {
        Line line = gates.get(compoundMark.getName());
        if (line == null) {
            line = new Line(
                    endPoints.get(0).getX(), endPoints.get(0).getY(),
                    endPoints.get(1).getX(), endPoints.get(1).getY());
            line.setFill(Color.WHITE);
            line.setStyle("-fx-stroke: red");
            gates.put(compoundMark.getName(), line);
            group.getChildren().addAll(line);
            line.toBack();
        }

        line.setStrokeWidth(geScaledLineWeight());
        line.setStartX(endPoints.get(0).getX());
        line.setStartY(endPoints.get(0).getY());
        line.setEndX(endPoints.get(1).getX());
        line.setEndY(endPoints.get(1).getY());
    }


    /**
     * Gets LINE_WEIGHT, scaling it to the current zoom.
     *
     * @return scaled line weight
     */
    private double geScaledLineWeight() {
        double LINE_WEIGHT = 1.0;
        return LINE_WEIGHT * pixelMapper.getZoomFactor();
    }


    /**
     * Renders all pickups in the course.
     */
    private void renderPickUps() {
        removePowers();
        for (PickUp pickUp : course.getPickUps()) {
            renderPickUp(pickUp);
        }
    }


    /**
     * Removes any pick ups taken by other players.
     */
    private void removePowers() {
        course.removeOldPickUps();
        List<Integer> pickUpIds = course
                .getPickUps()
                .stream()
                .map(PickUp::getId)
                .collect(Collectors.toList());
        List<Integer> removed = new ArrayList<>();
        for (Integer id : pickUps.keySet()) {
            if (!pickUpIds.contains(id)) {
                removed.add(id);
            }
        }
        for (Integer id : removed) {
            group.getChildren().remove(pickUps.get(id));
            pickUps.remove(id);
        }
    }


    /**
     * Creates a pick up if there isn't one and updates it if it exists.
     *
     * @param pickUp not null.
     */
    private void renderPickUp(PickUp pickUp) {
        switch (pickUp.getType()) {
            case SPEED:
                renderPickUp(pickUp, PowerType.SPEED);
                break;
            case SHARK:
                renderPickUp(pickUp, PowerType.SHARK);
                break;
            default:
                System.out.println("PowerUpInterpreter::makePowerUp has gone horribly wrong (ask Sunguin for help)");
        }
    }


    /**
     * Creates a pick up if there isn't one and updates it if it exists.
     * Scales the image to the correct size in pixels.
     *
     * @param pickUp not null.
     */
    private void renderPickUp(PickUp pickUp, PowerType type) {
        ImageView pickUpVisual = pickUps.get(pickUp.getId());
        double pickUpSize = pixelMapper.mappingRatio() * pickUp.getRadius();

        if (pickUpVisual == null) {

            switch (type) {
                case SPEED:
                    pickUpVisual = new ImageView(SPEED_POWER_UP);
                    break;
                case SHARK:
                    pickUpVisual = new ImageView(SHARK_POWER_UP);
                    break;
                default:
                    return;
            }

            pickUpVisual.setOnMouseClicked((event) -> {
                pixelMapper.setZoomLevel(4);
                pixelMapper.setViewPortCenter(pickUp.getLocation());
            });

            group.getChildren().addAll(pickUpVisual);
            pickUpVisual.toBack();
            pickUps.put(pickUp.getId(), pickUpVisual);
        }

        double powerImageSize = pickUp.getType().equals(PowerType.SPEED) ? SPEED_POWER_UP.getWidth() : SHARK_POWER_UP.getWidth();

        double imageRatio = powerImageSize / (pickUpSize * 2);  // Ratio for scaling image to correct size
        pickUpVisual.setScaleX(1 / imageRatio);
        pickUpVisual.setScaleY(1 / imageRatio);

        Coordinate coordinate = pickUp.getLocation();
        XYPair pixelCoordinates = pixelMapper.mapToPane(coordinate);
        pickUpVisual.setLayoutX(pixelCoordinates.getX() - (powerImageSize / 2));
        pickUpVisual.setLayoutY(pixelCoordinates.getY() - (powerImageSize / 2));
    }
}
