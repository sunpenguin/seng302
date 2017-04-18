package seng302.team18.test_mock;

import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.Regatta;

/**
 * Created by Justin on 18/04/2017.
 */
public class FileConstructor {

    private Regatta regatta;
    private Course course;
    private Race race;

    public FileConstructor(Regatta regatta, Course course, Race race) {
        this.regatta = regatta;
        this.course = course;
        this.race = race;
    }

    public void constructFiles() {

    }

}
