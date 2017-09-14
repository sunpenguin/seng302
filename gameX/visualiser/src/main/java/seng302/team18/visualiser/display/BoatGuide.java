package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import seng302.team18.model.Coordinate;
import seng302.team18.util.GPSCalculator;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * The arrow that shows players where to go to.
 */
public class BoatGuide extends DisplayBoatDecorator {

    private Polygon arrow;
    private Translate translation = new Translate(0, 0);
    private Rotate rotation = new Rotate(0, 0, 0);
    private Coordinate location;
    private PixelMapper mapper;

    private final static GPSCalculator GPS = new GPSCalculator();


    BoatGuide(PixelMapper mapper, DisplayBoat boat) {
        super(boat);
        this.mapper = mapper;
        arrow = new Polygon();
        setUpShape(mapper.mappingRatio());
        arrow.getTransforms().addAll(rotation, translation);
    }


    private void setUpShape(double mappingRatio) {
        double pixelLength = getBoatLength() * mappingRatio / 2;

        Double[] shape = new Double[]{
                -0.33 * pixelLength, 0.0,
                0.0, -0.66 * pixelLength,
                0.33 * pixelLength, 0.0
        };

        arrow.getPoints().clear();
        arrow.getPoints().addAll(shape);
        translation.setY(-pixelLength);
    }


    @Override
    public void setCoordinate(Coordinate coordinate) {
        XYPair pixels = mapper.mapToPane(coordinate);

        this.location = coordinate;
        arrow.setLayoutX(pixels.getX());
        arrow.setLayoutY(pixels.getY());
        super.setCoordinate(coordinate);
    }


    @Override
    public void setScale(double scaleFactor) {
        setUpShape(scaleFactor);
        super.setScale(scaleFactor);
    }


    @Override
    public void addToGroup(Group group) {
        group.getChildren().add(arrow);
        super.addToGroup(group);
        arrow.toFront();
    }


    @Override
    public void removeFrom(Group group) {
        group.getChildren().remove(arrow);
        super.removeFrom(group);
    }


    @Override
    public void setDestination(Coordinate destination) {
        if (destination != null) {
            double destinationHeading = GPS.getBearing(location, destination);
            rotation.setAngle(destinationHeading);
        } else {
            arrow.setVisible(false);
        }
        super.setDestination(destination);
    }


}
