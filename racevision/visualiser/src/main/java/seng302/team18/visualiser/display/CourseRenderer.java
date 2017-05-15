package seng302.team18.visualiser.display;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import seng302.team18.model.*;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.*;

/**
 * Created by dhl25 on 30/03/17.
 */
public class CourseRenderer {

    private final Color MARK_COLOR = Color.BLACK;
    private final Color BOUNDARY_FILL_COLOR = Color.ALICEBLUE;
    private final double BOUNDARY_OPACITY = 0.3;
    private final double MARK_SIZE = 10.0;
    private final double PADDING = 20.0;
    private Polyline border = new Polyline();
    private Map<Integer, Rectangle> marks = new HashMap<>();
    private Map<String, Line> gates = new HashMap<>();
    private Course course;
    private Group group;
    private Pane pane;
    private PixelMapper pixelMapper;


    public CourseRenderer(PixelMapper pixelMapper, Course course, Group group, Pane pane) {
        this.course = course;
        this.group = group;
        this.pane = pane;
        this.pixelMapper = pixelMapper;
    }


    /**
     * Called if the course needs to be re-rendered due to the window being resized.
     */
    public void renderCourse() {
//        System.out.println(group.getChildren().size());
//        System.out.println(course.getCompoundMarks());
        List<CompoundMark> compoundMarks = course.getCompoundMarks();
        // Renders CompoundMarks
        for (int i = 0 ; i < compoundMarks.size(); i++) {
            CompoundMark compoundMark = compoundMarks.get(i);
            if ((course.getMarkRoundings().get(0).getCompoundMark().getId().equals(compoundMark.getId())
                    || course.getMarkRoundings().get(course.getMarkRoundings().size() - 1).getCompoundMark().getId().equals(compoundMark.getId()))
                    && compoundMark.getMarks().size() == CompoundMark.GATE_SIZE) { // draw a line between the gate if its a start or finish
                renderGate(compoundMark);
            } else {
                renderCompoundMark(compoundMark);
            }
        }
        renderBoundaries();
    }

    private void renderBoundaries() {
        // Renders Boundaries
        group.getChildren().remove(border);
        border = new Polyline();
        List<BoundaryMark> boundaryMarks = course.getBoundaries();
        boundaryMarks.sort(Comparator.comparing(BoundaryMark::getSequenceID));
        for (BoundaryMark boundary : boundaryMarks) {
            renderBoundary(border, boundary.getCoordinate());
        }
        if (course.getBoundaries().size() != 0) {
            renderBoundary(border, course.getBoundaries().get(0).getCoordinate());
            group.getChildren().add(border);
        }
        border.setFill(BOUNDARY_FILL_COLOR);
        border.setOpacity(BOUNDARY_OPACITY);
        border.toBack();
    }


    /**
     * Reset a point on the border due to resizing
     * @param border Polyline for the border
     * @param boundary the point to reset
     */
    private void renderBoundary(Polyline border, Coordinate boundary) {
        XYPair boundaryPixels = pixelMapper.coordToPixel(boundary);
        border.getPoints().addAll(boundaryPixels.getX(), boundaryPixels.getY());
    }


    /**
     * Reset a point for a mark due to resizing
     * @param mark Mark to reset
     */
    private void renderMark(Mark mark) {
        Rectangle rectangle = marks.get(mark.getId());
        if (rectangle == null) {
            rectangle = new Rectangle(MARK_SIZE, MARK_SIZE, MARK_COLOR);

            rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    course.setViewCenter(mark.getCoordinate());
                    course.setZoomLevel(4);
                    course.setZoomId(mark.getId());
                }
            });

            marks.put(mark.getId(), rectangle);
            group.getChildren().addAll(rectangle);
        }
        Coordinate coordinate = mark.getCoordinate();
        XYPair pixelCoordinates = pixelMapper.coordToPixel(coordinate);
        rectangle.setX(pixelCoordinates.getX() - (MARK_SIZE / 2.0));
        rectangle.setY(pixelCoordinates.getY() - (MARK_SIZE / 2.0));
    }


    /**
     * Call each Mark in a CompoundMark to reset it's point due to resizing
     * @param compoundMark CompoundMark to reset.
     */
    private void renderCompoundMark(CompoundMark compoundMark) {
        for (int i = 0; i < compoundMark.getMarks().size(); i++) {
            Mark mark = compoundMark.getMarks().get(i);
            renderMark(mark);
        }
    }


    /**
     * Reset the points for the endpoints of a gate as well as the line between them due to resizing
     * @param compoundMark CompundMark to reset (Start/Finish only)
     */
    private void renderGate(CompoundMark compoundMark) {
        List<XYPair> endPoints = new ArrayList<>();
        for (int i = 0; i < compoundMark.getMarks().size(); i++) {
            Mark mark = compoundMark.getMarks().get(i);
            Rectangle rectangle = marks.get(mark.getId());
            if (rectangle == null) {
                rectangle = new Rectangle(MARK_SIZE, MARK_SIZE, MARK_COLOR);

                rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        course.setZoomLevel(4);
                        course.setViewCenter(mark.getCoordinate());
                        course.setZoomId(mark.getId());
                    }
                });

                marks.put(mark.getId(), rectangle);
                group.getChildren().addAll(rectangle);
            }

            Coordinate coordinate = mark.getCoordinate();
            XYPair pixelCoordinates = pixelMapper.coordToPixel(coordinate);
            rectangle.setX(pixelCoordinates.getX() - (MARK_SIZE / 2));
            rectangle.setY(pixelCoordinates.getY() - (MARK_SIZE / 2));
            endPoints.add(pixelCoordinates);
        }

        Line line = gates.get(compoundMark.getName());
        if (line == null) {
            line = new Line(
                    endPoints.get(0).getX(), endPoints.get(0).getY(),
                    endPoints.get(1).getX(), endPoints.get(1).getY());
            line.setFill(Color.WHITE);
            line.setStyle("-fx-stroke: red");
            gates.put(compoundMark.getName(), line);
            group.getChildren().addAll(line);
        }

        line.setStartX(endPoints.get(0).getX());
        line.setStartY(endPoints.get(0).getY());
        line.setEndX(endPoints.get(1).getX());
        line.setEndY(endPoints.get(1).getY());
    }

    public Group getGroup() {
        return group;
    }
}
