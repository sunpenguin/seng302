package seng302.team18.test_mock;

import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.Regatta;
import seng302.team18.test_mock.AC35XMLconstructors.AC35RegattaXMLConstructor;

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

    /**
     * Construct the files needed by instantiating the appropriate constructor classes.
     * For AC35, this would involve creating each AC35XMLConstructor and then calling construct()
     */
    public void constructFiles() {
        AC35RegattaXMLConstructor regattaXMLConstructor = new AC35RegattaXMLConstructor();
        regattaXMLConstructor.construct(regatta);
    }

}
