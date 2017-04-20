package seng302.team18.test_mock;

import seng302.team18.model.Boat;
import seng302.team18.model.Course;
import seng302.team18.model.Race;
import seng302.team18.model.Regatta;
import seng302.team18.test_mock.XMLparsers.*;

import java.util.List;

/**
 * Created by Justin on 18/04/2017.
 */
public class TestMock {

    private AC35RegattaContainer ac35RegattaContainer;
    private AC35BoatsContainer ac35BoatsContainer;
    private AC35RaceContainer ac35RaceContainer;

    private void readFiles() {
        AC35RegattaParser ac35RegattaParser = new AC35RegattaParser();
        ac35RegattaContainer = ac35RegattaParser.parse(this.getClass().getResourceAsStream("/AC35regatta.xml"));

        AC35BoatsParser ac35BoatsParser = new AC35BoatsParser();
        ac35BoatsContainer = ac35BoatsParser.parse(this.getClass().getResourceAsStream("/AC35boats.xml"));

        AC35RaceParser ac35RaceParser = new AC35RaceParser();
        ac35RaceContainer = ac35RaceParser.parse(this.getClass().getResourceAsStream("/AC35race.xml"));

        System.out.println(ac35RaceContainer.getBoundaryMark().get(2).getCoordinate().getLatitude());
    }

    public void run() {
        readFiles();
    }
}
