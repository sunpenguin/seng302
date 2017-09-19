package seng302.team18.visualiser.display;

import javafx.scene.shape.*;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Mark;

import java.awt.Color;
import java.util.List;

import javafx.scene.Group;
import seng302.team18.util.GPSCalculator;
import seng302.team18.util.XYPair;

/**
 * Class for information for arrow that rounds a mark.
 */
public class DisplayRoundingArrow {

    private Mark markToBeRounded;
    private Color color;
    private Polyline arrowHead;
    private Line arrowLine;
    private List<XYPair> endPoints;
    private Coordinate position;
    private GPSCalculator calculator = new GPSCalculator();


    DisplayRoundingArrow(Mark mark) {
        markToBeRounded = mark;
        color = Color.green;
        arrowHead = new Polyline();
//        position = calculator.toCoordinate();

        drawArrow();
    }


    public void drawArrow() {
        double pixelLength = markToBeRounded.getLength();

        Double[] headShape = new Double[]{
                0.0, -pixelLength,
                -pixelLength * 0.6667, pixelLength * 0.7454,
                pixelLength * 0.6667, pixelLength * 0.7454,
                0.0, -pixelLength
        };



        arrowLine = new Line(
                endPoints.get(0).getX(), endPoints.get(0).getY(),
                endPoints.get(1).getX(), endPoints.get(1).getY());
        arrowLine.setFill(javafx.scene.paint.Color.WHITE);
        arrowLine.setStyle("-fx-stroke: green");

        arrowHead.getPoints().clear();
        arrowHead.getPoints().addAll(headShape);
    }


    public void addToGroup(Group group) {
        group.getChildren().add(arrowHead);
        group.getChildren().add(arrowLine);
    }


    public void removeFromGroup(Group group) {
        group.getChildren().remove(arrowHead);
        group.getChildren().remove(arrowLine);
    }
}
