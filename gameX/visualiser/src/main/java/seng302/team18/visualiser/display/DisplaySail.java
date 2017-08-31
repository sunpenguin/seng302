

package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import seng302.team18.model.Coordinate;
import seng302.team18.util.GPSCalculations;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * The class to display the sails on the boat.
 */
public class DisplaySail extends DisplayBoatDecorator {

    private Polyline sail;
    private double windDirection;
    private double heading;
    private PixelMapper pixelMapper;
    private final Rotate rotation = new Rotate(0, 0, 0);
    private final Scale zoom = new Scale(1, 1, 0, 0);
    private final double SAIL_LENGTH = 20;
    private double sailLength;
    private final double poweredUpAngle = 60;


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
        XYPair pixels = pixelMapper.mapToPane(coordinate);
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


    public void setHeading(double heading) {
        this.heading = heading;
        super.setHeading(heading);
    }



    public void setApparentWindDirection(double apparentWind) {
        this.windDirection = apparentWind;
        super.setApparentWindDirection(apparentWind);
    }



    @Override
    public void setSailOut(boolean sailOut) {
        final int MAX_DEVIATION = 90;
        double sailAngle;
        if (sailOut) {
            sailAngle = getSailAngle(windDirection, MAX_DEVIATION);
        } else if ((windDirection - heading + 360) % 360 > 180) {
            sailAngle = getSailAngle(windDirection + poweredUpAngle, MAX_DEVIATION);
        } else {
            sailAngle = getSailAngle(windDirection - poweredUpAngle, MAX_DEVIATION);
        }
        rotation.setAngle(sailAngle);
        super.setSailOut(sailOut);
    }


    /**
     * Determines what the angle of the sail should be given the maximum allowed deviation from the boats heading.
     *
     * @param sailAngle angle of the sail.
     * @param maxDeviation max angle from directly .
     * @return the sails rotation.
     */
    private double getSailAngle(double sailAngle, double maxDeviation) {
        GPSCalculations gps = new GPSCalculations();
        double headingPlus = (heading + maxDeviation) % 360;
        double headingMinus = (heading - maxDeviation + 360) % 360;
        if (gps.isBetween(sailAngle, headingPlus, headingMinus)) {
            return maxDeviation + heading;
        }
        return sailAngle;
    }

}

