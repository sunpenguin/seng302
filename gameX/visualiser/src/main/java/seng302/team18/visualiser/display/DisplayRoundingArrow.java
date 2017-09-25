package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import seng302.team18.model.MarkRounding;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * Class for information for arrow that rounds a mark.
 */
public class DisplayRoundingArrow {

    private MarkRounding markRounding;
    private Shape head;
    private Arc tail;
    private PixelMapper pixelMapper;

    private final static Color COLOR = Color.GREEN;


    public DisplayRoundingArrow(PixelMapper pixelMapper, MarkRounding markRounding) {
        this.pixelMapper = pixelMapper;
        this.markRounding = markRounding;

        drawArrow();
    }


    /**
     * Create the rounding arrow.
     */
    private void drawArrow() {
        tail = makeTail();
        head = makeHeadShape();

        head.setLayoutX(calculateHeadX());
        head.setLayoutY(calculateHeadY());
        head.getTransforms().add(new Rotate(getHeadingAtFinish(), 0, 0));
    }


    /**
     * Create the size (shape) of the arrow head.
     */
    private Shape makeHeadShape() {
        double pixelLength = markRounding.getCompoundMark().getMarks().get(0).getLength() * pixelMapper.mappingRatio() * 0.2;

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


    private Arc makeTail() {
        XYPair center = pixelMapper.mapToPane(markRounding.getCompoundMark().getMarks().get(0).getCoordinate());
        final double radius = markRounding.getCompoundMark().getMarks().get(0).getLength() * 2 * pixelMapper.mappingRatio();

        Arc tail = new Arc(center.getX(), center.getY(), radius, radius, 90, 135);
        tail.setType(ArcType.OPEN);
        tail.setStroke(COLOR);
        tail.setStrokeWidth(2);
        tail.setFill(Color.TRANSPARENT);
        return tail;
    }


    /**
     * Add created arrow to the group.
     *
     * @param group arrow to be added on
     */
    public void addToGroup(Group group) {
        group.getChildren().add(head);
        group.getChildren().add(tail);
    }


    /**
     * Remove existing arrow from the group.
     *
     * @param group arrow to be removed from
     */
    public void removeFromGroup(Group group) {
        group.getChildren().remove(head);
        group.getChildren().remove(tail);
    }


    private double calculateHeadX() {
        return tail.getCenterX() + tail.getRadiusX() * Math.cos(Math.toRadians(tail.getStartAngle() + tail.getLength()));
    }


    private double calculateHeadY() {
        return tail.getCenterY() - tail.getRadiusY() * Math.sin(Math.toRadians(tail.getStartAngle() + tail.getLength()));
    }


    private double getHeadingAtFinish() {
        return (tail.getStartAngle() + tail.getLength() + ((tail.getLength() < 0) ? 90 : -90) + 360) % 360;
    }
}
