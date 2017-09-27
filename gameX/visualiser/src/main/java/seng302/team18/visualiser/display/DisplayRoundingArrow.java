package seng302.team18.visualiser.display;

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
        drawStraightArrow(endPos, markSize, arrowHeading, is1Clockwise);

        endPos = polarToCartesian(mark2, -offset, bearing1to2);
        endPos = polarToCartesian(endPos, scaleValue(0, markSize * STRAIGHT_ARROW_LENGTH) / -2, arrowHeading);
        drawStraightArrow(endPos, markSize, arrowHeading, is1Clockwise);

    }


    private void drawMarkArrow(MarkRounding rounding) {
        boolean isClockwise = rounding.getRoundingDirection().equals(MarkRounding.Direction.STARBOARD);
        double offset = MARK_ARROW_ARC_LENGTH / (isClockwise ? 2 : -2);
        double startAngle = (rounding.getPassAngle() - offset + 540) % 360;
        XYPair center = pixelMapper.mapToPane(rounding.getCompoundMark().getMarks().get(0).getCoordinate());
        double markSize = rounding.getCompoundMark().getMarks().get(0).getLength();
        drawArcedArrow(center, markSize, startAngle, offset * 2, isClockwise);
    }


    /**
     * Create the rounding arrow.
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


    private void drawStraightArrow(XYPair endPos, double markSize, double heading, boolean is1Clockwise) {
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


    private double getLength(double maxLength) {
        return (System.currentTimeMillis() % PERIOD_MILLIS) * maxLength / PERIOD_MILLIS;
    }


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
     *
     * @param group arrow to be sent back on.
     */
    public void sendToBack(Group group) {
        shapes.forEach(Node::toBack);
    }


    private double calculateHeadX(Arc tail) {
        return polarX(tail.getCenterX(), tail.getRadiusX(), Math.toRadians(tail.getStartAngle() + tail.getLength()));
    }


    private double calculateHeadY(Arc tail) {
        return polarY(tail.getCenterY(), tail.getRadiusY(), Math.toRadians(tail.getStartAngle() + tail.getLength()));
    }


    private double getHeadingAtFinish(Arc tail) {
        double arcDegrees = tail.getStartAngle() + tail.getLength();
        double screenAngle = (720 - arcDegrees) % 360;
        if (tail.getLength() < 0) {
            screenAngle = (screenAngle + 180) % 360;
        }
        return screenAngle;
    }


    private XYPair polarToCartesian(XYPair start, double radius, double angleDeg) {
        double angleRad = Math.toRadians(angleDeg);
        return new XYPair(polarX(start.getX(), radius, angleRad), polarY(start.getY(), radius, angleRad));
    }


    private double polarX(double x, double radius, double angleRad) {
        return x + radius * Math.cos(angleRad);
    }


    private double polarY(double y, double radius, double angleRad) {
        return y - radius * Math.sin(angleRad);
    }

}
