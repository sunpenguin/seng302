package seng302.team18.racemodel.model;

import seng302.team18.model.BoundaryMark;
import seng302.team18.model.CompoundMark;
import seng302.team18.model.MarkRounding;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

public class CourseBuilderRealistic extends AbstractCourseBuilder {
    @Override
    protected List<MarkRounding> getMarkRoundings() {
        return null;
    }


    @Override
    protected List<CompoundMark> buildCompoundMarks() {
        return null;
    }


    @Override
    protected List<BoundaryMark> getBoundaryMarks() {
        return null;
    }


    @Override
    protected double getWindDirection() {
        return 145;
    }


    @Override
    protected double getWindSpeed() {
        return 30;
    }


    @Override
    protected ZoneId getTimeZone() {
        return ZoneId.ofOffset("UTC", ZoneOffset.ofHours(0));
    }
}
