

package seng302.team18.racemodel.builder.course;

import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.MarkRounding;
import seng302.team18.util.GPSCalculator;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * The course to be used for bumper boats mode.
 *
 * NOTE: Course currently looks fine with the PixelMapper. Will need adjusting once distortions fixed.
 */
public class CourseBuilderBumper extends AbstractCourseBuilder {


    @Override
    protected List<MarkRounding> getMarkRoundings() {
        return new ArrayList<>();
    }


    @Override
    protected List<CompoundMark> buildCompoundMarks() {
        return new ArrayList<>();
    }


    @Override
    public List<Coordinate> getBoundaryMarks() {
        // The course can shrink, just need to update like spyro type
        List<Coordinate> boundaryMarks = new ArrayList<>();
        GPSCalculator calculator = new GPSCalculator();

        Coordinate origin = new Coordinate(5.00150, 4.0005);
        double distance = 200; // - count);

        Coordinate top = calculator.toCoordinate(origin, 0, distance);
        Coordinate topRight = calculator.toCoordinate(origin, 45, distance);
        Coordinate right = calculator.toCoordinate(origin, 90, distance);
        Coordinate bottomRight = calculator.toCoordinate(origin, 135, distance);
        Coordinate bottom = calculator.toCoordinate(origin, 180, distance);
        Coordinate bottomLeft = calculator.toCoordinate(origin, 225, distance);
        Coordinate left = calculator.toCoordinate(origin, 270, distance);
        Coordinate topLeft = calculator.toCoordinate(origin, 315, distance);

        boundaryMarks.add(top);
        boundaryMarks.add(topRight);
        boundaryMarks.add(right);
        boundaryMarks.add(bottomRight);
        boundaryMarks.add(bottom);
        boundaryMarks.add(bottomLeft);
        boundaryMarks.add(left);
        boundaryMarks.add(topLeft);

        return boundaryMarks;
    }


    @Override
    protected double getWindDirection() {
        return 180;
    }


    @Override
    protected double getWindSpeed() {
        return 45;
    }


    @Override
    protected ZoneId getTimeZone() {
        return ZoneId.ofOffset("UTC", ZoneOffset.ofHours(12));
    }
}

