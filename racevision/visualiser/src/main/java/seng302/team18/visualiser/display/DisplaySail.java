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
    public DisplaySail(PixelMapper mapper, DisplayBoat boat) {
        super(boat);
        this.pixelMapper = mapper;
        sail = new Polyline();
        sail.getPoints().addAll(SAIL_OUT);
        sail.getTransforms().addAll(rotation, zoom);
        sail.toFront();
    }

    /**
     * A setter for the Coordinate that the DisplaySail is located at
     * @param coordinate The coordinate the sail will be set to
     */
    public void setCoordinate(Coordinate coordinate) {
        XYPair pixels = pixelMapper.coordToPixel(coordinate);
        sail.setLayoutX(pixels.getX());
        sail.setLayoutY(pixels.getY());
        super.setCoordinate(coordinate);
    }

    /**
     * Sets the scale factor of the DisplaySail
     * @param scaleFactor The scale factor that will be set
     */
    public void setScale(double scaleFactor) {
        zoom.setX(scaleFactor);
        zoom.setY(scaleFactor);
        super.setScale(scaleFactor);
    }

    /**
     * Adds he DisplaySail to the group so it can be displayed
     * @param group The group the DisplaySail will be added to
     */
    public void addToGroup(Group group){
        group.getChildren().add(sail);
        super.addToGroup(group);
        sail.toFront();

    }

    /**
     * Sets the current heading of the DisplaySail
     * @param heading The heading the sail will be set to
     */
    public void setHeading(double heading) {
        super.setHeading(heading);
    }

    /**
     * Sets the windDirection parameter and changes the rotation of the sail accordingly
     * @param windDirection The direction the WindDirectio will be set to
     */
    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
        rotation.setAngle(windDirection);
    }

    /**
     * Sets the rotation of the sail
     * @param rotation The value the rotation will be set to
     */
    public void setRotation(double rotation){
        this.rotation.setAngle(rotation);
    }


    /**
     * Returns the current rotation of the sail
     * @return
     */
    public Rotate getRotation() {
        return rotation;
    }
}
