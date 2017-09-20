package seng302.team18.visualiser.display.object;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Rotate;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Projectile;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * Created by cslaven on 10/09/17.
 */
public class DisplayShark {

    private PixelMapper pixelMapper;
    private Polyline sharkLine;
    private double sharkSize = 5;
    private Coordinate location;
    private int id;

    private final Rotate rotation = new Rotate(0, 0, 0);

    private Projectile projectile;


    public DisplayShark(Projectile projectile, PixelMapper pixelMapper) {
        this.projectile = projectile;
        this.pixelMapper = pixelMapper;
        this.id = projectile.getId();

        sharkLine = new Polyline();
        setUpSharkShape(pixelMapper.mappingRatio());
        sharkLine.setFill(Color.BEIGE);

        sharkLine.getTransforms().add(rotation);
    }

    private void setUpSharkShape(double mappingRatio) {
        double pixelLength = sharkSize * mappingRatio / 2;

        Double[] sharkShape = new Double[]{
                0.0, -pixelLength,
                -pixelLength * 0.6667, pixelLength * 0.7454,
                pixelLength * 0.6667, pixelLength * 0.7454,
                0.0, -pixelLength
        };

        sharkLine.getPoints().clear();
        sharkLine.getPoints().addAll(sharkShape);
    }

    public void setScale(double scaleFactor) {
        setUpSharkShape(scaleFactor);
    }


    public void addToGroup(Group group) {
        group.getChildren().add(sharkLine);
        sharkLine.toFront();
    }

    public void removeFrom(Group group) {
        group.getChildren().remove(sharkLine);
    }

    public void setCoordinate(Coordinate coordinate) {
        location = coordinate;
        XYPair pixels = pixelMapper.mapToPane(coordinate);
        sharkLine.setLayoutX(pixels.getX());
        sharkLine.setLayoutY(pixels.getY());
    }

    public void setHeading(double angle){
        rotation.setAngle(angle);
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }
}
