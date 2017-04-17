package seng302.team18.test_mock;

import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.Regatta;

/**
 * Created by Justin on 18/04/2017.
 */
public class TestMock {

    private Regatta regatta;
    private Course course;
    private Race race;

    public void generateClasses() {
        RegattaGenerator regattaGenerator = new RegattaGenerator();
        regattaGenerator.generate();
        regatta = regattaGenerator.getRegatta();


    }
}
