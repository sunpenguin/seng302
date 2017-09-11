package seng302.team18.model;

import seng302.team18.util.GPSCalculator;

import java.util.List;

/**
 * Created by dhl25 on 11/09/17.
 */
public class StartLineSetter implements StartPositionSetter {

    private double distance;
    private GPSCalculator calculator = new GPSCalculator();


    public StartLineSetter(double distance) {
        this.distance = distance;
    }

    @Override
    public void setBoatPositions(List<Boat> boats, Course course) {
        for (Boat boat : boats) {
            Coordinate position = getBoatPosition(boat, course, boats.size());
            boat.setCoordinate(position);
        }
    }



    @Override
    public Coordinate getBoatPosition(Boat boat, Course course, int numBoats) {
        MarkRounding startRounding = course.getMarkRounding(0);
        Coordinate midPoint = startRounding.getCoordinate();
        Coordinate startMark1 = startRounding.getCompoundMark().getMarks().get(0).getCoordinate();
        Coordinate startMark2 = startRounding.getCompoundMark().getMarks().get(1).getCoordinate();

        double bearing = calculator.getBearing(startMark1, startMark2);

        double diff = (startRounding.getRoundingDirection().equals(MarkRounding.Direction.PS)) ? 90 : -90;
        double behind = (bearing + diff + 360) % 360;

        double offset = numBoats;

        if ((offset % 2) == 0) {
            offset /= 2;
        } else {
            offset = -Math.floor(offset / 2);
        }

        Coordinate behindMidPoint = calculator.toCoordinate(midPoint, behind, distance);
        return calculator.toCoordinate(behindMidPoint, bearing, (boat.getLength() * offset + 10));
    }
}
