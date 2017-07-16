package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;
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

    /**
     * Creates a new instance of DisplayBoat
     *
     * @param boat the display boat being decorated
     */
    public BoatHighlight(PixelMapper pixelMapper, DisplayBoat boat) {
        super(boat);
        this.pixelMapper = pixelMapper;
        highlight = new Circle(6);
        highlight.setFill(Color.YELLOW);
        highlight.getTransforms().addAll(zoom);
        highlight.toBack();
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



    public void addToGroup(Group group) {
        group.getChildren().add(highlight);
        highlight.toFront();
        super.addToGroup(group);
    }


}
