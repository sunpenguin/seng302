package seng302.team18.test_mock;


import seng302.team18.test_mock.XMLparsers.AC35RegattaContainer;
import seng302.team18.test_mock.XMLparsers.AC35RegattaParser;

/**
 * Created by Justin on 18/04/2017.
 */
public class TestMock {

    AC35RegattaContainer ac35RegattaContainer;

    private void readFiles() {
        AC35RegattaParser ac35RegattaParser = new AC35RegattaParser();
        ac35RegattaContainer = ac35RegattaParser.parse(this.getClass().getResourceAsStream("/AC35regatta.xml"));

        AC35RaceParser ac35RaceParser = new AC35RaceParser();


    }

    public void run() {
        readFiles();
    }
}
