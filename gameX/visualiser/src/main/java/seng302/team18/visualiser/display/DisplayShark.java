package seng302.team18.visualiser.display;

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
    private double sharkSize;
    private Coordinate location;

    private final Rotate rotation = new Rotate(0, 0, 0);

    private Projectile projectile;

    private void setUpBoatShape(double mappingRatio) {
        double pixelLength = sharkSize * mappingRatio / 2;

        Double[] boatShape = new Double[]{
                0.0, -pixelLength,
                -pixelLength * 0.6667, pixelLength * 0.7454,
                pixelLength * 0.6667, pixelLength * 0.7454,
                0.0, -pixelLength
        };

        sharkLine.getPoints().clear();
        sharkLine.getPoints().addAll(boatShape);
    }

    public void setCoordinate(Coordinate coordinate) {
        location = coordinate;
        XYPair pixels = pixelMapper.mapToPane(coordinate);
        sharkLine.setLayoutX(pixels.getX());
        sharkLine.setLayoutY(pixels.getY());
    }


}
