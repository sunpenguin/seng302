package seng302.team18.visualiser.display;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import seng302.team18.model.Coordinate;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * Created by dhl25 on 30/06/17.
 */
public abstract class DisplayBoatDecorator extends DisplayBoat {

    private DisplayBoat boat;

    /**
     * Creates a new instance of DisplayBoat
     *
     * @param pixelMapper
     * @param name          the name of the boat
     * @param boatColor     the color to display the boat in
     * @param boat          the display boat being decorated
     */
    public DisplayBoatDecorator(PixelMapper pixelMapper, String name, Color boatColor, DisplayBoat boat) {
        super(pixelMapper, name, boatColor);
        this.boat = boat;
    }



    @Override
    public void setCoordinate(Coordinate coordinate) {
        boat.setCoordinate(coordinate);
    }


    @Override
    public void setSpeed(double speed) {
        boat.setSpeed(speed);
    }


    @Override
    public void setScale(double scaleFactor) {
        boat.setScale(scaleFactor);
    }


    @Override
    public void setEstimatedTime(Long estimatedTime) {
        boat.setEstimatedTime(estimatedTime);
    }


    @Override
    public void addToGroup(Group group) {
        boat.addToGroup(group);

    }


    @Override
    public void setAnnotationVisible(AnnotationType type, Boolean isVisible) {
        boat.setAnnotationVisible(type, isVisible);
    }


    @Override
    public Boolean isAnnotationVisible(AnnotationType type) {
        return boat.isAnnotationVisible(type);
    }


    @Override
    public Color getBoatColor() {
        return boat.getBoatColor();
    }


    @Override
    public void setTimeSinceLastMark(Long timeSinceLastMark) {
        boat.setTimeSinceLastMark(timeSinceLastMark);
    }


    @Override
    public boolean isControlled() {
        return boat.isControlled();
    }


    @Override
    public void setControlled(boolean controlled) {
        boat.setControlled(controlled);
    }


    @Override
    public int getStatus() {
        return boat.getStatus();
    }


    @Override
    public void setStatus(int status) {
        boat.setStatus(status);
    }


    @Override
    public DoubleProperty speedProperty() {
        return boat.speedProperty();
    }


    @Override
    public int getLegNumber() {
        return boat.getLegNumber();
    }


    @Override
    public IntegerProperty legNumberProperty() {
        return boat.legNumberProperty();
    }


    @Override
    public void setLegNumber(int legNumber) {
        boat.setLegNumber(legNumber);
    }


    @Override
    public Coordinate getCoordinate() {
        return boat.getCoordinate();
    }


    @Override
    public String getName() {
        return boat.getName();
    }


    @Override
    public double getHeading() {
        return boat.getHeading();
    }


    @Override
    public void setHeading(double heading) {
        boat.setHeading(heading);
    }


    @Override
    public String getShortName() {
        return boat.getShortName();
    }


    @Override
    public double getSpeed() {
        return boat.getSpeed();
    }


    @Override
    public Coordinate getDestination() {
        return boat.getDestination();
    }


    @Override
    public void setDestination(Coordinate destination) {
        boat.setDestination(destination);
    }


    @Override
    public int getPlace() {
        return boat.getPlace();
    }


    @Override
    public void setPlace(int place) {
        boat.setPlace(place);
    }


    @Override
    public IntegerProperty placeProperty() {
        return boat.placeProperty();
    }


    @Override
    public Integer getId() {
        return boat.getId();
    }


    @Override
    public long getTimeTilNextMark() {
        return boat.getTimeTilNextMark();
    }


    @Override
    public void setTimeTilNextMark(long timeTilNextMark) {
        boat.setTimeTilNextMark(timeTilNextMark);
    }


    @Override
    public Long getTimeSinceLastMark() {
        return boat.getTimeSinceLastMark();
    }


    @Override
    public void setTimeSinceLastMark(long timeSinceLastMark) {
        boat.setTimeSinceLastMark(timeSinceLastMark);
    }


    @Override
    public Long getTimeAtLastMark() {
        return boat.getTimeAtLastMark();
    }


    @Override
    public void setTimeAtLastMark(Long timeAtLastMark) {
        boat.setTimeAtLastMark(timeAtLastMark);
    }

}
