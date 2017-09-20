package seng302.team18.visualiser.display.object;

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
    private final static Color WAKE_COLOR = Color.CADETBLUE;
    private final static double WAKE_SCALE_FACTOR = 1.0d / 16.0d; // the wake is at normal size when the boat is moving 1 / WAKE_SCALE_FACTOR speed
    private final static double MIN_WAKE_SIZE = 0.1;
    private final static double MAX_WAKE_SIZE = 2;
    private final static double WAKE_OFFSET = 0;
    private final static double WAKE_WIDTH = 10;
    private final static double WAKE_HEIGHT = 20;
    private final Rotate rotation = new Rotate(0, 0, 0);
    private Scale wakeSpeed;
    private Scale wakeZoom;

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

        double wakeHeight = WAKE_HEIGHT * pixelMapper.mappingRatio();
        double wakeWidth = WAKE_WIDTH * pixelMapper.mappingRatio();

        Double[] WAKE_SHAPE = new Double[]{
                0.0, WAKE_OFFSET / 2,
                wakeWidth / -2, WAKE_OFFSET / 2 + wakeHeight,
                wakeWidth / 2, WAKE_OFFSET / 2 + wakeHeight
        };
        wakeSpeed = new Scale(1, 1, WAKE_SHAPE[0], WAKE_SHAPE[1]);
        wakeZoom = new Scale(1, 1, WAKE_SHAPE[0], WAKE_SHAPE[1]);
        wake.getPoints().addAll(WAKE_SHAPE);
        wake.setFill(WAKE_COLOR);
        wake.getTransforms().addAll(wakeSpeed, rotation, wakeZoom);
        wake.toBack();
        wake.setOpacity(0.6);
    }


    @Override
    public void setCoordinate(Coordinate coordinate) {
        XYPair pixels = pixelMapper.mapToPane(coordinate);
        wake.setLayoutX(pixels.getX());
        wake.setLayoutY(pixels.getY());
        super.setCoordinate(coordinate);
    }


    @Override
    public void setSpeed(double speed) {
        speed = Math.abs(speed);
        double scale = (speed != 0) ? speed * WAKE_SCALE_FACTOR : MIN_WAKE_SIZE;
        scale = (scale > MAX_WAKE_SIZE) ? MAX_WAKE_SIZE : scale;
        wakeSpeed.setX(scale);
        wakeSpeed.setY(scale);
        super.setSpeed(speed);
    }


    @Override
    public void setScale(double scaleFactor) {
        wakeZoom.setX(scaleFactor);
        wakeZoom.setY(scaleFactor);
        super.setScale(scaleFactor);
    }


    @Override
    public void addToGroup(Group group) {
        group.getChildren().add(wake);
        super.addToGroup(group);
        wake.toBack();
    }


    @Override
    public void removeFrom(Group group) {
        group.getChildren().remove(wake);
        super.removeFrom(group);
    }


    @Override
    public void setHeading(double heading) {
        rotation.setAngle(heading);
        super.setHeading(heading);
    }
}
