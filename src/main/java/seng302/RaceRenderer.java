package seng302;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
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
    final double PADDING = 15.0;

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
        XYPair pixelCoordinates = convertCoordPixel(coordinate);
        rectangle.setX(pixelCoordinates.getX());
        rectangle.setY(pixelCoordinates.getY());
        group.getChildren().add(rectangle);
    }


    private void renderGate(CompoundMark compoundMark) {
        ArrayList<Rectangle> endPoints = new ArrayList<>();
        for (int i = 0; i < compoundMark.getMarks().size(); i++) {
            Mark mark = compoundMark.getMarks().get(i);
            Rectangle rectangle = new Rectangle(MARK_SIZE, MARK_SIZE, MARK_COLOR);
            Coordinate coordinate = mark.getMarkCoordinates();
            XYPair pixelCoordinates = convertCoordPixel(coordinate);
            rectangle.setX(pixelCoordinates.getX());
            rectangle.setY(pixelCoordinates.getY());
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
            Coordinate boatCoordinates = boat.getCoordinate();
            XYPair pixels = convertCoordPixel(boatCoordinates);
            System.out.println("pix x = " + pixels.getX());
            System.out.println("pix y = " + pixels.getY());
            System.out.println();
            Circle boatImage = boats.get(boat.getBoatName());
            boatImage.setCenterX(pixels.getX());
            boatImage.setCenterY(pixels.getY());
        }
    }

    /**
     * Converts the latitude / longitude coordinates to pixel coordinates.
     * @param coord Coordinates to be converted
     * @return x and y pixel coordinates of the given coordinates
     */
    private XYPair convertCoordPixel(Coordinate coord) {
        double pixelWidth = 1280 - PADDING * 2; // TODO get size of screen
        double pixelHeight = 720.0 - PADDING * 2;
//        double pixelHeight = group.getLayoutY() - PADDING * 2;
//        double pixelWidth = group.getParent().getBoundsInLocal().getWidth() - PADDING * 2;
//        double pixelHeight = group.getParent().getBoundsInLocal().getHeight() - PADDING * 2;
//        System.out.println(group.getParent().getBoundsInLocal().getWidth());
//        System.out.println(group.getParent().getBoundsInParent().getWidth());
//        System.out.println(pixelWidth);
//        System.out.println();

        GPSCalculations gps = new GPSCalculations();
        gps.findMinMaxPoints(race.getCourse());
        double courseWidth = gps.getMaxX() - gps.getMinX();
        double courseHeight = gps.getMaxY() - gps.getMinY();


        XYPair planeCoordinates = GPSCalculations.GPSxy(coord);
        double widthRatio = (courseWidth - (gps.getMaxX() - planeCoordinates.getX())) / courseWidth;
        double heightRatio = (courseHeight - (gps.getMaxY() - planeCoordinates.getY())) / courseHeight;
        System.out.println("course width = " + courseWidth);
        System.out.println("gps.getMaxX = " + gps.getMaxX());
        System.out.println("planeCoordinates.getX = " + planeCoordinates.getX());
        System.out.println("width ratio = " + widthRatio);
        System.out.println();
//        System.out.println("height ratio = " + heightRatio);

        return new XYPair(pixelWidth * widthRatio + PADDING, (pixelHeight * heightRatio + PADDING) * -1);
    }

}
