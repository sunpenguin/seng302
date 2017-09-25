package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import seng302.team18.model.AbstractBoat;
import seng302.team18.model.MarkRounding;
import seng302.team18.util.GPSCalculator;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class for information for arrow that rounds a mark.
 */
public class DisplayRoundingArrow {

    private Collection<Shape> shapes = new ArrayList<>();
    private PixelMapper pixelMapper;

    private final static GPSCalculator GPS_CALCULATOR = new GPSCalculator();
    private final static Color COLOR = Color.GREEN;
    private final static int PERIOD_MILLIS = 5000;
    private final static double MARK_ARROW_ARC_LENGTH = 120;


    public DisplayRoundingArrow(PixelMapper pixelMapper, MarkRounding markRounding) {
        this.pixelMapper = pixelMapper;

        if (markRounding.getCompoundMark().isGate()) {
            drawGateArrows(markRounding);
        } else {
            drawMarkArrow(markRounding);
        }
    }


    private void drawGateArrows(MarkRounding rounding) {
        double bearing1to2 = GPS_CALCULATOR.getBearing(
                rounding.getCompoundMark().getMarks().get(0).getCoordinate(),
                rounding.getCompoundMark().getMarks().get(1).getCoordinate()
        );
        double bearing2to1 = (bearing1to2 + 180) % 360;

        boolean is1Clockwise = rounding.getRoundingDirection().equals(MarkRounding.Direction.SP);
        drawArcedArrow(rounding.getCompoundMark().getMarks().get(0), bearing1to2, 180, is1Clockwise);
        drawArcedArrow(rounding.getCompoundMark().getMarks().get(1), bearing2to1, 180, !is1Clockwise);

        Line line = new Line();
        XYPair start = pixelMapper.mapToPane(rounding.getCompoundMark().getMarks().get(0).getCoordinate());
        line.setStartX(start.getX());
        line.setStartY(start.getY());
        XYPair end = pixelMapper.mapToPane(rounding.getCompoundMark().getMarks().get(1).getCoordinate());
        line.setEndX(end.getX());
        line.setEndY(end.getY());
        shapes.add(line);
    }


    private void drawMarkArrow(MarkRounding rounding) {
        boolean isClockwise = rounding.getRoundingDirection().equals(MarkRounding.Direction.STARBOARD);
        double offset = MARK_ARROW_ARC_LENGTH / (isClockwise ? 2 : -2);
        double startAngle = (rounding.getPassAngle() - offset + 540) % 360;
        drawArcedArrow(rounding.getCompoundMark().getMarks().get(0), startAngle, offset * 2, isClockwise);
    }


    /**
     * Create the rounding arrow.
     */
    private void drawArcedArrow(AbstractBoat mark, double startAngle, double maxLength, boolean isClockwise) {
        Arc tail = makeArcedTail(mark, startAngle, maxLength, isClockwise);
        Shape head = makeHeadShape(mark);

        shapes.add(tail);
        shapes.add(head);

        head.setLayoutX(calculateHeadX(tail));
        head.setLayoutY(calculateHeadY(tail));
        head.getTransforms().add(new Rotate(getHeadingAtFinish(tail), 0, 0));
    }


    private Arc makeArcedTail(AbstractBoat mark, double startAngle, double maxLength, boolean isClockwise) {
        XYPair center = pixelMapper.mapToPane(mark.getCoordinate());
        final double radius = mark.getLength() * 2 * pixelMapper.mappingRatio();
        maxLength *= (isClockwise ? -1 : 1);

        Arc tail = new Arc(center.getX(), center.getY(), radius, radius, startAngle, getLength(maxLength));
        tail.setType(ArcType.OPEN);
        tail.setStroke(COLOR);
        tail.setStrokeWidth(2);
        tail.setFill(Color.TRANSPARENT);
        return tail;
    }


    private double getLength(double maxLength) {
        return (System.currentTimeMillis() % PERIOD_MILLIS) * maxLength / PERIOD_MILLIS;
    }


    /**
     * Create the size (shape) of the arrow head.
     */
    private Shape makeHeadShape(AbstractBoat mark) {
        double pixelLength = mark.getLength() * pixelMapper.mappingRatio() * 0.2;

        Double[] headShape = new Double[]{
                0.0, 0.0,
                pixelLength, 0.0,
                0.0, -pixelLength * 3,
                -pixelLength, 0.0
        };

        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(headShape);
        polygon.setFill(COLOR);
        polygon.setStrokeWidth(0);
        return polygon;
    }


    /**
     * Add created arrow to the group.
     *
     * @param group arrow to be added on
     */
    public void addToGroup(Group group) {
        shapes.forEach(shape -> group.getChildren().add(shape));
    }


    /**
     * Remove existing arrow from the group.
     *
     * @param group arrow to be removed from
     */
    public void removeFromGroup(Group group) {
        shapes.forEach(shape -> group.getChildren().remove(shape));
    }


    private double calculateHeadX(Arc tail) {
        return tail.getCenterX() + tail.getRadiusX() * Math.cos(Math.toRadians(tail.getStartAngle() + tail.getLength()));
    }


    private double calculateHeadY(Arc tail) {
        return tail.getCenterY() - tail.getRadiusY() * Math.sin(Math.toRadians(tail.getStartAngle() + tail.getLength()));
    }


    private double getHeadingAtFinish(Arc tail) {
        double arcDegrees = tail.getStartAngle() + tail.getLength();
        double screenAngle = (720 - arcDegrees) % 360;
        if (tail.getLength() < 0) {
            screenAngle = (screenAngle + 180) % 360;
        }
        return screenAngle;
    }
}
