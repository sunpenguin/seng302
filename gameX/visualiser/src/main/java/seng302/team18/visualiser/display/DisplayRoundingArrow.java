package seng302.team18.visualiser.display;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Mark;
import seng302.team18.util.GPSCalculator;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for information for arrow that rounds a mark.
 */
public class DisplayRoundingArrow {

    private Coordinate current;
    private CompoundMark next;
    private Polyline arrowHead;
    private Line arrowLine;
    private List<XYPair> endPoints  = new ArrayList<>();
    private GPSCalculator calculator = new GPSCalculator();
    private PixelMapper pixelMapper;
    private double bearing;


    public DisplayRoundingArrow(Coordinate current, CompoundMark next, PixelMapper pixelMapper) {
        this.current = current;
        this.next = next;
        arrowHead = new Polyline();
        this.pixelMapper = pixelMapper;
    }


    public void drawArrow() {
        makeHeadShape();
        calculateEndPoints();
        makeLine();
        setPosition();
    }

    private void calculateEndPoints() {
        Coordinate middle = null;

        if (next.isGate()) {
            Mark m1 = next.getMarks().get(0);
            Mark m2 = next.getMarks().get(1);
            middle = calculator.midPoint(m1.getCoordinate(), m2.getCoordinate());
        } else {
            middle = next.getMarks().get(0).getCoordinate();
        }

        bearing = calculator.getBearing(current, next.getCoordinate());
        Coordinate start = calculator.toCoordinate(middle, (bearing + 270) % 360, 20);
        Coordinate end = calculator.toCoordinate(start, (bearing + 180) % 360, 50);
        XYPair startPixel = pixelMapper.mapToPane(start);
        XYPair endPixel = pixelMapper.mapToPane(end);

        endPoints.add(startPixel);
        endPoints.add(endPixel);
    }

    private void makeHeadShape() {
        double pixelLength = next.getMarks().get(0).getLength();

        Double[] headShape = new Double[]{
                0.0, -pixelLength * 0.45,
                -pixelLength * 0.45, pixelLength * 0.45,
                pixelLength * 0.45, pixelLength * 0.45,
                0.0, -pixelLength * 0.45
        };

        arrowHead.getPoints().clear();
        arrowHead.getPoints().addAll(headShape);
    }

    private void makeLine() {
        arrowLine = new Line(
                endPoints.get(0).getX(), endPoints.get(0).getY(),
                endPoints.get(1).getX(), endPoints.get(1).getY());
        arrowLine.setFill(javafx.scene.paint.Color.WHITE);
        arrowLine.setStyle("-fx-stroke: green");
        arrowLine.setStrokeWidth(2);
    }

    public void addToGroup(Group group) {
        group.getChildren().add(arrowHead);
        group.getChildren().add(arrowLine);
    }


    public void removeFromGroup(Group group) {
        group.getChildren().remove(arrowHead);
        group.getChildren().remove(arrowLine);
    }

    public void setPosition() {
        arrowLine.setStartX(endPoints.get(0).getX());
        arrowLine.setStartY(endPoints.get(0).getY());
        arrowLine.setEndX(endPoints.get(1).getX());
        arrowLine.setEndY(endPoints.get(1).getY());

        arrowHead.setLayoutX(endPoints.get(0).getX());
        arrowHead.setLayoutY(endPoints.get(0).getY());
        arrowHead.setStyle("-fx-stroke: green");
        arrowHead.setFill(Color.GREEN);
        arrowHead.setRotate(bearing);
    }






    public void Curve (Group group) {
        CubicCurve curve1 = new CubicCurve( 125, 150, 125, 225, 325, 225, 325, 300);
        curve1.setStroke(Color.BLACK);
        curve1.setStrokeWidth(1);
        curve1.setFill( null);

        double size=Math.max(curve1.getBoundsInLocal().getWidth(),
                curve1.getBoundsInLocal().getHeight());
        double scale=size/4d;

        Point2D ori=eval(curve1,0);
        Point2D tan=evalDt(curve1,0).normalize().multiply(scale);
        Path arrowIni=new Path();
        arrowIni.getElements().add(new MoveTo(ori.getX()+0.2*tan.getX()-0.2*tan.getY(),
                ori.getY()+0.2*tan.getY()+0.2*tan.getX()));
        arrowIni.getElements().add(new LineTo(ori.getX(), ori.getY()));
        arrowIni.getElements().add(new LineTo(ori.getX()+0.2*tan.getX()+0.2*tan.getY(),
                ori.getY()+0.2*tan.getY()-0.2*tan.getX()));

        ori=eval(curve1,1);
        tan=evalDt(curve1,1).normalize().multiply(scale);
        Path arrowEnd=new Path();
        arrowEnd.getElements().add(new MoveTo(ori.getX()-0.2*tan.getX()-0.2*tan.getY(),
                ori.getY()-0.2*tan.getY()+0.2*tan.getX()));
        arrowEnd.getElements().add(new LineTo(ori.getX(), ori.getY()));
        arrowEnd.getElements().add(new LineTo(ori.getX()-0.2*tan.getX()+0.2*tan.getY(),
                ori.getY()-0.2*tan.getY()-0.2*tan.getX()));

        group.getChildren().addAll(curve1, arrowIni, arrowEnd);

//        primaryStage.setScene(new Scene(root, 800, 600));
//        primaryStage.show();
    }

    /**
     * Evaluate the cubic curve at a parameter 0<=t<=1, returns a Point2D
     * @param c the CubicCurve
     * @param t param between 0 and 1
     * @return a Point2D
     */
    private Point2D eval(CubicCurve c, float t){
        Point2D p=new Point2D(Math.pow(1-t,3)*c.getStartX()+
                3*t*Math.pow(1-t,2)*c.getControlX1()+
                3*(1-t)*t*t*c.getControlX2()+
                Math.pow(t, 3)*c.getEndX(),
                Math.pow(1-t,3)*c.getStartY()+
                        3*t*Math.pow(1-t, 2)*c.getControlY1()+
                        3*(1-t)*t*t*c.getControlY2()+
                        Math.pow(t, 3)*c.getEndY());
        return p;
    }

    /**
     * Evaluate the tangent of the cubic curve at a parameter 0<=t<=1, returns a Point2D
     * @param c the CubicCurve
     * @param t param between 0 and 1
     * @return a Point2D
     */
    private Point2D evalDt(CubicCurve c, float t){
        Point2D p=new Point2D(-3*Math.pow(1-t,2)*c.getStartX()+
                3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlX1()+
                3*((1-t)*2*t-t*t)*c.getControlX2()+
                3*Math.pow(t, 2)*c.getEndX(),
                -3*Math.pow(1-t,2)*c.getStartY()+
                        3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlY1()+
                        3*((1-t)*2*t-t*t)*c.getControlY2()+
                        3*Math.pow(t, 2)*c.getEndY());
        return p;
    }
}
