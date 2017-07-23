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
    private double apparentWindDirection;
    private PixelMapper pixelMapper;
    private final Rotate rotation = new Rotate(0, 0, 0);
    private final Scale zoom = new Scale(1, 1, 0, 0);
    private final double SAIL_LENGTH = 15;
    private final Double[] SAIL_OUT = new Double[]{
            0.0, 0.0,
            0.0, SAIL_LENGTH / 2
    };
    private double heading;
    private boolean sailOut;


    /**
     * Creates a new instance of DisplayBoat
     *
     * @param boat the display boat being decorated
     */
    public DisplaySail(PixelMapper mapper, DisplayBoat boat) {
        super(boat);
        this.pixelMapper = mapper;
        sail = new Polyline();
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


    public void setApparentWindDirection(double apparentWind) {
        this.apparentWindDirection = apparentWind;
        super.setApparentWindDirection(apparentWind);
    }

    public void setHeading(double heading) {
        this.heading  = heading;
        super.setHeading(heading);
    }

    @Override
    public void setSailOut(boolean sailOut) {
        this.sailOut = sailOut;

        if (sailOut) {
            rotation.setAngle(apparentWindDirection);
        } else {
            rotation.setAngle(heading);
        }

        super.setSailOut(sailOut);
    }
}
