package seng302.team18.test_mock;

import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.messageparsing.AC35XMLBoatParser;
import seng302.team18.messageparsing.AC35XMLRaceParser;
import seng302.team18.messageparsing.AC35XMLRegattaParser;
import seng302.team18.model.*;
import seng302.team18.test_mock.model.BoatsModel;
import seng302.team18.test_mock.model.RaceModel;
import seng302.team18.test_mock.model.RegattaModel;

import java.time.ZoneId;
import java.util.List;

/**
 * Class to read each XML file and generate Race and Course objects from them
 */
public class RaceCourseGenerator {

    private AC35XMLRegattaMessage regattaMessage;
    private AC35XMLBoatMessage boatMessage;
    private AC35XMLRaceMessage raceMessage;

    /**
     * Generate xml messages.
     *
     */
    public void generateXmlMessages() {
        RegattaModel regattaModel = new RegattaModel();
        regattaMessage = regattaModel.getRegattaMessage();

        BoatsModel boatsModel = new BoatsModel();
        boatMessage = boatsModel.getBoatMessage();

        RaceModel raceModel = new RaceModel();
        raceMessage = raceModel.getRaceMessage();
    }


    /**
     * Generate a course
     *
     * Pre condition: generateXmlMessages has been called.
     * Post condition: Returns a course which is represented by the file read in readFile.
     *
     * @return Course, the course
     */
    public Course generateCourse() {
        Course course;
        List<CompoundMark> compoundMarks = raceMessage.getCompoundMarks();
        List<BoundaryMark> boundaryMarks = raceMessage.getBoundaryMarks();
        List<MarkRounding> markRoundings = raceMessage.getMarkRoundings();
        String utcOffset = regattaMessage.getUtcOffset();
        double windDirection = 0;
        ZoneId zoneId;

        if (utcOffset.startsWith("+") || utcOffset.startsWith("-")) {
            zoneId = ZoneId.of("UTC" + utcOffset);
        } else {
            zoneId = ZoneId.of("UTC+" + utcOffset);
        }

        Coordinate central = new Coordinate(regattaMessage.getCentralLat(), regattaMessage.getCentralLong());

        course = new Course(compoundMarks, boundaryMarks, windDirection, 10, zoneId, markRoundings);
        course.setCentralCoordinate(central);

        return course;
    }


    /**
     * Generate a Race
     *
     * Pre condition: readFiles has been called.
     * Post condition: Returns a race which is represented by the file read in readFile.
     *
     * @param course Course, the course
     * @return Race, the race
     */
    public Race generateRace(Course course) {
        List<Boat> startingList = boatMessage.getBoats();
        int raceID = raceMessage.getRaceID();
        Race race = new Race(startingList, course, raceID);

        return race;
    }
}
