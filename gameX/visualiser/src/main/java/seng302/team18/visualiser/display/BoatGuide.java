package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import seng302.team18.model.Coordinate;
import seng302.team18.util.GPSCalculations;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * The arrow that shows players where to go to.
 */
public class BoatGuide extends DisplayBoatDecorator {

    private Polygon arrow;
    private Scale scale;
    private Rotate rotation;
    private Coordinate location;
    private PixelMapper mapper;


    public BoatGuide(PixelMapper mapper, DisplayBoat boat) {
        super(boat);
        this.mapper = mapper;
        arrow = new Polygon();
        arrow.getPoints().addAll(
                -4d, -10d,
                0d, -18d,
                4d, -10d);
        scale = new Scale(1, 1, 0, 0);
        rotation = new Rotate(0, 0, 0);
        arrow.getTransforms().addAll(scale, rotation);
    }

    public void setCoordinate(Coordinate coordinate) {
        XYPair pixels = mapper.coordToPixel(coordinate);

        this.location = coordinate;
        arrow.setLayoutX(pixels.getX());
        arrow.setLayoutY(pixels.getY());
        super.setCoordinate(coordinate);
    }


    public void setScale(double scaleFactor) {
        scale.setX(scaleFactor);
        scale.setY(scaleFactor);
        super.setScale(scaleFactor);
    }


    public void addToGroup(Group group) {
        group.getChildren().add(arrow);
        super.addToGroup(group);
        arrow.toFront();
    }


    public void setDestination(Coordinate destination) {
        double destinationHeading = GPSCalculations.getBearing(location, destination);
        rotation.setAngle(destinationHeading);
        super.setDestination(destination);
    }



}
