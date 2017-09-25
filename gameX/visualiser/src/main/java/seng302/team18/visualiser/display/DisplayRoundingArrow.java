package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import seng302.team18.model.AbstractBoat;
import seng302.team18.model.MarkRounding;
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

    private final static Color COLOR = Color.GREEN;


    public DisplayRoundingArrow(PixelMapper pixelMapper, MarkRounding markRounding) {
        this.pixelMapper = pixelMapper;

        if (markRounding.getCompoundMark().isGate()) {
            boolean isPS = markRounding.getRoundingDirection().equals(MarkRounding.Direction.PS);
            drawArrow(markRounding.getCompoundMark().getMarks().get(0), !isPS);
            drawArrow(markRounding.getCompoundMark().getMarks().get(1), isPS);
        } else {
            drawArrow(markRounding.getCompoundMark().getMarks().get(0), markRounding.getRoundingDirection().equals(MarkRounding.Direction.STARBOARD));
        }
    }


    /**
     * Create the rounding arrow.
     */
    private void drawArrow(AbstractBoat mark, boolean isClockwise) {
        Arc tail = makeTail(mark, isClockwise);
        Shape head = makeHeadShape(mark);

        shapes.add(tail);
        shapes.add(head);

        head.setLayoutX(calculateHeadX(tail));
        head.setLayoutY(calculateHeadY(tail));
        head.getTransforms().add(new Rotate(getHeadingAtFinish(tail), 0, 0));
    }


    private Arc makeTail(AbstractBoat mark, boolean isClockwise) {
        XYPair center = pixelMapper.mapToPane(mark.getCoordinate());
        final double radius = mark.getLength() * 2 * pixelMapper.mappingRatio();
        double length = 135 * (isClockwise ? -1 : 1);

        Arc tail = new Arc(center.getX(), center.getY(), radius, radius, 90, length);
        tail.setType(ArcType.OPEN);
        tail.setStroke(COLOR);
        tail.setStrokeWidth(2);
        tail.setFill(Color.TRANSPARENT);
        return tail;
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
        return (tail.getStartAngle() + tail.getLength() + 270) % 360;
    }
}
