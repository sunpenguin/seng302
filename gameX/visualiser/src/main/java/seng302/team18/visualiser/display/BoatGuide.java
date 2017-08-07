package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import seng302.team18.model.Coordinate;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * The arrow that shows players where to go to.
 */
public class BoatGuide extends DisplayBoatDecorator {

    private Polygon arrow;
    private Scale scale;
    private Rotate rotation;
    private PixelMapper mapper;


    public BoatGuide(PixelMapper mapper, DisplayBoat boat) {
        super(boat);
        this.mapper = mapper;
        arrow = new Polygon();
        arrow.getPoints().addAll(
                -4d, 0d,
                0d, 8d,
                4d, 0d);
        scale = new Scale(1, 1, 0, 0);
        rotation = new Rotate(0, 0, 0);
        arrow.getTransforms().addAll(scale);
    }

    public void setCoordinate(Coordinate coordinate) {
        double radius = 11d; // pixels
        double angle = 180d;
        double xOffset = Math.sin(Math.toRadians(angle)) * radius;
        double yOffset = Math.cos(Math.toRadians(angle)) * radius;
        XYPair pixels = mapper.coordToPixel(coordinate);
        arrow.setLayoutX(pixels.getX() + xOffset);
        arrow.setLayoutY(pixels.getY() + yOffset);
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


//    public void setHeading(double heading) {
//        rotation.setAngle(heading);
//        super.setHeading(heading);
//    }



}
