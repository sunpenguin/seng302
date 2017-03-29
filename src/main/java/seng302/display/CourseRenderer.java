package seng302.display;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import seng302.model.CompoundMark;
import seng302.model.Coordinate;
import seng302.model.Course;
import seng302.model.Mark;
import seng302.util.PixelMapper;
import seng302.util.XYPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private HashMap<String, Rectangle> marks = new HashMap();
    private HashMap<String, Line> gates = new HashMap<>();
    private HashMap<String, CompoundMark> compoundMarkMap = new HashMap<>();
    private Course course;
    private Group group;
    private Pane pane;


    public CourseRenderer(Course course, Group group, Pane pane) {
        this.course = course;
        this.group = group;
        this.pane = pane;
        setupCourse();
    }

    /**
     * Set up the boundary around the course by getting the coordinates of the boundary points and drawing
     * a PolyLine using each point.
     */
    private void setupBoundary() {
        // Renders Boundaries
        for (Coordinate boundary : course.getBoundaries()) {
            XYPair boundaryPixels = PixelMapper.convertCoordPixel(boundary, PADDING, true, pane, course);
            border.getPoints().addAll(boundaryPixels.getX(), boundaryPixels.getY());
        }
        if (course.getBoundaries().size() != 0) {
            Coordinate boundary = course.getBoundaries().get(0);
            XYPair boundaryPixels = PixelMapper.convertCoordPixel(boundary, PADDING, true, pane, course);
            border.getPoints().addAll(boundaryPixels.getX(), boundaryPixels.getY());
        }
        group.getChildren().addAll(border);
    }


    /**
     * Set up a CompoundMark by creating a rectangle for each mark within the CompoundMark,
     * setting the necessary x, y coordinates and addinf them to the group.
     * @param compoundMark CompoundMark to set up.
     */
    private void setupMark(CompoundMark compoundMark) {
        compoundMarkMap.put(compoundMark.getName(), compoundMark);

        for (Mark mark : compoundMark.getMarks()) {
            Rectangle markImage = new Rectangle(MARK_SIZE, MARK_SIZE, MARK_COLOR);

            Coordinate coordinate = mark.getCoordinates();
            XYPair pixelCoordinates = PixelMapper.convertCoordPixel(coordinate, PADDING, true, pane, course);
            markImage.setX(pixelCoordinates.getX() - (MARK_SIZE / 2.0));
            markImage.setY(pixelCoordinates.getY() - (MARK_SIZE / 2.0));

            marks.put(mark.getName(), markImage);
            group.getChildren().add(markImage);

        }
    }


    /**
     * Set up a gate (Start or Finish only) by creating a rectangle for the endpoints and drawing a line between them
     * and adding the shapes to the group.
     * @param compoundMark CompoundMark to set up.
     */
    private void setupGate(CompoundMark compoundMark) {
        ArrayList<XYPair> endPoints = new ArrayList<>();

        for (int i = 0; i < compoundMark.getMarks().size(); i++) {
            Mark mark = compoundMark.getMarks().get(i);
            Rectangle rectangle = new Rectangle(MARK_SIZE, MARK_SIZE, MARK_COLOR);
            Coordinate coordinate = mark.getCoordinates();
            XYPair pixelCoordinates = PixelMapper.convertCoordPixel(coordinate, PADDING, true, pane, course);
            rectangle.setX(pixelCoordinates.getX() - (MARK_SIZE / 2));
            rectangle.setY(pixelCoordinates.getY() - (MARK_SIZE / 2));
            endPoints.add(pixelCoordinates);

            marks.put(mark.getName(), rectangle);
            group.getChildren().add(rectangle);
        }
        Line line = new Line(
                endPoints.get(0).getX(), endPoints.get(0).getY(),
                endPoints.get(1).getX(), endPoints.get(1).getY());
        line.setFill(Color.WHITE);
        line.setStyle("-fx-stroke: red");

        gates.put(compoundMark.getName(), line);
        group.getChildren().add(line);
    }


    /**
     * Set up each stage of the course.
     * Only set up a CompoundMark if it has not already been added to avoid duplicates.
     */
    private void setupCourse() {
        List<CompoundMark> compoundMarks = course.getCompoundMarks();

        for (int i = 0 ; i < compoundMarks.size(); i++) {
            CompoundMark compoundMark = compoundMarks.get(i);
            if (!compoundMarkMap.containsKey(compoundMark.getName())) {
                if ((i == 0 || i == compoundMarks.size() - 1) && compoundMark.getMarks().size() == CompoundMark.GATE_SIZE) {
                    setupGate(compoundMark);
                } else {
                    setupMark(compoundMark);
                }
            }
        }
        setupBoundary();
    }


    /**
     * Called if the course needs to be re-rendered due to the window being resized.
     */
    public void renderCourse() {
        List<CompoundMark> compoundMarks = course.getCompoundMarks();
        // Renders CompoundMarks
        for (int i = 0 ; i < compoundMarks.size(); i++) {
            CompoundMark compoundMark = compoundMarks.get(i);
            if ((i == 0 || i == compoundMarks.size() - 1) && compoundMark.getMarks().size() == CompoundMark.GATE_SIZE) {
                renderGate(compoundMark);
            } else {
                renderCompoundMark(compoundMark);
            }
        }
        // Renders Boundaries
        group.getChildren().remove(border);
        border = new Polyline();
        for (Coordinate boundary : course.getBoundaries()) {
            renderBoundary(border, boundary);
        }
        if (course.getBoundaries().size() != 0) {
            renderBoundary(border, course.getBoundaries().get(0));
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
        XYPair boundaryPixels = PixelMapper.convertCoordPixel(boundary, PADDING, false, pane, course);
        border.getPoints().addAll(boundaryPixels.getX(), boundaryPixels.getY());
    }


    /**
     * Reset a point for a mark due to resizing
     * @param mark Mark to reset
     */
    private void renderMark(Mark mark) {
        Rectangle rectangle = marks.get(mark.getName());
        Coordinate coordinate = mark.getCoordinates();
        XYPair pixelCoordinates = PixelMapper.convertCoordPixel(coordinate, PADDING, false, pane, course);
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

        ArrayList<XYPair> endPoints = new ArrayList<>();
        for (int i = 0; i < compoundMark.getMarks().size(); i++) {
            Mark mark = compoundMark.getMarks().get(i);

            Rectangle rectangle = marks.get(mark.getName());

            Coordinate coordinate = mark.getCoordinates();
            XYPair pixelCoordinates = PixelMapper.convertCoordPixel(coordinate, PADDING, false, pane, course);
            rectangle.setX(pixelCoordinates.getX() - (MARK_SIZE / 2));
            rectangle.setY(pixelCoordinates.getY() - (MARK_SIZE / 2));
            endPoints.add(pixelCoordinates);
        }

        Line line = gates.get(compoundMark.getName());

        line.setStartX(endPoints.get(0).getX());
        line.setStartY(endPoints.get(0).getY());
        line.setEndX(endPoints.get(1).getX());
        line.setEndY(endPoints.get(1).getY());
    }
}
