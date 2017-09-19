package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Mark;
import seng302.team18.util.GPSCalculator;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for information for arrow that rounds a mark.
 */
public class DisplayRoundingArrow {

    private CompoundMark current;
    private CompoundMark next;
    private Color color;
    private Polyline arrowHead;
    private Line arrowLine;
    private List<XYPair> endPoints  = new ArrayList<>();
    private Coordinate position;
    private GPSCalculator calculator = new GPSCalculator();
    private PixelMapper pixelMapper;


    public DisplayRoundingArrow(CompoundMark current, CompoundMark next, PixelMapper pixelMapper) {
        this.current = current;
        this.next = next;
        color = Color.green;
        arrowHead = new Polyline();
        this.pixelMapper = pixelMapper;
    }


    public void drawArrow() {
        double pixelLength = current.getMarks().get(0).getLength();

        Double[] headShape = new Double[]{
                0.0, -pixelLength,
                -pixelLength * 0.6667, pixelLength * 0.7454,
                pixelLength * 0.6667, pixelLength * 0.7454,
                0.0, -pixelLength
        };

        Coordinate middle = null;

        if (current.isGate()) {
            Mark m1 = current.getMarks().get(0);
            Mark m2 = current.getMarks().get(1);
            middle = calculator.midPoint(m1.getCoordinate(), m2.getCoordinate());
        } else {
            middle = current.getMarks().get(0).getCoordinate();
        }

        double bearing = calculator.getBearing(current.getCoordinate(), next.getCoordinate());

        Coordinate start = calculator.toCoordinate(middle, bearing, 100);
        Coordinate end = calculator.toCoordinate(start, bearing, 50);
        XYPair startPixel = pixelMapper.mapToPane(start);
        XYPair endPixel = pixelMapper.mapToPane(end);
        endPoints.add(startPixel);
        endPoints.add(endPixel);

        arrowLine = new Line(
                endPoints.get(0).getX(), endPoints.get(0).getY(),
                endPoints.get(1).getX(), endPoints.get(1).getY());
        arrowLine.setFill(javafx.scene.paint.Color.WHITE);
        arrowLine.setStyle("-fx-stroke: green");
//        line.setStrokeWidth(geScaledLineWeight());
        arrowLine.setStartX(endPoints.get(0).getX());
        arrowLine.setStartY(endPoints.get(0).getY());
        arrowLine.setEndX(endPoints.get(1).getX());
        arrowLine.setEndY(endPoints.get(1).getY());

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

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }
}
