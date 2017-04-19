package seng302.team18.test_mock;


import seng302.team18.test_mock.XMLparsers.AC35BoatsContainer;
import seng302.team18.test_mock.XMLparsers.AC35BoatsParser;
import seng302.team18.test_mock.XMLparsers.AC35RegattaContainer;
import seng302.team18.test_mock.XMLparsers.AC35RegattaParser;

/**
 * Created by Justin on 18/04/2017.
 */
public class TestMock {

    AC35RegattaContainer ac35RegattaContainer;
    AC35BoatsContainer ac35BoatsContainer;

    private void readFiles() {
        AC35RegattaParser ac35RegattaParser = new AC35RegattaParser();
        ac35RegattaContainer = ac35RegattaParser.parse(this.getClass().getResourceAsStream("/AC35regatta.xml"));

        AC35BoatsParser ac35BoatsParser = new AC35BoatsParser();
        ac35BoatsContainer = ac35BoatsParser.parse(this.getClass().getResourceAsStream("/AC35boats.xml"));
    }

    public void run() {
        readFiles();
    }
}
