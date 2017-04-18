package seng302.team18.test_mock;

import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.Regatta;
import seng302.team18.test_mock.mock_generators.CourseGenerator;
import seng302.team18.test_mock.mock_generators.RaceGenerator;
import seng302.team18.test_mock.mock_generators.RegattaGenerator;


/**
 * Created by Justin on 18/04/2017.
 */
public class TestMock {

    private Regatta regatta;
    private Course course;
    private Race race;

    /**
     * Generate the classes necessary to visualise a mock race.
     * These are: Regatta, Race, Course
     */
    private void generateClasses() {
        RegattaGenerator regattaGenerator = new RegattaGenerator();
        regattaGenerator.generate();
        regatta = regattaGenerator.getRegatta();

        CourseGenerator courseGenerator = new CourseGenerator();
        courseGenerator.generate();
        course = courseGenerator.getCourse();

        RaceGenerator raceGenerator = new RaceGenerator();
        raceGenerator.generate();
        race = raceGenerator.getRace();
    }

    /**
     * Create a FileConstructor with the necessary objects as arguments (regatta, course, race)
     * so that the appropriate files can be constructed.
     */
    private void constructFiles() {
        FileConstructor fileConstructor = new FileConstructor(regatta, course, race);
        fileConstructor.constructFiles();
    }

    public void run() {
        generateClasses();
        constructFiles();
    }
}
