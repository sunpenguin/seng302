package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import seng302.team18.model.Coordinate;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * A class to display the wake of the boat
 */
public class DisplayWake extends DisplayBoatDecorator {


    private Polygon wake;
    private Color wakeColor = Color.CADETBLUE;
    private double wakeScaleFactor = 1.0d / 16.0d; // the wake is at normal size when the boat is moving 1 / wakeScaleFactor speed
    private double minWakeSize = 0.1;
    private double maxWakeSize = 2;
    private final double WAKE_OFFSET = 0;
    private final double WAKE_WIDTH = 10;
    private final double WAKE_HEIGHT = 20;
    private final Double[] WAKE_SHAPE = new Double[]{
            0.0, WAKE_OFFSET / 2,
            WAKE_WIDTH / -2, WAKE_OFFSET / 2 + WAKE_HEIGHT,
            WAKE_WIDTH / 2, WAKE_OFFSET / 2 + WAKE_HEIGHT
    };
    private final Scale wakeSpeed = new Scale(1, 1, WAKE_SHAPE[0], WAKE_SHAPE[1]);
    private final Scale wakeZoom = new Scale(1, 1, WAKE_SHAPE[0], WAKE_SHAPE[1]);
    private final Rotate rotation = new Rotate(0, 0, 0);

    private final PixelMapper pixelMapper;


    /**
     * Creates a new instance of DisplayBoat
     *
     * @param pixelMapper the mapper from coordinate system to screen pixels
     * @param boat        the display boat being decorated
     */
    public DisplayWake(PixelMapper pixelMapper, DisplayBoat boat) {
        super(boat);

        this.pixelMapper = pixelMapper;

        wake = new Polygon();
        wake.getPoints().addAll(WAKE_SHAPE);
        wake.setFill(wakeColor);
        wake.getTransforms().addAll(wakeSpeed, rotation, wakeZoom);
        wake.toBack();
    }


    public void setCoordinate(Coordinate coordinate) {
        XYPair pixels = pixelMapper.coordToPixel(coordinate);
        wake.setLayoutX(pixels.getX());
        wake.setLayoutY(pixels.getY());
        super.setCoordinate(coordinate);
    }


    public void setSpeed(double speed) {
        speed = Math.abs(speed);
        double scale = (speed != 0) ? speed * wakeScaleFactor : minWakeSize;
        scale = (scale > maxWakeSize) ? maxWakeSize : scale;
        wakeSpeed.setX(scale);
        wakeSpeed.setY(scale);
        super.setSpeed(speed);
    }


    public void setScale(double scaleFactor) {
        wakeZoom.setX(scaleFactor);
        wakeZoom.setY(scaleFactor);
        super.setScale(scaleFactor);
    }

    public void addToGroup(Group group) {
        group.getChildren().add(wake);
        super.addToGroup(group);
        wake.toBack();
    }


    public void setHeading(double heading) {
        rotation.setAngle(heading);
        super.setHeading(heading);
    }
}
