package seng302.team18.visualiser.display.object;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import seng302.team18.model.MarkRounding;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Class for information for arrow that rounds a mark.
 */
public class DisplayRoundingArrow {

    private static final Color COLOR = Color.BLACK;
    private static final int PERIOD_MILLIS = 2500;
    private static final double MARK_ARROW_ARC_LENGTH = 120;
    private static final double TAIL_WIDTH = 1.5;
    private static final double ARROW_SCALING_FACTOR = 0.80;
    private static final double STRAIGHT_ARROW_LENGTH = 2; // In boat lengths
    private static final double STRAIGHT_ARROW_RADIUS = 1.0; // In boat lengths
    private static final double CURVED_ARROW_RADIUS = 1.5; // In boat lengths
    private static final Collection<MarkRounding.GateType> straightArrowedGates = Arrays.asList(
            MarkRounding.GateType.THROUGH_GATE,
            MarkRounding.GateType.ROUND_BOTH_MARKS
    );

    private Collection<Shape> shapes = new ArrayList<>();
    private PixelMapper pixelMapper;


    /**
     * Creates a mark rounding arrow
     *
     * @param pixelMapper  the mapper to use to map geographic coordinates to the screen
     * @param markRounding the mark rounding to create arrows for
     */
    public DisplayRoundingArrow(PixelMapper pixelMapper, MarkRounding markRounding) {
        this.pixelMapper = pixelMapper;

        if (markRounding.getCompoundMark().isGate()) {
            if (straightArrowedGates.contains(markRounding.getGateType())) {
                drawStraightGateArrows(markRounding, !markRounding.getGateType().isThroughFirst());
            } else {
                drawRoundGateArrows(markRounding, !markRounding.getGateType().isThroughFirst());
            }
        } else {
            drawMarkArrow(markRounding);
        }
    }


    /**
     * Draw semi-circular arrows around each mark in the gate
     *
     * @param rounding      a gate rounding
     * @param isDirReversed true if the gate entry direction is reversed
     */
    private void drawRoundGateArrows(MarkRounding rounding, boolean isDirReversed) {
        XYPair mark1 = pixelMapper.mapToPane(rounding.getCompoundMark().getMarks().get(0).getCoordinate());
        XYPair mark2 = pixelMapper.mapToPane(rounding.getCompoundMark().getMarks().get(1).getCoordinate());

        double bearing1to2 = Math.atan2(mark2.getX() - mark1.getX(), mark2.getY() - mark1.getY()) - Math.PI / 2;
        if (bearing1to2 < 0) bearing1to2 += Math.PI * 2;
        bearing1to2 = Math.toDegrees(bearing1to2);
        if (isDirReversed) bearing1to2 += 180;
        double bearing2to1 = (bearing1to2 + 180) % 360;
        boolean is1Clockwise = rounding.getRoundingDirection().equals(MarkRounding.Direction.SP);

        double markSize = rounding.getCompoundMark().getMarks().get(0).getLength();

        drawArcedArrow(mark1, markSize, bearing1to2, 180, is1Clockwise);
        drawArcedArrow(mark2, markSize, bearing2to1, 180, !is1Clockwise);
    }


