package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import seng302.team18.model.Coordinate;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * Created by cslaven on 6/07/17.
 */
public class DisplaySail extends DisplayBoatDecorator {

    private Polyline sail;
    private PixelMapper pixelMapper;
    private Coordinate start;
    private final Rotate rotation = new Rotate(0, 0, 0);
    private final Scale zoom = new Scale(1, 1, 0, 0);
    private final double SAIL_LENGTH = 15;
    private final Double[] SAIL_OUT = new Double[]{
            0.0, 0.0,
            0.0, SAIL_LENGTH / 2
    };

    /**
     * Creates a new instance of DisplayBoat
     *
     * @param boat the display boat being decorated
     */
    public DisplaySail(DisplayBoat boat, PixelMapper mapper) {
        super(boat);
        this.pixelMapper = mapper;
        sail = new Polyline();
        sail.getPoints().addAll(SAIL_OUT);
        sail.getTransforms().addAll(rotation);
    }

    public void setCoordinate(Coordinate coordinate) {
        XYPair pixels = pixelMapper.coordToPixel(coordinate);
        sail.setLayoutX(pixels.getX());
        sail.setLayoutY(pixels.getY());
        super.setCoordinate(coordinate);
    }

    public void setScale(double scaleFactor) {
        zoom.setX(scaleFactor);
        zoom.setY(scaleFactor);
        super.setScale(scaleFactor);
    }

    public void addToGroup(Group group){
        group.getChildren().add(sail);
        sail.toFront();
        super.addToGroup(group);
    }

    public void setHeading(double heading) {
        if (isSailOut()){
            rotation.setAngle(heading + 270);
        }
        else{
            rotation.setAngle(heading);
        }
        super.setHeading(heading);
    }


}
