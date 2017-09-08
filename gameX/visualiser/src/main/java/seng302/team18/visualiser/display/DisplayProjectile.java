package seng302.team18.visualiser.display;

import javafx.scene.shape.Polyline;

/**
 * Created by csl62 on 8/09/17.
 */
public class DisplayProjectile {

    private int projectileSize = 5;
    private Polyline polyline;

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

}
