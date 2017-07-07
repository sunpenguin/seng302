package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import seng302.team18.model.Coordinate;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * Created by cslaven on 6/07/17.
 */
public class DisplaySail extends DisplayBoatDecorator {

    private Line sail;
    private PixelMapper pixelMapper;
    private Coordinate start;
    private final Rotate rotation = new Rotate(0, 0, 0);
    private final double length = 10;

    /**
     * Creates a new instance of DisplayBoat
     *
     * @param boat the display boat being decorated
     */
    public DisplaySail(DisplayBoat boat, PixelMapper mapper) {
        super(boat);
        this.pixelMapper = mapper;
        sail = new Line();


    }

    @Override
    public void setCoordinate(Coordinate coordinate) {
        start = coordinate;
        XYPair pixels = pixelMapper.coordToPixel(coordinate);
        sail.setStartX(pixels.getX());
        sail.setStartY(pixels.getY());
        sail.setEndX(pixels.getX()+10);
        sail.setEndY(pixels.getY()+10);
    }

    public void addToGroup(Group group){
        group.getChildren().add(sail);
    }


    public void setHeading(double heading) {
        rotation.setAngle(heading);
        super.setHeading(heading);
    }

}
