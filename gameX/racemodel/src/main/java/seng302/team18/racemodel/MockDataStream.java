package seng302.team18.racemodel;

import seng302.team18.messageparsing.AC35MessageParserFactory;
import seng302.team18.model.Race;
import seng302.team18.racemodel.ac35_xml_encoding.BoatXmlDefaults;
import seng302.team18.racemodel.ac35_xml_encoding.RaceXmlDefaults;
import seng302.team18.racemodel.ac35_xml_encoding.XmlMessageBuilder;
import seng302.team18.racemodel.connection.ConnectionListener;
import seng302.team18.racemodel.connection.Server;
import seng302.team18.racemodel.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

    private static int START_WAIT_TIME;
    private static int WARNING_WAIT_TIME;
    private static int PREP_WAIT_TIME;
    private static int MAX_PLAYERS;

    /**
     * Loads a properties file (config file) and reads the data.
     *
     * @param path Path to the properties file
     * @throws IOException                  Thrown if error occurs when reading the file
     * @throws InvalidPlayerNumberException Thrown if an invalid MAX_PLAYERS property is given in the file
     */
    public static void readConfig(String path) throws IOException, InvalidPlayerNumberException {
        Properties prop = new Properties();

        InputStream input = MockDataStream.class.getResourceAsStream(path);
        prop.load(input);

        START_WAIT_TIME = Integer.parseInt(prop.getProperty("START_WAIT_TIME"));
        WARNING_WAIT_TIME = Integer.parseInt(prop.getProperty("WARNING_WAIT_TIME"));
        PREP_WAIT_TIME = Integer.parseInt(prop.getProperty("PREP_WAIT_TIME"));
        MAX_PLAYERS = Integer.parseInt(prop.getProperty("MAX_PLAYERS"));

        if (MAX_PLAYERS < 1 || MAX_PLAYERS > 20) {
            throw new InvalidPlayerNumberException();
        }
    }


    /**
     * Main setup method for the application.
     */
    private static void runMock(int serverPort) {

        final int CUTOFF_DIFFERENCE = 0;

        try {
            readConfig("/config.txt");
        } catch (IOException e) {
            System.out.println("Error occurred reading configuration file");
        } catch (InvalidPlayerNumberException e) {
            System.out.println("Invalid maximum number of players in configuration file.\n" +
                    "Use a value between 1 and 20");
        }

        Race race = RACE_BUILDER.buildRace(new Race(), REGATTA_BUILDER.buildRegatta(), COURSE_BUILDER.buildCourse());
        Server server = new Server(serverPort, MAX_PLAYERS);
        ConnectionListener listener = new ConnectionListener(race, PARTICIPANTS_BUILDER.getIdPool(), new AC35MessageParserFactory());
        TestMock testMock = new TestMock(server, XML_MESSAGE_BUILDER, race, PARTICIPANTS_BUILDER.getParticipantPool(), COURSE_BUILDER);
        testMock.setSendRaceXML(true);


        server.setCloseOnEmpty(true);
        server.addObserver(listener);
        listener.addObserver(testMock);
        server.openServer();
        listener.setTimeout(System.currentTimeMillis() + ((WARNING_WAIT_TIME - CUTOFF_DIFFERENCE) * 1000));
        testMock.runSimulation(START_WAIT_TIME, WARNING_WAIT_TIME, PREP_WAIT_TIME, CUTOFF_DIFFERENCE);
        server.close();
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

        runMock(serverPort);
    }


    public static int getStartWaitTime() {
        return START_WAIT_TIME;
    }


    public static int getWarningWaitTime() {
        return WARNING_WAIT_TIME;
    }


    public static int getPrepWaitTime() {
        return PREP_WAIT_TIME;
    }


    public static int getMaxPlayers() {
        return MAX_PLAYERS;
    }
}
