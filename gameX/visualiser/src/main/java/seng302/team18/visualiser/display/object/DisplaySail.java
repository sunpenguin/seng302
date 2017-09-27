package seng302.team18.visualiser.display.object;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import seng302.team18.model.Coordinate;
import seng302.team18.util.GPSCalculator;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * The class to display the sails on the boat.
 */
public class DisplaySail extends DisplayBoatDecorator {

    private double windDirection;
    private double heading;
    private PixelMapper pixelMapper;
    private final static double POWERED_UP_ANGLE = 60;

    private double sailAngle = 0;

    private Image sailImage = new Image("/images/race_view/sail.png");
    private ImageView sailView = new ImageView(sailImage);
    private Image sailImageFlipped = new Image("/images/race_view/sail_flipped.png");
    private ImageView sailViewFlipped = new ImageView(sailImageFlipped);


    /**
     * Creates a new instance of DisplayBoat
     *
     * @param boat the display boat being decorated
     */
    public DisplaySail(PixelMapper mapper, DisplayBoat boat) {
        super(boat);
        this.pixelMapper = mapper;

        sailView.toFront();
        sailViewFlipped.toFront();

        sailView.setVisible(true);
        sailViewFlipped.setVisible(false);
    }


    @Override
    public void setCoordinate(Coordinate coordinate) {
        XYPair pixels = pixelMapper.mapToPane(coordinate);
        super.setCoordinate(coordinate);


        double boatSize = pixelMapper.mappingRatio() * getBoatLength();
        double boatImageSize = sailImage.getHeight();
        double imageRatio = boatImageSize / (boatSize);  // Ratio for scaling image to correct size

        sailView.setScaleX(1 / imageRatio);
        sailView.setScaleY(1 / imageRatio);

        sailViewFlipped.setScaleX(1 / imageRatio);
        sailViewFlipped.setScaleY(1 / imageRatio);

        sailView.setLayoutX(pixels.getX() - (boatImageSize / 2));
        sailView.setLayoutY(pixels.getY() - (boatImageSize / 2));

        sailViewFlipped.setLayoutX(pixels.getX() - (boatImageSize / 2));
        sailViewFlipped.setLayoutY(pixels.getY() - (boatImageSize / 2));
    }


    @Override
    public void setScale(double scaleFactor) {
        super.setScale(scaleFactor);
    }


    @Override
    public void addToGroup(Group group) {
        super.addToGroup(group);

        sailView.toFront();
        sailViewFlipped.toFront();

        group.getChildren().add(sailView);
        group.getChildren().add(sailViewFlipped);
    }


    @Override
    public void removeFrom(Group group) {
        group.getChildren().remove(sailView);
        group.getChildren().remove(sailViewFlipped);
        super.removeFrom(group);
    }


    @Override
    public void setHeading(double heading) {
        this.heading = heading;
        super.setHeading(heading);
        setSailSide();
    }


    @Override
    public void setApparentWindDirection(double apparentWind) {
        this.windDirection = apparentWind;
        super.setApparentWindDirection(apparentWind);
        setSailSide();
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
        super.setSailOut(sailOut);

        this.sailAngle =  sailAngle;
        sailView.setRotate(sailAngle - 45);
        sailViewFlipped.setRotate(sailAngle - 135);
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


    private void setSailSide() {
        if (sailAngle < windDirection) {
            sailViewFlipped.setVisible(true);
            sailView.setVisible(false);
        } else {
            sailViewFlipped.setVisible(false);
            sailView.setVisible(true);        }
    }

}

