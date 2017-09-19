package seng302.team18.visualiser.display.object;

import javafx.scene.shape.Polyline;
import seng302.team18.model.Coordinate;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * Created by csl62 on 8/09/17.
 */
public class DisplayProjectile {

    private int projectileSize = 5;
    private Polyline polyline;
    private Coordinate location;
    private double speed;
    private PixelMapper mapper;


    public DisplayProjectile(int projectileSize, Polyline polyline, Coordinate location, PixelMapper mapper) {
        this.projectileSize = projectileSize;
        this.polyline = polyline;
        this.location = location;
        this.speed = 60;
        this.mapper = mapper;
        setUpBoatShape(mapper.mappingRatio());
    }

    private void setUpBoatShape(double mappingRatio) {
        double pixelLength = projectileSize * mappingRatio / 2;

        Double[] shape = new Double[]{
                0.0, -pixelLength,
                -pixelLength * 0.6667, pixelLength * 0.7454,
                pixelLength * 0.6667, pixelLength * 0.7454,
                0.0, -pixelLength
        };

        polyline.getPoints().clear();
        polyline.getPoints().addAll(shape);
    }


    public void setCoordinate(Coordinate coordinate) {
        location = coordinate;
        XYPair pixels = mapper.mapToPane(coordinate);
        polyline.setLayoutX(pixels.getX());
        polyline.setLayoutY(pixels.getY());
    }


    public Coordinate getCoordinate() {
        return location;
    }


}
