package seng302.team18.test_mock;

import seng302.team18.message.AC35XMLBoatMessage;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.message.AC35XMLRegattaMessage;
import seng302.team18.messageparsing.AC35XMLBoatParser;
import seng302.team18.messageparsing.AC35XMLRaceParser;
import seng302.team18.messageparsing.AC35XMLRegattaParser;
import seng302.team18.model.*;

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
     * Read each test xml file and fill the containers so classes can be made
     *
     * Pre condition: Strings must represent real, existing file paths
     * Post condition: files will be read so that classes can be generated
     *
     * @param regattaXML file path
     * @param boatsXML file path
     * @param raceXML file path
     */
    public void readFiles(String regattaXML, String boatsXML, String raceXML) {
        AC35XMLRegattaParser regattaParser = new AC35XMLRegattaParser();
        regattaMessage = regattaParser.parse(this.getClass().getResourceAsStream(regattaXML));

        AC35XMLBoatParser boatsParser = new AC35XMLBoatParser();
        boatMessage = boatsParser.parse(this.getClass().getResourceAsStream(boatsXML));

        AC35XMLRaceParser raceParser = new AC35XMLRaceParser();
        raceMessage = raceParser.parse(this.getClass().getResourceAsStream(raceXML));
    }


    /**
     * Generate a course
     *
     * Pre condition: readFiles has been called.
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

        course = new Course(compoundMarks, boundaryMarks, windDirection, zoneId, markRoundings);
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
        Race race;

        List<Boat> startingList = boatMessage.getBoats();
        int raceID = raceMessage.getRaceID();

        race = new Race(startingList, course, raceID);

        return race;
    }
}
