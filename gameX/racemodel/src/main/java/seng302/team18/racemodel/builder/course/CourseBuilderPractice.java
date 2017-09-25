package seng302.team18.racemodel.builder.course;

import seng302.team18.model.CompoundMark;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Mark;
import seng302.team18.model.MarkRounding;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Builds a preset course to be used in the practice modes.
 * <p>
 * Concrete implementation of AbstractCourseBuilder.
 *
 * @see AbstractCourseBuilder
 */
public class CourseBuilderPractice extends AbstractCourseBuilder {

    @Override
    protected List<CompoundMark> buildCompoundMarks() {
        return new ArrayList<>();
    }


    @Override
    public List<Coordinate> getBoundaryMarks() {
        List<Coordinate> boundaries = new ArrayList<>();

        boundaries.add(new Coordinate(32.30009, -64.85787));
        boundaries.add(new Coordinate(32.30009, -64.85342));
        boundaries.add(new Coordinate(32.30419, -64.85342));
        boundaries.add(new Coordinate(32.30419, -64.85787));

        return boundaries;
    }


    @Override
    protected double getWindDirection() {
        return 200;
    }


    @Override
    protected double getWindSpeed() {
        return 30;
    }


    @Override
    protected ZoneId getTimeZone() {
        return ZoneId.ofOffset("UTC", ZoneOffset.ofHours(+12));
    }


    @Override
    protected List<MarkRounding> getMarkRoundings() {
        return new ArrayList<>();
    }
}
