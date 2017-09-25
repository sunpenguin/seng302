package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Mark;
import seng302.team18.util.GPSCalculator;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for information for arrow that rounds a mark.
 */
public class DisplayRoundingArrow {

    private CompoundMark current;
    private Polyline arrowHead;
    private Line arrowLine;
    private List<XYPair> endPoints = new ArrayList<>();
    private GPSCalculator calculator = new GPSCalculator();
    private PixelMapper pixelMapper;
    private double bearing;
    private Coordinate middle;


    public DisplayRoundingArrow(CompoundMark current, PixelMapper pixelMapper) {
        this.current = current;
        arrowHead = new Polyline();
        this.pixelMapper = pixelMapper;
        drawArrow();
    }


    /**
     * Create the rounding arrow.
     */
    private void drawArrow() {
        makeHeadShape();
        calculateEndPoints();
        makeLine();
        setPosition();
    }


    /**
     * Calculate the start and end points for the body of the arrow.
     */
    private void calculateEndPoints() {
        double turingAngle = 0;
        if (current.isGate()) {
            Mark m1 = current.getMarks().get(0);
            Mark m2 = current.getMarks().get(1);
            middle = calculator.midPoint(m1.getCoordinate(), m2.getCoordinate());
            turingAngle = 180;
        } else {
            middle = current.getMarks().get(0).getCoordinate();
            turingAngle = 270;
        }

        bearing = (calculator.getBearing(current.getMarks().get(0).getCoordinate(), middle) + 90) % 360;
        Coordinate start = calculator.toCoordinate(middle, (bearing + turingAngle) % 360, 20);
        Coordinate end = calculator.toCoordinate(start, (bearing + 180) % 360, 50);
        XYPair startPixel = pixelMapper.mapToPane(start);
        XYPair endPixel = pixelMapper.mapToPane(end);

        endPoints.add(startPixel);
        endPoints.add(endPixel);
    }


    /**
     * Create the size (shape) of the arrow head.
     */
    private void makeHeadShape() {
        double pixelLength = current.getMarks().get(0).getLength();

        Double[] headShape = new Double[]{
                0.0, -pixelLength * 0.45,
                -pixelLength * 0.45, pixelLength * 0.45,
                pixelLength * 0.45, pixelLength * 0.45,
                0.0, -pixelLength * 0.45
        };

        arrowHead.getPoints().clear();
        arrowHead.getPoints().addAll(headShape);
    }


    /**
     * Create the body (line) of the arrow.
     */
    private void makeLine() {
        arrowLine = new Line(
                endPoints.get(0).getX(), endPoints.get(0).getY(),
                endPoints.get(1).getX(), endPoints.get(1).getY());
        arrowLine.setFill(javafx.scene.paint.Color.WHITE);
        arrowLine.setStyle("-fx-stroke: green");
        arrowLine.setStrokeWidth(2);
    }


    /**
     * Add created arrow to the group.
     *
     * @param group arrow to be added on
     */
    public void addToGroup(Group group) {
        group.getChildren().add(arrowHead);
        group.getChildren().add(arrowLine);
    }


    /**
     * Remove existing arrow from the group.
     *
     * @param group arrow to be removed from
     */
    public void removeFromGroup(Group group) {
        group.getChildren().remove(arrowHead);
        group.getChildren().remove(arrowLine);
    }


    /**
     * Set the position of the arrow in the race.
     */
    private void setPosition() {
        arrowLine.setStartX(endPoints.get(0).getX());
        arrowLine.setStartY(endPoints.get(0).getY());
        arrowLine.setEndX(endPoints.get(1).getX());
        arrowLine.setEndY(endPoints.get(1).getY());

        arrowHead.setStyle("-fx-stroke: green");
        arrowHead.setFill(Color.GREEN);
        if (current.getName().equals("Start Line")) {
            arrowHead.setLayoutX(endPoints.get(1).getX());
            arrowHead.setLayoutY(endPoints.get(1).getY());
            arrowHead.setRotate((bearing + 180) % 360);
        } else {
            arrowHead.setLayoutX(endPoints.get(0).getX());
            arrowHead.setLayoutY(endPoints.get(0).getY());
            arrowHead.setRotate(bearing);
        }
    }
}
