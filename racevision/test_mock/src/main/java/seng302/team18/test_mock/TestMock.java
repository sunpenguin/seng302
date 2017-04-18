package seng302.team18.test_mock;

import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.Regatta;
import seng302.team18.test_mock.XMLparsers.AC35RegattaParser;

/**
 * Created by Justin on 18/04/2017.
 */
public class TestMock {

    private Regatta regatta;
    private Course course;
    private Race race;

    private void readFiles() {
        AC35RegattaParser ac35RegattaParser = new AC35RegattaParser();
        ac35RegattaParser.parse(this.getClass().getResourceAsStream("/AC35Regatta.xml"));
    }

    public void run() {
        readFiles();
    }
}
