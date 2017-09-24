package seng302.team18.racemodel;

import seng302.team18.model.Race;
import seng302.team18.parse.AC35MessageParserFactory;
import seng302.team18.racemodel.builder.course.AbstractCourseBuilder;
import seng302.team18.racemodel.builder.course.CourseBuilderRealistic;
import seng302.team18.racemodel.builder.participants.AbstractParticipantsBuilder;
import seng302.team18.racemodel.builder.participants.ParticipantsBuilderSize20;
import seng302.team18.racemodel.builder.race.AbstractRaceBuilder;
import seng302.team18.racemodel.builder.race.RegularRaceBuilder;
import seng302.team18.racemodel.builder.regatta.AbstractRegattaBuilder;
import seng302.team18.racemodel.builder.regatta.RegattaBuilderRealistic;
import seng302.team18.racemodel.connection.ConnectionListener;
import seng302.team18.racemodel.connection.Server;
import seng302.team18.racemodel.encode.BoatXmlDefaults;
import seng302.team18.racemodel.encode.RaceXmlDefaults;
import seng302.team18.racemodel.encode.XmlMessageBuilder;


/**
 * Class to set up the mock stream
 */
public class MockDataStream {

    // Change concrete builders here to change the preset of race/regatta/course
    private static final AbstractRaceBuilder RACE_BUILDER = new RegularRaceBuilder();
    private static final AbstractCourseBuilder COURSE_BUILDER = new CourseBuilderRealistic();
    private static final AbstractRegattaBuilder REGATTA_BUILDER = new RegattaBuilderRealistic();
    private static final AbstractParticipantsBuilder PARTICIPANTS_BUILDER = new ParticipantsBuilderSize20();

    // Change the XmlDefault implementations to change the default values for the XML messages
    private static final XmlMessageBuilder XML_MESSAGE_BUILDER = new XmlMessageBuilder(new BoatXmlDefaults(), new RaceXmlDefaults());


    /**
     * Main setup method for the application.
     */
    private static void runMock(int serverPort) {

        Race race = RACE_BUILDER.buildRace(new Race(), REGATTA_BUILDER.buildRegatta(), COURSE_BUILDER.buildCourse());
        Server server = new Server(serverPort);
        ConnectionListener listener = new ConnectionListener(race, PARTICIPANTS_BUILDER.getIdPool(), new AC35MessageParserFactory());
        TestMock testMock = new TestMock(server, XML_MESSAGE_BUILDER, race, PARTICIPANTS_BUILDER.getParticipantPool());

        server.setCloseOnEmpty(true);
        server.addObserver(listener);
        listener.addObserver(testMock);
        server.open();
    }


    public static void main(String[] args) {
        final int DEFAULT_PORT = 5005;
        int serverPort = DEFAULT_PORT;

        if (args.length == 1) {
            try {
                serverPort = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Unable to parse ");
            }
        }

        if (serverPort < 1024 || serverPort > 65535) {
            serverPort = DEFAULT_PORT;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(MockDataStream::logProcessTermination));

        runMock(serverPort);

    }


    private static void logProcessTermination() {
        System.out.println("High Seas Server process terminated");
    }
}