    /**
     * Draw two straight arrows between the gate
     *
     * @param rounding      a gate rounding
     * @param isDirReversed true if the gate entry direction is reversed
     */
    private void drawStraightGateArrows(MarkRounding rounding, boolean isDirReversed) {
        XYPair mark1 = pixelMapper.mapToPane(rounding.getCompoundMark().getMarks().get(0).getCoordinate());
        XYPair mark2 = pixelMapper.mapToPane(rounding.getCompoundMark().getMarks().get(1).getCoordinate());

        double bearing1to2 = Math.atan2(mark2.getX() - mark1.getX(), mark2.getY() - mark1.getY()) - Math.PI / 2;
        if (bearing1to2 < 0) bearing1to2 += Math.PI * 2;
        bearing1to2 = Math.toDegrees(bearing1to2);
        if (isDirReversed) bearing1to2 += 180;
        boolean is1Clockwise = rounding.getRoundingDirection().equals(MarkRounding.Direction.SP);
        double arrowHeading = (bearing1to2 + (is1Clockwise ? 360 - 90 : 90)) % 360;

        double markSize = rounding.getCompoundMark().getMarks().get(0).getLength();
        double offset = STRAIGHT_ARROW_RADIUS * markSize * pixelMapper.mappingRatio();
        XYPair endPos;

        endPos = polarToCartesian(mark1, offset, bearing1to2);
        endPos = polarToCartesian(endPos, scaleValue(0, markSize * STRAIGHT_ARROW_LENGTH) / -2, arrowHeading);
        drawStraightArrow(endPos, markSize, arrowHeading);

        endPos = polarToCartesian(mark2, -offset, bearing1to2);
        endPos = polarToCartesian(endPos, scaleValue(0, markSize * STRAIGHT_ARROW_LENGTH) / -2, arrowHeading);
        drawStraightArrow(endPos, markSize, arrowHeading);

    }


    /**
     * Draw a curved guide arrow for a mark
     *
     * @param rounding a mark rounding
     */
    private void drawMarkArrow(MarkRounding rounding) {
        boolean isClockwise = rounding.getRoundingDirection().equals(MarkRounding.Direction.STARBOARD);
        double offset = MARK_ARROW_ARC_LENGTH / (isClockwise ? 2 : -2);
        double startAngle = (rounding.getPassAngle() - offset + 540) % 360;
        XYPair center = pixelMapper.mapToPane(rounding.getCompoundMark().getMarks().get(0).getCoordinate());
        double markSize = rounding.getCompoundMark().getMarks().get(0).getLength();
        drawArcedArrow(center, markSize, startAngle, offset * 2, isClockwise);
    }


    /**
     * Draw a single arrow, that follows a circular path around a point
     *
     * @param center      the location of the origin of the circle on screen
     * @param size        the size of the arrow
     * @param startAngle  the angle to start the arrow at
     * @param maxLength   the length of the arrow in degrees
     * @param isClockwise true if the mark rounding direction is clockwise around mark1, else false
     */
    private void drawArcedArrow(XYPair center, double size, double startAngle, double maxLength, boolean isClockwise) {
        Arc tail = makeArcedTail(center, size * CURVED_ARROW_RADIUS, startAngle, maxLength, isClockwise);
        Shape head = makeHeadShape(size);

        shapes.add(tail);
        shapes.add(head);

        head.setLayoutX(calculateHeadX(tail));
        head.setLayoutY(calculateHeadY(tail));
        head.getTransforms().add(new Rotate(getHeadingAtFinish(tail), 0, 0));
    }


    /**
     * Draw a straight arrow
     *
     * @param endPos   the point at the tail of the arrow
     * @param markSize the size to use when setting the size of the arrow head
     * @param heading  the direction the arrow is pointing
     */
    private void drawStraightArrow(XYPair endPos, double markSize, double heading) {
        double length = scaleValue(0, getLength(markSize * STRAIGHT_ARROW_LENGTH));
        XYPair headPos = polarToCartesian(endPos, length, heading);

        Line tail = new Line(endPos.getX(), endPos.getY(), headPos.getX(), headPos.getY());
        tail.setStroke(COLOR);
        tail.setStrokeWidth(scaleValue(2, TAIL_WIDTH));

        Shape head = makeHeadShape(markSize);
        head.setLayoutX(headPos.getX());
        head.setLayoutY(headPos.getY());
        double headHeading = 90 - heading;
        head.getTransforms().add(new Rotate(headHeading, 0, 0));

        shapes.add(tail);
        shapes.add(head);
    }


