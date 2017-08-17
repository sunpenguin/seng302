

package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import seng302.team18.model.Boat;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * The class to display the sails on the boat.
 */
public class DisplaySail extends DisplayBoatDecorator {

    private Polyline sail;
    private double windDirection;
    private PixelMapper pixelMapper;
    private final Rotate rotation = new Rotate(0, 0, 0);
    private final Scale zoom = new Scale(1, 1, 0, 0);
    private final double SAIL_LENGTH = 20;
    private double sailLength;
    private final double POWERED_UP_ANGLE = 20;


    /**
     * Creates a new instance of DisplayBoat
     *
     * @param boat the display boat being decorated
     */
    public DisplaySail(PixelMapper mapper, DisplayBoat boat) {
        super(boat);
        this.pixelMapper = mapper;
        sail = new Polyline();
        sailLength = SAIL_LENGTH * pixelMapper.mappingRatio();
        Double[] SAIL_OUT = new Double[]{
                0.0, 0.0,
                0.0, sailLength / 2
        };
        sail.getPoints().addAll(SAIL_OUT);
        sail.getTransforms().addAll(rotation, zoom);
        sail.toFront();
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
        super.addToGroup(group);
        sail.toFront();
    }


    public void removeFrom(Group group) {
        group.getChildren().remove(sail);
        super.removeFrom(group);
    }


    public void setApparentWindDirection(double apparentWind) {
        this.windDirection = apparentWind;
        super.setApparentWindDirection(apparentWind);
    }



    @Override
    public void setSailOut(boolean sailOut) {
        if (sailOut) {
            rotation.setAngle(windDirection);
        } else {
            rotation.setAngle(windDirection + POWERED_UP_ANGLE);
        }

        super.setSailOut(sailOut);
    }

}

