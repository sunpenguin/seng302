package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import seng302.team18.model.Coordinate;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * A class to display a circular highlight around a specific boat
 */
public class BoatHighlight extends DisplayBoatDecorator {

    private PixelMapper pixelMapper;
    private Circle highlight;
    private Scale zoom = new Scale(0, 0);
    private final Rotate rotation = new Rotate(0, 0, 0);

    /**
     * Creates a new instance of DisplayBoat
     *
     * @param boat the display boat being decorated
     * @param pixelMapper The PixelMapper used to find where to map the highlight
     */
    public BoatHighlight(PixelMapper pixelMapper, DisplayBoat boat) {
        super(boat);

        final Translate translate = new Translate(0, 1);

        this.pixelMapper = pixelMapper;
        highlight = new Circle(11);
        highlight.setFill(Color.YELLOW);
        highlight.getTransforms().addAll(zoom, rotation, translate);
        highlight.toBack();
        highlight.setOpacity(0.5);
    }


    public void setCoordinate(Coordinate coordinate) {
        XYPair pixel = pixelMapper.coordToPixel(coordinate);
        highlight.setLayoutX(pixel.getX());
        highlight.setLayoutY(pixel.getY());
        super.setCoordinate(coordinate);
    }


    public void setScale(double scaleFactor) {
        zoom.setX(scaleFactor);
        zoom.setY(scaleFactor);
        super.setScale(scaleFactor);
    }


    public void setHeading(double heading) {
        rotation.setAngle(heading);
        super.setHeading(heading);
    }


    public void addToGroup(Group group) {
        group.getChildren().add(highlight);
        super.addToGroup(group);
        highlight.toBack();
    }


    public void removeFrom(Group group) {
        group.getChildren().remove(highlight);
        super.removeFrom(group);
    }
}
