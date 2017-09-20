package seng302.team18.visualiser.display.object;

import javafx.scene.Group;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Rotate;
import seng302.team18.model.Coordinate;
import seng302.team18.util.GPSCalculator;
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
    private final static double POWERED_UP_ANGLE = 60;
    private final double strokeWidth;


    /**
     * Creates a new instance of DisplayBoat
     *
     * @param boat the display boat being decorated
     */
    public DisplaySail(PixelMapper mapper, DisplayBoat boat) {
        super(boat);
        this.pixelMapper = mapper;
        sail = new Polyline();
        strokeWidth = sail.getStrokeWidth();
        setUpShape(mapper.mappingRatio());
        sail.getTransforms().addAll(rotation);
        sail.toFront();
    }


    private void setUpShape(double mappingRatio) {
        double pixelLength = getBoatLength() * mappingRatio / 2;

        Double[] shape = new Double[]{
                0.0, 0.0,
                0.0, pixelLength
        };

        sail.getPoints().clear();
        sail.setStrokeWidth(strokeWidth * mappingRatio);
        sail.getPoints().addAll(shape);
    }


    @Override
    public void setCoordinate(Coordinate coordinate) {
        XYPair pixels = pixelMapper.mapToPane(coordinate);
        sail.setLayoutX(pixels.getX());
        sail.setLayoutY(pixels.getY());
        super.setCoordinate(coordinate);
    }


    @Override
    public void setScale(double scaleFactor) {
        setUpShape(scaleFactor);
        super.setScale(scaleFactor);
    }


    @Override
    public void addToGroup(Group group) {
        group.getChildren().add(sail);
        super.addToGroup(group);
        sail.toFront();
    }


    @Override
    public void removeFrom(Group group) {
        group.getChildren().remove(sail);
        super.removeFrom(group);
    }


    @Override
    public void setHeading(double heading) {
        this.heading = heading;
        super.setHeading(heading);
    }


    @Override
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
            sailAngle = getSailAngle(windDirection + POWERED_UP_ANGLE, MAX_DEVIATION);
        } else {
            sailAngle = getSailAngle(windDirection - POWERED_UP_ANGLE, MAX_DEVIATION);
        }
        rotation.setAngle(sailAngle);
        super.setSailOut(sailOut);
    }


    /**
     * Determines what the angle of the sail should be given the maximum allowed deviation from the boats heading.
     *
     * @param sailAngle    angle of the sail.
     * @param maxDeviation max angle from directly .
     * @return the sails rotation.
     */
    private double getSailAngle(double sailAngle, double maxDeviation) {
        GPSCalculator gps = new GPSCalculator();
        double headingPlus = (heading + maxDeviation) % 360;
        double headingMinus = (heading - maxDeviation + 360) % 360;
        if (gps.isBetween(sailAngle, headingPlus, headingMinus)) {
            return maxDeviation + heading;
        }
        return sailAngle;
    }

}

