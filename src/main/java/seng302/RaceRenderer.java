package seng302;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Class that takes a Race and a Group and draws the Race on the Group.
 */
public class RaceRenderer {

    private Group group;
    private Race race;
    private HashMap<String, Circle> boats;
    final private Color MARK_COLOR = Color.BLACK;
    final private double MARK_SIZE = 10.0;

    /**
     * Constructor for RaceRenderer, takes a Race and Group as parameters.
     *
     * @param race the race containing the boats to be drawn
     * @param group the group to be drawn on
     */
    public RaceRenderer(Race race, Group group) {
        this.race = race;
        this.group = group;
        boats = new HashMap<>();
        final double BOAT_RADIUS = 10.0;
        final ArrayList<Color> BOAT_COLOURS = new ArrayList<>(
                Arrays.asList(Color.CORNSILK, Color.BEIGE, Color.GREEN, Color.YELLOW, Color.RED, Color.BROWN));
        for (int i = 0; i < race.getStartingList().size(); i++) {
            Circle boat = new Circle(BOAT_RADIUS, BOAT_COLOURS.get(i));
            boats.put(race.getStartingList().get(i).getBoatName(), boat);
            this.group.getChildren().add(boat);
        }
    }


    public void renderCourse() {
        final int SINGLE_MARK_SIZE = 1;
        final int GATE_SIZE = 2;
        ArrayList<CompoundMark> compoundMarks = race.getCourse().getCompoundMarks();
        for (int i = 0 ; i < compoundMarks.size(); i++) {
            CompoundMark compoundMark = compoundMarks.get(i);
            System.out.println(compoundMark.getMarks().get(0).getMarkName());
            if (compoundMark.getMarks().size() == SINGLE_MARK_SIZE) {
                renderMark(compoundMark.getMarks().get(0));
            } else if (compoundMark.getMarks().size() == GATE_SIZE) {
                renderGate(compoundMark);
            }
        }
    }


    private void renderMark(Mark mark) {
        Rectangle rectangle = new Rectangle(MARK_SIZE, MARK_SIZE, MARK_COLOR);
        Coordinate coordinate = mark.getMarkCoordinates();
        ArrayList<Double> pixelCoordinates = convertCoordPixel(coordinate);
        rectangle.setX(pixelCoordinates.get(0));
        rectangle.setY(pixelCoordinates.get(1));
        group.getChildren().add(rectangle);
    }


    private void renderGate(CompoundMark compoundMark) {
        ArrayList<Rectangle> endPoints = new ArrayList<>();
        for (int i = 0; i < compoundMark.getMarks().size(); i++) {
            Mark mark = compoundMark.getMarks().get(i);
            Rectangle rectangle = new Rectangle(MARK_SIZE, MARK_SIZE, MARK_COLOR);
            Coordinate coordinate = mark.getMarkCoordinates();
            ArrayList<Double> pixelCoordinates = convertCoordPixel(coordinate);
            rectangle.setX(pixelCoordinates.get(0));
            rectangle.setY(pixelCoordinates.get(1));
            endPoints.add(rectangle);
            group.getChildren().add(rectangle);
        }
        Line line = new Line(
                endPoints.get(0).getX(), endPoints.get(0).getY(),
                endPoints.get(1).getX(), endPoints.get(1).getY());
        group.getChildren().add(line);
    }


    /**
     * Draws boats in the Race on the Group.
     */
    public void renderBoats() {
        for (int i = 0; i < race.getStartingList().size(); i++) {
            Boat boat = race.getStartingList().get(i);
            Coordinate boatCoordinates = boat.getBoatCoordinates();
            ArrayList<Double> pixels = convertCoordPixel(boatCoordinates);
            Circle boatImage = boats.get(boat.getBoatName());
            boatImage.setCenterX(pixels.get(0));
            boatImage.setCenterY(pixels.get(1));
        }
    }

    /**
     * Converts the latitude / longitude coordinates to pixel coordinates.
     * @param coord Coordinates to be converted
     * @return x and y pixel coordinates of the given coordinates
     */
    private ArrayList<Double> convertCoordPixel(Coordinate coord) {
        ArrayList<Double> pixels = new ArrayList<>();
//        double pixelWidth = group.getParent().prefWidth();
//        double pixelHeight = group.getParent().prefHeight();
        double pixelWidth = 1280.0;
        double pixelHeight = 720.0;
//        System.out.println("pix " + pixelHeight);
//        System.out.println("pix " + pixelWidth);

        GPSCalculations gps = new GPSCalculations();
        gps.findMinMaxPoints(race.getCourse());

        double courseWidth = gps.getMaxX() - gps.getMinX();
        double courseHeight = gps.getMaxY() - gps.getMinY();
//        System.out.println("course " + courseHeight);
//        System.out.println("course " + courseWidth);

        XYPair xy = GPSCalculations.GPSxy(coord);
        double widthRatio = (courseWidth - (gps.getMaxX() - xy.getX())) / courseWidth;
        double heightRatio = (courseHeight - (gps.getMaxY() - xy.getY())) / courseHeight;
//        System.out.println("ratio " + widthRatio);
//        System.out.println("ratio " + heightRatio);

        double pixelX = pixelWidth * widthRatio;
        double pixelY = pixelHeight * heightRatio;

        pixels.add(pixelX);
        pixels.add(pixelY);
        System.out.println("pix = " + pixels);
        return pixels;
    }

}
