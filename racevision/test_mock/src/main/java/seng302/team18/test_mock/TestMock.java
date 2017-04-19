package seng302.team18.test_mock;

import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.Regatta;
import seng302.team18.test_mock.XMLparsers.AC35RegattaParser;

/**
 * Created by Justin on 18/04/2017.
 */
public class TestMock {

    private Regatta regatta = new Regatta();
//    private Course course = new Course();
//    private Race race = new Race();

    private void readFiles() {
        AC35RegattaParser ac35RegattaParser = new AC35RegattaParser();
        ac35RegattaParser.parse(this.getClass().getResourceAsStream("/AC35regatta.xml"), regatta);
    }

    public void run() {
        readFiles();
    }
}
