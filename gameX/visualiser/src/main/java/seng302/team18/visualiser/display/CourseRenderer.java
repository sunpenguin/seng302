package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
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
public class CourseRenderer {

    private final Color MARK_COLOR = Color.GREY;
    private final Color BOUNDARY_FILL_COLOR = Color.ALICEBLUE;
    private double markSize;
    private Polyline border = new Polyline();
    private Map<Integer, Circle> marks = new HashMap<>();
    private Map<String, Line> gates = new HashMap<>();
    private Map<Integer, Circle> pickUps = new HashMap<>();
    private Course course;
    private Group group;
    private PixelMapper pixelMapper;
    private RaceMode mode;


    public CourseRenderer(PixelMapper pixelMapper, Course course, Group group, Pane pane, RaceMode mode) {
        this.course = course;
        this.group = group;
        this.pixelMapper = pixelMapper;
        this.mode = mode;
    }


    /**
     * Called if the course needs to be re-rendered due to the window being resized.
     */
    public void renderCourse() {
        if (mode != RaceMode.CONTROLS_TUTORIAL) {
            double MARK_SIZE = 10;
            markSize = MARK_SIZE * pixelMapper.mappingRatio();
            List<CompoundMark> compoundMarks = course.getCompoundMarks();
            // Renders CompoundMarks
            for (int i = 0; i < compoundMarks.size(); i++) {
                CompoundMark compoundMark = compoundMarks.get(i);
                if ((course.getMarkSequence().get(0).getCompoundMark().getId().equals(compoundMark.getId())
                        || course.getMarkSequence().get(course.getMarkSequence().size() - 1).getCompoundMark().getId().equals(compoundMark.getId()))
                        && compoundMark.getMarks().size() == CompoundMark.GATE_SIZE) { // draw a line between the gate if its a start or finish
                    renderGate(compoundMark);
                } else {
                    renderCompoundMark(compoundMark);
                }
            }
            renderBoundaries();
            renderPickUps();
        }
    }

    /**
     * Renders all of the course boundaries again due to resizing
     */
    private void renderBoundaries() {
        // Renders Boundaries
        group.getChildren().remove(border);
        border = new Polyline();

        for (Coordinate boundaryMark : course.getCourseLimits()) {
            renderBoundary(border, boundaryMark);
        }

        if (course.getCourseLimits().size() > 0 && !group.getChildren().contains(border)) {
            renderBoundary(border, course.getCourseLimits().get(0));
            group.getChildren().add(border);
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
//        double scaledMarSize = getScaledMarkSize();
        Circle circle = marks.get(mark.getId());
        if (circle == null) {
            circle = makeMark(mark);
        }

        circle.setRadius(markSize);
        Coordinate coordinate = mark.getCoordinate();
        XYPair pixelCoordinates = pixelMapper.mapToPane(coordinate);
        circle.setCenterX(pixelCoordinates.getX());
        circle.setCenterY(pixelCoordinates.getY());

        return circle;
    }

    private Circle makeMark(Mark mark) {
        Circle circle = new Circle(markSize, MARK_COLOR);

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
        for (int i = 0; i < compoundMark.getMarks().size(); i++) {
            Mark mark = compoundMark.getMarks().get(i);
            renderMark(mark);
        }
    }


    /**
     * Reset the points and line for the endpoints of a gate
     *
     * @param compoundMark CompundMark to reset (Start/Finish only)
     */
    private void renderGate(CompoundMark compoundMark) {
//        double scaledMarkSize = getScaledMarkSize();

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

    public Group getGroup() {
        return group;
    }

    /**
     * Gets MARK_SIZE, scaling it to the current zoom.
     *
     * @return scaled mark size
     */
    private double getScaledMarkSize() {
        return markSize * pixelMapper.getZoomFactor();
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


    private void renderPickUps() {
        removeTakenPowers();
        for (PickUp pickUp : course.getPickUps()) {
            renderPickUp(pickUp);
        }
    }


    private void removeTakenPowers() {
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


    private void renderPickUp(PickUp pickUp) {
        switch (pickUp.getType()) {
            case SPEED:
                renderSpeedPickUp(pickUp);
                break;
            default:
                System.out.println("PowerUpInterpreter::makePowerUp has gone horribly wrong (ask Sunguin for help)");
                return;
        }
    }


    private void renderSpeedPickUp(PickUp pickUp) {
        Circle pickUpVisual = pickUps.get(pickUp.getId());
        if (pickUpVisual == null) {
            pickUpVisual = new Circle(markSize, Color.GREEN);
            pickUpVisual.setOnMouseClicked((event) -> {
                pixelMapper.setZoomLevel(4);
                pixelMapper.setViewPortCenter(pickUp.getLocation());
            });
            group.getChildren().addAll(pickUpVisual);
//            pickUpVisual.toBack();
            pickUps.put(pickUp.getId(), pickUpVisual);
        }
        pickUpVisual.setRadius(markSize); // change to radius specified
        Coordinate coordinate = pickUp.getLocation();
        XYPair pixelCoordinates = pixelMapper.mapToPane(coordinate);
        pickUpVisual.setCenterX(pixelCoordinates.getX());
        pickUpVisual.setCenterY(pixelCoordinates.getY());
    }




}
