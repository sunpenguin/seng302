package seng302.team18.visualiser.display;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * Created by dhl25 on 30/06/17.
 */
public class DisplayWake extends DisplayBoatDecorator {


    private Polygon wake;
    private Color wakeColor = Color.CADETBLUE;
    private double wakeScaleFactor = 1.0d / 32.0d; // the wake is at normal size when the boat is moving 1 / wakeScaleFactor speed
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



    /**
     * Creates a new instance of DisplayBoat
     *
     * @param pixelMapper
     * @param name        the name of the boat
     * @param boatColor   the color to display the boat in
     * @param boat        the display boat being decorated
     */
    public DisplayWake(PixelMapper pixelMapper, String name, Color boatColor, DisplayBoat boat) {
        super(pixelMapper, name, boatColor, boat);

        wake = new Polygon();
        wake.getPoints().addAll(WAKE_SHAPE);
        wake.setFill(wakeColor);
        wake.getTransforms().addAll(wakeSpeed, rotation, wakeZoom);
    }



}