    /**
     * Draw the tail of an arrow, that follows a circular path around a point
     *
     * @param center      the location of the origin of the circle on screen
     * @param radius      the radius to draw the arrow at
     * @param startAngle  the angle to start the arrow at
     * @param maxLength   the length of the arrow in degrees
     * @param isClockwise true if the mark rounding direction is clockwise around mark1, else false
     */
    private Arc makeArcedTail(XYPair center, double radius, double startAngle, double maxLength, boolean isClockwise) {
        radius *= pixelMapper.mappingRatio();
        maxLength *= (isClockwise ? -1 : 1);

        Arc tail = new Arc(center.getX(), center.getY(), radius, radius, startAngle, getLength(maxLength));
        tail.setType(ArcType.OPEN);
        tail.setStroke(COLOR);
        tail.setStrokeWidth(scaleValue(2, TAIL_WIDTH));
        tail.setFill(Color.TRANSPARENT);
        return tail;
    }


    /**
     * Returns a length between 0 and {@code maxLength}/. Changes over time.
     *
     * @param maxLength the maximum length
     * @return the length
     */
    private double getLength(double maxLength) {
        return (System.currentTimeMillis() % PERIOD_MILLIS) * maxLength / PERIOD_MILLIS;
    }


    /**
     * Scales a value based on the zoom level
     *
     * @param min   the minimum value to return
     * @param value the value to scale
     * @return the scaled value, or {@code min} if the scaled value is less than {@code min}
     */
    private double scaleValue(double min, double value) {
        return Math.max(min, value * Math.pow(pixelMapper.mappingRatio(), ARROW_SCALING_FACTOR));
    }


    /**
     * Create the size (shape) of the arrow head.
     */
    private Shape makeHeadShape(double size) {
        double pixelLength = scaleValue(scaleValue(2, TAIL_WIDTH), size * 0.15);

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


    /**
     * Sends the existing arrow to the back of the group.
     */
    public void sendToBack() {
        shapes.forEach(Node::toBack);
    }


    /**
     * Calculate the x-position of the arrow head on screen
     *
     * @param tail a circular arc arrow tail
     * @return the x position of the head of the arc in pixels
     */
    private double calculateHeadX(Arc tail) {
        return polarX(tail.getCenterX(), tail.getRadiusX(), Math.toRadians(tail.getStartAngle() + tail.getLength()));
    }


    /**
     * Calculate the y-position of the arrow head on screen
     *
     * @param tail a circular arc arrow tail
     * @return the y position of the head of the arc in pixels
     */
    private double calculateHeadY(Arc tail) {
        return polarY(tail.getCenterY(), tail.getRadiusY(), Math.toRadians(tail.getStartAngle() + tail.getLength()));
    }


    /**
     * Get the tangent of the arc at the head end
     *
     * @param tail a circular arc arrow tail
     * @return the tangent in degrees
     */
    private double getHeadingAtFinish(Arc tail) {
        double arcDegrees = tail.getStartAngle() + tail.getLength();
        double screenAngle = (720 - arcDegrees) % 360;
        if (tail.getLength() < 0) {
            screenAngle = (screenAngle + 180) % 360;
        }
        return screenAngle;
    }


    /**
     * Finds the cartesian coodinate that is {@code radius} units from {@code start}, at a bearing of {@code angleDeg}
     *
     * @param start    the start position
     * @param radius   the distance
     * @param angleDeg the heading in degrees
     * @return the new point
     */
    private XYPair polarToCartesian(XYPair start, double radius, double angleDeg) {
        double angleRad = Math.toRadians(angleDeg);
        return new XYPair(polarX(start.getX(), radius, angleRad), polarY(start.getY(), radius, angleRad));
    }


    /**
     * Finds the x value of the point at an angle of {@code angleRad} and distance of {@code radius} from {@code x}
     *
     * @param x        the x-value
     * @param radius   the distance
     * @param angleRad the angle in radians
     * @return the new x-value
     */
    private double polarX(double x, double radius, double angleRad) {
        return x + radius * Math.cos(angleRad);
    }


    /**
     * Finds the y value of the point at an angle of {@code angleRad} and distance of {@code radius} from {@code y}
     *
     * @param y        the y-value
     * @param radius   the distance
     * @param angleRad the angle in radians
     * @return the new y-value
     */
    private double polarY(double y, double radius, double angleRad) {
        return y - radius * Math.sin(angleRad);
    }

}
